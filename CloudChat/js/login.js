var username, password, roomId;
var session;
var socket = io("http://localhost:3000");

$(document).ready(function() {
  $("#registerButton").click(function() {
    if($("#roomInput").is(":visible")) {
      $("#formTitle").text('User Register');
      $("#roomInput").hide();
    } else {
      var username = $("#usernameInput").val();
      var password = $("#passwordInput").val();

      if(username === "" || password === "") {
        $("#error").show();
        return;
      }

      socket.emit('register', {
        username: username,
        password: password
      });
      
      $("#loginContainer").fadeOut();
      $("#chatContainer").fadeIn();
    }
  });

  $("#loginButton").click(function() {
    if($("#roomInput").is(":visible")) {
      var username = $("#usernameInput").val();
      var password = $("#passwordInput").val();
      var roomId = $("#roomInput").val();

      if(username === "" || password === "" || roomId === "") {
        $("#error").show();
        return;
      }

      socket.emit('login', {
        username: username,
        password: password,
        roomId: roomId
      },
      function(data) {
          console.log(data.error);
      });
      
    } else {
      $("#formTitle").text('User Login');
      $("#roomInput").show();
    }
  });
});
