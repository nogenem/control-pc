$(function () {
  const EXEC_URL = window.location.origin + '/exec';
  const TYPE_URL = window.location.origin + '/type';
  const PRESS_KEY_URL = window.location.origin + '/press_key';
  const RELEASE_KEY_URL = window.location.origin + '/release_key';
  const MOVE_MOUSE_URL = window.location.origin + '/move_mouse';
  const MOUSE_DOWN_URL = window.location.origin + '/mouse_down';
  const MOUSE_UP_URL = window.location.origin + '/mouse_up';
  const MOUSE_CLICK_URL = window.location.origin + '/mouse_click';
  const KEYBOARD_LAYOUTS_URL = window.location.origin + '/keyboardLayouts/';

  const MOUSE_BUTTONS = {
    LEFT: 'LEFT',
    MIDDLE: 'MIDDLE',
    RIGHT: 'RIGHT',
    SCROLL_UP: 'SCROLL_UP',
    SCROLL_DOWN: 'SCROLL_DOWN',
  };
  const BTN_TOGGLE_STATES = {
    UNTOGGLED: 0,
    TOGGLED: 1,
    PRESSED: 2,
  };

  const mainContainer = $('#main-container');
  const commandsSelect = $('#command-select');
  const commandsCheckboxes = $('.checkboxes-wrapper > p input');
  const textarea = $('#text-command');
  const mouseArea = $('.mouse-area');
  const sensibilityInput = $('#sensibility-input');
  const keyboardContainer = $('#keyboard-container');
  const keyboardWrapper = $('#keyboard-wrapper');
  const keyboardLayoutSelect = $('#keyboard-layout-select');

  let lastX = null;
  let lastY = null;
  let mouseMovements = [];

  const keyboard_buttons = {};

  const mouseBtnData = {
    [MOUSE_BUTTONS.LEFT]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.MIDDLE]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.RIGHT]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.SCROLL_UP]: { timer: null, clicked: false },
    [MOUSE_BUTTONS.SCROLL_DOWN]: { timer: null, clicked: false },
  };

  const keyboardBtnData = {
    // [btn.key]: { timer: null, clicked: false },
  };

  const keyboardModifiersToggleState = {
    // [btn.key]: BTN_TOGGLE_STATES,
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

  /* Keyboard */
  function insertKeyboardButtons(layout) {
    keyboardWrapper.empty();

    const BASE_BTN_MARGIN = 2; //px
    // Se tiver mais altura do que largura
    const SHOULD_ROTATE_KEYBOARD = vh(1) > getMainContainerWidth(1);

    const MAX_ROWS = keyboard_buttons[layout].length;
    const MAX_BTNS_IN_A_ROW = Math.max(
      ...keyboard_buttons[layout].map(row => {
        return row.reduce((prev, curr) => {
          return prev + curr.width;
        }, 0);
      }),
    );

    const EXTRA_PADDING = getMainContainerWidth(5); //px
    const CONTAINER_PADDING =
      Number.parseInt(mainContainer.find('> div').css('padding-left')) +
      Number.parseInt(mainContainer.find('> div').css('padding-right')); //px
    let BASE_BTN_SIZE = 0;
    if (SHOULD_ROTATE_KEYBOARD) {
      BASE_BTN_SIZE =
        (getMainContainerWidth(100) -
          CONTAINER_PADDING -
          EXTRA_PADDING -
          (MAX_ROWS - 1) * BASE_BTN_MARGIN) /
        MAX_ROWS;
    } else {
      BASE_BTN_SIZE =
        (getMainContainerWidth(100) -
          CONTAINER_PADDING -
          EXTRA_PADDING -
          (MAX_BTNS_IN_A_ROW - 1) * BASE_BTN_MARGIN) /
        MAX_BTNS_IN_A_ROW;
    }

    let top = 0;
    let minWrapperWidth = Number.MIN_VALUE;
    keyboard_buttons[layout].forEach((row, rowIdx) => {
      let left = 0;
      row.forEach((data, elIdx) => {
        const width =
          BASE_BTN_SIZE * data.width + (data.width - 1) * BASE_BTN_MARGIN;
        const height =
          BASE_BTN_SIZE * data.height + (data.height - 1) * BASE_BTN_MARGIN;

        const button = document.createElement('button');
        button.className = 'waves-effect waves-light btn';
        button.style.width = `${width}px`;
        button.style.height = `${height}px`;
        button.style.fontSize = `${Math.min(width, height) / 4}px`;
        button.style.textTransform = 'none';
        button.dataset.key = data.key;

        if (SHOULD_ROTATE_KEYBOARD) {
          button.style.padding = 'clamp(1px, 1vw, 10px)';
        } else {
          button.style.padding = 'clamp(1px, 0.5vh, 10px)';
        }

        button.style.position = 'absolute';
        button.style.top = `${rowIdx * BASE_BTN_SIZE + top}px`;
        button.style.left = `${left}px`;

        if (data.labels.length === 0) {
          // just for spacing
          let html = '';
          html += '<div class="keyboard-button-text-wrapper">';
          html += '</div>';

          button.innerHTML = html;
          button.style.visibility = 'hidden';
        } else if (data.labels.length === 1) {
          let html = '';
          html += '<div class="keyboard-button-text-wrapper">';
          html += data.labels[0];
          html += '</div>';

          button.innerHTML = html;
        } else if (data.labels.length === 4) {
          let html = '';
          html += '<div class="keyboard-button-text-wrapper">';
          html += ' <div>';
          html += `   <span>${data.labels[0]}</span>`;
          html += `   <span>${data.labels[1]}</span>`;
          html += ' </div>';
          html += ' <div>';
          html += `   <span>${data.labels[2]}</span>`;
          html += `   <span>${data.labels[3]}</span>`;
          html += ' </div>';
          html += '</div>';

          button.innerHTML = html;
        }

        keyboardWrapper.append(button);

        let marginRight = 0;
        if (!!data.marginRight) {
          if (typeof data.marginRight === 'number') {
            marginRight = data.marginRight;
          } else {
            if (data.marginRight.px) {
              marginRight = data.marginRight.px;
            }
            if (data.marginRight.width) {
              marginRight +=
                BASE_BTN_SIZE * data.marginRight.width +
                data.marginRight.width * BASE_BTN_MARGIN;
            }
          }
        }

        left += width + BASE_BTN_MARGIN + marginRight;
      });
      top += BASE_BTN_MARGIN;

      if (left > minWrapperWidth) {
        minWrapperWidth = left;
      }
    });
    const minWrapperHeight =
      keyboard_buttons[layout].length * BASE_BTN_SIZE + top;
    keyboardWrapper[0].style.minHeight = `${minWrapperHeight}px`;
    keyboardWrapper[0].style.minWidth = `${minWrapperWidth}px`;

    if (SHOULD_ROTATE_KEYBOARD) {
      keyboardWrapper[0].style.transform =
        'rotate(90deg) translate3d( 0, 0, 0)';
    } else {
      keyboardWrapper[0].style.transform = '';
    }

    keyboardContainer[0].style.minWidth = keyboardWrapper[0].style.minHeight;
    keyboardContainer[0].style.minHeight = keyboardWrapper[0].style.minWidth;
  }

  async function onChange_KeyboardLayout(e) {
    const layout = e.target.value;

    if (!keyboard_buttons[layout]) {
      const data = await fetchKeyboardLayoutButtons(layout);

      keyboard_buttons[layout] = data;
    }

    insertKeyboardButtons(layout);
  }

  function onMouseDown_ModifierKey(key) {
    if (keyboardModifiersToggleState[key] === undefined) {
      keyboardModifiersToggleState[key] = BTN_TOGGLE_STATES.UNTOGGLED;
    }

    switch (keyboardModifiersToggleState[key]) {
      case BTN_TOGGLE_STATES.UNTOGGLED:
        keyboardModifiersToggleState[key] = BTN_TOGGLE_STATES.TOGGLED;
        keyboardWrapper.find(`button[data-key="${key}"]`).addClass('toggled');
        break;
      case BTN_TOGGLE_STATES.TOGGLED:
        keyboardModifiersToggleState[key] = BTN_TOGGLE_STATES.PRESSED;
        keyboardWrapper.find(`button[data-key="${key}"]`).addClass('underline');
        sendPressKey(key, true);
        break;
      case BTN_TOGGLE_STATES.PRESSED:
        keyboardModifiersToggleState[key] = BTN_TOGGLE_STATES.UNTOGGLED;
        keyboardWrapper
          .find(`button[data-key="${key}"]`)
          .removeClass('toggled underline');
        sendReleaseKey(key, true);
        break;
    }
  }

  function untoggleAllModifierKeys() {
    Object.keys(keyboardModifiersToggleState).forEach(key => {
      if (keyboardModifiersToggleState[key] === BTN_TOGGLE_STATES.TOGGLED) {
        keyboardModifiersToggleState[key] = BTN_TOGGLE_STATES.UNTOGGLED;
        keyboardWrapper
          .find(`button[data-key="${key}"]`)
          .removeClass('toggled');
      }
    });
  }

  function resendPressKey(key) {
    sendPressKey(key);

    keyboardBtnData[key].timer = setTimeout(() => {
      resendPressKey(key);
    }, 100);
  }

  function onMouseDown_Keyboard(e) {
    const target = $(e.target).closest('button');
    const key = `${target.data('key')}`;

    e.preventDefault();

    if (!isModifierKey(key)) {
      if (!keyboardBtnData[key])
        keyboardBtnData[key] = { timer: null, clicked: false };
      else if (keyboardBtnData[key].clicked) return true;

      keyboardBtnData[key].clicked = true;

      sendPressKey(key);

      keyboardBtnData[key].timer = setTimeout(() => {
        resendPressKey(key);
      }, 500);
    } else {
      onMouseDown_ModifierKey(key);
    }
  }

  function onMouseUp_Keyboard(e) {
    const target = $(e.target).closest('button');
    const key = `${target.data('key')}`;

    e.preventDefault();

    if (isModifierKey(key)) return true;

    if (!keyboardBtnData[key])
      keyboardBtnData[key] = { timer: null, clicked: false };
    else if (!keyboardBtnData[key].clicked) return true;

    if (!!keyboardBtnData[key].timer) {
      clearTimeout(keyboardBtnData[key].timer);
      keyboardBtnData[key].timer = null;
    }

    sendReleaseKey(key);

    keyboardBtnData[key].clicked = false;

    untoggleAllModifierKeys();
  }

  keyboardLayoutSelect.select2({ width: '100%', minimumResultsForSearch: -1 });
  keyboardLayoutSelect.change(onChange_KeyboardLayout);

  keyboardLayoutSelect.trigger('change');

  keyboardWrapper.on('touchstart mousedown', '.btn', onMouseDown_Keyboard);
  keyboardWrapper.on('touchend mouseup', '.btn', onMouseUp_Keyboard);

  if (screen.orientation) {
    screen.orientation.addEventListener(
      'change',
      () => {
        insertKeyboardButtons(keyboardLayoutSelect.val());
      },
      false,
    );
  } else {
    window.addEventListener(
      'orientationchange',
      () => {
        insertKeyboardButtons(keyboardLayoutSelect.val());
      },
      false,
    );
  }

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

  /* Fetch functions */
  function fetchKeyboardLayoutButtons(layout) {
    return $.ajax({
      url: KEYBOARD_LAYOUTS_URL + layout + '.json',
      method: 'GET',
      dataType: 'json',
    });
  }

  /* Send functions */
  function sendCommands(commands) {
    $.ajax({
      url: EXEC_URL,
      method: 'POST',
      data: {
        commands,
        layout: keyboardLayoutSelect.val(),
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

  function sendPressKey(key, isModifierKey = false) {
    if (!isModifierKey) {
      const toggledModifierKeys = getToggledModifierKeys();

      if (toggledModifierKeys.length > 0) {
        toggledModifierKeys.push(key);

        return sendCommands(toggledModifierKeys);
      }
    }

    $.ajax({
      url: PRESS_KEY_URL,
      method: 'POST',
      data: {
        key,
        layout: keyboardLayoutSelect.val(),
      },
    });
  }

  function sendReleaseKey(key, isModifierKey = false) {
    if (!isModifierKey) {
      const toggledModifierKeys = getToggledModifierKeys();

      if (toggledModifierKeys.length > 0) {
        // In this case, i did send a command instead of press before
        return;
      }
    }

    $.ajax({
      url: RELEASE_KEY_URL,
      method: 'POST',
      data: {
        key,
        layout: keyboardLayoutSelect.val(),
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

  /* Utils */
  function isScrollBtn(button) {
    return (
      button === MOUSE_BUTTONS.SCROLL_UP || button === MOUSE_BUTTONS.SCROLL_DOWN
    );
  }

  // https://stackoverflow.com/a/44109531
  function vh(v) {
    var h = Math.max(
      document.documentElement.clientHeight,
      window.innerHeight || 0,
    );
    return (v * h) / 100;
  }

  function vw(v) {
    var w = Math.max(
      document.documentElement.clientWidth,
      window.innerWidth || 0,
    );
    return (v * w) / 100;
  }

  function vmin(v) {
    return Math.min(vh(v), vw(v));
  }

  function vmax(v) {
    return Math.max(vh(v), vw(v));
  }

  function getMainContainerWidth(v) {
    const w = mainContainer[0].clientWidth;
    return (v * w) / 100;
  }

  function isModifierKey(key) {
    return !!key.match(
      /_(shift|ctrl|control|win|windows|cmd|command|alt|alt_gr|alt_graph)_/i,
    );
  }

  function getToggledModifierKeys() {
    const ret = [];
    Object.keys(keyboardModifiersToggleState).forEach(key => {
      if (keyboardModifiersToggleState[key] === BTN_TOGGLE_STATES.TOGGLED) {
        ret.push(key);
      }
    });
    return ret;
  }
});
