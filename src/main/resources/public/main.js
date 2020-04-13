$(function() {
  const EXEC_URL = window.location.origin + '/exec';
  const TYPE_URL = window.location.origin + '/type';

  const commandsSelect = $('#command-select');
  const commandsCheckboxes = $('.checkboxes-wrapper > p input');
  const textarea = $('#text-command');

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
        text: term
      };
    }
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

  function sendCommands(commands) {
    $.ajax({
      url: EXEC_URL,
      method: 'POST',
      data: {
        commands
      }
    });
  }

  function sendText(text) {
    $.ajax({
      url: TYPE_URL,
      method: 'POST',
      data: {
        text
      }
    });
  }
});
