$(function() {
  const EXEC_URL = window.location.origin + '/exec';
  const TYPE_URL = window.location.origin + '/type';
  const MOVE_MOUSE_URL = window.location.origin + '/move_mouse';
  const MOUSE_DOWN_URL = window.location.origin + '/mouse_down';
  const MOUSE_UP_URL = window.location.origin + '/mouse_up';
  const MOUSE_CLICK_URL = window.location.origin + '/mouse_click';

  const BUTTONS = {
    LEFT: 'LEFT',
    MIDDLE: 'MIDDLE',
    RIGHT: 'RIGHT',
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
    [BUTTONS.LEFT]: { timer: null, clicked: false },
    [BUTTONS.MIDDLE]: { timer: null, clicked: false },
    [BUTTONS.RIGHT]: { timer: null, clicked: false },
  };

  commandsSelect.select2({
    width: '100%',
    tags: true,
    createTag: function(params) {
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
  $('.media-controls-wrapper button:not(.dont-show)').click(function() {
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
  $('#send-commands').click(function() {
    let data = commandsSelect.select2('data');
    data = data.map(option => option.id);

    const modifierKeys = [];
    commandsCheckboxes.each(function() {
      const checkbox = $(this);
      if (checkbox.is(':checked')) modifierKeys.push(checkbox.data('command'));
    });

    sendCommands([...modifierKeys, ...data]);
  });

  $('#clear-commands').click(function() {
    commandsSelect.val(null).trigger('change');
    commandsCheckboxes.each(function() {
      const checkbox = $(this);
      if (checkbox.is(':checked')) checkbox.parent().click();
    });
  });

  /* Text Controls */
  $('#send-text').click(function() {
    const text = $.trim(textarea.val());

    sendText(text);
  });

  $('#clear-text').click(function() {
    textarea.val('');
  });

  /* Mouse Area */
  function onMouseDown_MouseArea(e) {
    if (e.originalEvent.type === 'mousedown') {
      lastX = event.clientX;
      lastY = event.clientY;
    } else if (e.originalEvent.type === 'touchstart') {
      const touches = e.originalEvent.touches;
      lastX = touches[0].pageX;
      lastY = touches[0].pageY;
    }

    if (e.originalEvent.type === 'touchstart') {
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

    const touches = e.originalEvent.touches;
    let movement;

    if (e.originalEvent.type === 'mousemove')
      // movement = [lastX - event.clientX, lastY - event.clientY];
      movement = [event.clientX - lastX, event.clientY - lastY];
    else if (e.originalEvent.type === 'touchmove') {
      // movement = [lastX - touches[0].pageX, lastY - touches[0].pageY];
      movement = [touches[0].pageX - lastX, touches[0].pageY - lastY];
    }

    if (movement[0] === 0 && movement[1] === 0) return true;

    mouseMovements.push(movement);

    if (mouseMovements.length === 20) {
      sendMoveMouse([...mouseMovements]);
      mouseMovements = [];
    }

    if (e.originalEvent.type === 'mousemove') {
      lastX = event.clientX;
      lastY = event.clientY;
    } else if (e.originalEvent.type === 'touchmove') {
      lastX = touches[0].pageX;
      lastY = touches[0].pageY;
    }

    if (e.originalEvent.type === 'touchmove') {
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
    mouseBtnData[button].timer = setTimeout(() => {
      sendMouseDown(button);
      mouseBtnData[button].timer = null;
    }, 250);
    mouseBtnData[button].clicked = true;
  }

  function onMouseUp_MouseButtons(e, button) {
    if (mouseBtnData[button].timer !== null) {
      clearTimeout(mouseBtnData[button].timer);
      sendMouseClick(button);
    } else {
      sendMouseUp(button);
    }

    mouseBtnData[button].clicked = false;
  }

  $('#left-mouse').mousedown(function(e) {
    onMouseDown_MouseButtons(e, BUTTONS.LEFT);
  });
  $('#left-mouse').on('touchstart', function(e) {
    onMouseDown_MouseButtons(e, BUTTONS.LEFT);
  });

  $('#middle-mouse').mousedown(function(e) {
    onMouseDown_MouseButtons(e, BUTTONS.MIDDLE);
  });
  $('#middle-mouse').on('touchstart', function(e) {
    onMouseDown_MouseButtons(e, BUTTONS.MIDDLE);
  });

  $('#right-mouse').mousedown(function(e) {
    onMouseDown_MouseButtons(e, BUTTONS.RIGHT);
  });
  $('#right-mouse').on('touchstart', function(e) {
    onMouseDown_MouseButtons(e, BUTTONS.RIGHT);
  });

  function onMouseUp(e) {
    const evtType = !!e.originalEvent ? e.originalEvent.type : e.type;

    if (!!lastX && !!lastY) {
      onMouseUp_MouseArea(e);
    }

    const id = e.target.id;
    if (id === 'left-mouse' && mouseBtnData[BUTTONS.LEFT].clicked) {
      onMouseUp_MouseButtons(e, BUTTONS.LEFT);
    } else if (id === 'middle-mouse' && mouseBtnData[BUTTONS.MIDDLE].clicked) {
      onMouseUp_MouseButtons(e, BUTTONS.MIDDLE);
    } else if (id === 'right-mouse' && mouseBtnData[BUTTONS.RIGHT].clicked) {
      onMouseUp_MouseButtons(e, BUTTONS.RIGHT);
    }

    if (evtType === 'touchend') {
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
      function(prev, curr) {
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
    if (!BUTTONS[button]) return;

    $.ajax({
      url: MOUSE_DOWN_URL,
      method: 'POST',
      data: {
        button,
      },
    });
  }

  function sendMouseUp(button) {
    if (!BUTTONS[button]) return;

    $.ajax({
      url: MOUSE_UP_URL,
      method: 'POST',
      data: {
        button,
      },
    });
  }

  function sendMouseClick(button) {
    if (!BUTTONS[button]) return;

    $.ajax({
      url: MOUSE_CLICK_URL,
      method: 'POST',
      data: {
        button,
      },
    });
  }
});
