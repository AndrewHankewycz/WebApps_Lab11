var timerId;
timerId = setInterval(function() {
  if($('#userUpload').val() !== '') {
        clearInterval(timerId);
        $('#uploadForm').submit();
    }
}, 500);

$('#uploadForm').submit(function() {
  $(this).ajaxSubmit({
    dataType: 'text',

    error: function(xhr) {
      status('XHR: ' + JSON.stringify(xhr));
      return false;
      //status('Sorry, something went wrong!')
    },

    success: function(response) {
        try {
            alert('success');
            response = $.parseJSON(response);
            console.log(response);
        }
        catch(e) {
            status('Bad response from server');
            return false;
        }

        if(response.error) {
            status(response.error);
            //status('Oops, something bad happened');
            return false;
        }

        status("Uploading file!");

        $("#uploadForm :input").prop("disabled", true);
        $("#uploadForm :button").prop("disabled", true);
    }
  });
  return false;
});
