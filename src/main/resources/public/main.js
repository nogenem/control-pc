$(function() {
  const EXEC_URL = window.location.origin + '/exec';

  $('.media-controls-wrapper button:not(.dont-show)').click(function() {
    const btn = $(this);

    try {
      const commands = btn.data('commands');
      if (!commands || !Array.isArray(commands) || commands.length === 0)
        return false;

      $.ajax({
        url: EXEC_URL,
        method: 'POST',
        data: {
          commands
        }
      });

      return true;
    } catch (err) {
      console.error(err);
      return false;
    }
  });
});
