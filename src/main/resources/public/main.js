$(function() {
  const EXEC_URL = window.location.origin + '/exec';
  const TYPE_URL = window.location.origin + '/type';
  const MOVE_MOUSE_URL = window.location.origin + '/move_mouse';

  const commandsSelect = $('#command-select');
  const commandsCheckboxes = $('.checkboxes-wrapper > p input');
  const textarea = $('#text-command');
  const mouseArea = $('.mouse-area');
  const sensibilityInput = $('#sensibility-input');

  let lastX = null;
  let lastY = null;
  let mouseMovements = [];

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

  $('#send-text').click(function() {
    const text = $.trim(textarea.val());

    sendText(text);
  });

  $('#clear-text').click(function() {
    textarea.val('');
  });

  /* Mouse Area */
  function onMouseDown(e) {
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

  function onMouseUp() {
    if (mouseMovements.length > 0) {
      sendMoveMouse([...mouseMovements]);
      mouseMovements = [];
    }

    lastX = null;
    lastY = null;
  }

  function onMouseMove(e) {
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

    console.log('mov', movement);

    if (e.originalEvent.type === 'touchmove') {
      e.preventDefault();
      e.stopPropagation();

      return false;
    }
  }

  mouseArea.mousedown(onMouseDown);
  mouseArea.mouseup(onMouseUp);
  mouseArea.mousemove(onMouseMove);

  mouseArea.on('touchstart', onMouseDown);
  mouseArea.on('touchend', onMouseUp);
  mouseArea.on('touchmove', onMouseMove);

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
});
