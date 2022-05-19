$(function () {
  const EXEC_URL = window.location.origin + '/exec';
  const TYPE_URL = window.location.origin + '/type';
  const MOVE_MOUSE_URL = window.location.origin + '/move_mouse';
  const MOUSE_DOWN_URL = window.location.origin + '/mouse_down';
  const MOUSE_UP_URL = window.location.origin + '/mouse_up';
  const MOUSE_CLICK_URL = window.location.origin + '/mouse_click';

  const MOUSE_BUTTONS = {
    LEFT: 'LEFT',
    MIDDLE: 'MIDDLE',
    RIGHT: 'RIGHT',
    SCROLL_UP: 'SCROLL_UP',
    SCROLL_DOWN: 'SCROLL_DOWN',
  };

  const commandsSelect = $('#command-select');
  const commandsCheckboxes = $('.checkboxes-wrapper > p input');
  const textarea = $('#text-command');
  const mouseArea = $('.mouse-area');
  const sensibilityInput = $('#sensibility-input');

  let lastX = null;
  let lastY = null;
  let mouseMovements = [];

  const mouseBtnData = {
    [MOUSE_BUTTONS.LEFT]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.MIDDLE]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.RIGHT]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.SCROLL_UP]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.SCROLL_DOWN]: { timer: null, clicked: false },
  };

  commandsSelect.select2({
    width: '100%',
    tags: true,
    createTag: function (params) {
      var term = $.trim(params.term);

      if (term === '') {
        return null;
      }

      return {
        id: term,
        text: term,
      };
    },
  });

  window.document.addEventListener('mouseup', onMouseUp);
  window.document.addEventListener('touchend', onMouseUp);

  /* Media Controls */
  $('.media-controls-wrapper button:not(.dont-show)').click(function (e) {
    const btn = $(this);

    try {
      const commands = btn.data('commands');
      if (!commands || !Array.isArray(commands) || commands.length === 0)
        return false;

      sendCommands(commands);
      return true;
    } catch (err) {
      console.error(err);
      return false;
    }
  });

  /* Command Controls */
  $('#send-commands').click(function () {
    let data = commandsSelect.select2('data');
    data = data.map(option => option.id);

    const modifierKeys = [];
    commandsCheckboxes.each(function () {
      const checkbox = $(this);
      if (checkbox.is(':checked')) modifierKeys.push(checkbox.data('command'));
    });

    sendCommands([...modifierKeys, ...data]);
  });

  $('#clear-commands').click(function () {
    commandsSelect.val(null).trigger('change');
    commandsCheckboxes.each(function () {
      const checkbox = $(this);
      if (checkbox.is(':checked')) checkbox.parent().click();
    });
  });

  /* Text Controls */
  $('#send-text').click(function () {
    const text = $.trim(textarea.val());

    sendText(text);
  });

  $('#clear-text').click(function () {
    textarea.val('');
  });

  /* Mouse Area */
  function onMouseDown_MouseArea(e) {
    const event = e.originalEvent;
    if (event.type === 'mousedown') {
      lastX = event.clientX;
      lastY = event.clientY;
    } else if (event.type === 'touchstart') {
      const touches = event.touches;
      lastX = touches[0].pageX;
      lastY = touches[0].pageY;
    }

    if (event.type === 'touchstart') {
      e.preventDefault();
      e.stopPropagation();

      return false;
    }
  }

  function onMouseUp_MouseArea() {
    if (mouseMovements.length > 0) {
      sendMoveMouse([...mouseMovements]);
      mouseMovements = [];
    }

    lastX = null;
    lastY = null;
  }

  function onMouseMove_MouseArea(e) {
    if (lastX === null || lastY === null) return true;

    const event = e.originalEvent;
    const touches = event.touches;
    let movement;

    if (event.type === 'mousemove')
      // movement = [lastX - event.clientX, lastY - event.clientY];
      movement = [event.clientX - lastX, event.clientY - lastY];
    else if (event.type === 'touchmove') {
      // movement = [lastX - touches[0].pageX, lastY - touches[0].pageY];
      movement = [touches[0].pageX - lastX, touches[0].pageY - lastY];
    }

    if (movement[0] === 0 && movement[1] === 0) return true;

    mouseMovements.push(movement);

    if (mouseMovements.length === 20) {
      sendMoveMouse([...mouseMovements]);
      mouseMovements = [];
    }

    if (event.type === 'mousemove') {
      lastX = event.clientX;
      lastY = event.clientY;
    } else if (event.type === 'touchmove') {
      lastX = touches[0].pageX;
      lastY = touches[0].pageY;
    }

    if (event.type === 'touchmove') {
      e.preventDefault();
      e.stopPropagation();

      return false;
    }
  }

  mouseArea.mousedown(onMouseDown_MouseArea);
  mouseArea.mousemove(onMouseMove_MouseArea);

  mouseArea.on('touchstart', onMouseDown_MouseArea);
  mouseArea.on('touchmove', onMouseMove_MouseArea);

  function onMouseDown_MouseButtons(e, button) {
    if (!!mouseBtnData[button].timer) {
      clearTimeout(mouseBtnData[button].timer);
      mouseBtnData[button].timer = null;
    }

    if (isScrollBtn(button)) {
      const delay = +$('#scroll-delay-input').val();
      mouseBtnData[button].timer = setInterval(() => {
        sendMouseClick(button);
      }, delay);
    } else {
      mouseBtnData[button].timer = setTimeout(() => {
        sendMouseDown(button);
        mouseBtnData[button].timer = null;
      }, 250);
    }
    mouseBtnData[button].clicked = true;
  }

  function onMouseUp_MouseButtons(e, button) {
    if (mouseBtnData[button].timer !== null) {
      if (isScrollBtn(button)) {
        clearInterval(mouseBtnData[button].timer);
        mouseBtnData[button].timer = null;
      } else {
        clearTimeout(mouseBtnData[button].timer);
        mouseBtnData[button].timer = null;
        sendMouseClick(button);
      }
    } else {
      sendMouseUp(button);
    }

    mouseBtnData[button].clicked = false;
  }

  $('#left-mouse').on('mousedown touchstart', function (e) {
    if (e.handled) return false;
    e.handled = true;

    onMouseDown_MouseButtons(e, MOUSE_BUTTONS.LEFT);
  });

  $('#middle-mouse').on('mousedown touchstart', function (e) {
    if (e.handled) return false;
    e.handled = true;

    onMouseDown_MouseButtons(e, MOUSE_BUTTONS.MIDDLE);
  });

  $('#right-mouse').on('mousedown touchstart', function (e) {
    if (e.handled) return false;
    e.handled = true;

    onMouseDown_MouseButtons(e, MOUSE_BUTTONS.RIGHT);
  });

  $('#scroll-up').on('mousedown touchstart', function (e) {
    if (e.handled) return false;
    e.handled = true;

    onMouseDown_MouseButtons(e, MOUSE_BUTTONS.SCROLL_UP);
  });

  $('#scroll-down').on('mousedown touchstart', function (e) {
    if (e.handled) return false;
    e.handled = true;

    onMouseDown_MouseButtons(e, MOUSE_BUTTONS.SCROLL_DOWN);
  });

  function onMouseUp(e) {
    if (e.handled) return false;
    e.handled = true;

    if (!!lastX && !!lastY) {
      onMouseUp_MouseArea(e);
    }

    const id = e.target.id;
    let hasBtnClicked = false;
    if (id === 'left-mouse' && mouseBtnData[MOUSE_BUTTONS.LEFT].clicked) {
      onMouseUp_MouseButtons(e, MOUSE_BUTTONS.LEFT);
      hasBtnClicked = true;
    } else if (
      id === 'middle-mouse' &&
      mouseBtnData[MOUSE_BUTTONS.MIDDLE].clicked
    ) {
      onMouseUp_MouseButtons(e, MOUSE_BUTTONS.MIDDLE);
      hasBtnClicked = true;
    } else if (
      id === 'right-mouse' &&
      mouseBtnData[MOUSE_BUTTONS.RIGHT].clicked
    ) {
      onMouseUp_MouseButtons(e, MOUSE_BUTTONS.RIGHT);
      hasBtnClicked = true;
    } else if (
      id === 'scroll-up' &&
      mouseBtnData[MOUSE_BUTTONS.SCROLL_UP].clicked
    ) {
      onMouseUp_MouseButtons(e, MOUSE_BUTTONS.SCROLL_UP);
      hasBtnClicked = true;
    } else if (
      id === 'scroll-down' &&
      mouseBtnData[MOUSE_BUTTONS.SCROLL_DOWN].clicked
    ) {
      onMouseUp_MouseButtons(e, MOUSE_BUTTONS.SCROLL_DOWN);
      hasBtnClicked = true;
    }

    const evtType = !!e.originalEvent ? e.originalEvent.type : e.type;
    if (hasBtnClicked && evtType === 'touchend') {
      e.preventDefault();
      e.stopPropagation();
      return false;
    }

    return true;
  }

  function sendCommands(commands) {
    $.ajax({
      url: EXEC_URL,
      method: 'POST',
      data: {
        commands,
      },
    });
  }

  function sendText(text) {
    $.ajax({
      url: TYPE_URL,
      method: 'POST',
      data: {
        text,
      },
    });
  }

  function sendMoveMouse(movements) {
    let mov = movements.reduce(
      function (prev, curr) {
        return [prev[0] + curr[0], prev[1] + curr[1]];
      },
      [0, 0],
    );

    const sensibility = +sensibilityInput.val();
    mov = [mov[0] * sensibility, mov[1] * sensibility];

    $.ajax({
      url: MOVE_MOUSE_URL,
      method: 'POST',
      data: {
        movements: JSON.stringify([mov]),
      },
    });
  }

  function sendMouseDown(button) {
    if (!MOUSE_BUTTONS[button]) return;

    $.ajax({
      url: MOUSE_DOWN_URL,
      method: 'POST',
      data: {
        button,
        sensibility,
      },
    });
  }

  function sendMouseUp(button) {
    if (!MOUSE_BUTTONS[button]) return;

    $.ajax({
      url: MOUSE_UP_URL,
      method: 'POST',
      data: {
        button,
      },
    });
  }

  function sendMouseClick(button) {
    if (!MOUSE_BUTTONS[button]) return;

    let sensibility = undefined;
    if (isScrollBtn(button))
      sensibility = +$('#scroll-sensibility-input').val();

    $.ajax({
      url: MOUSE_CLICK_URL,
      method: 'POST',
      data: {
        button,
        sensibility,
      },
    });
  }

  function isScrollBtn(button) {
    return (
      button === MOUSE_BUTTONS.SCROLL_UP || button === MOUSE_BUTTONS.SCROLL_DOWN
    );
  }
});
