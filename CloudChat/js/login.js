$(document).ready(function() {
  $("#registerButton").click(function() {
    $("#error").hide();

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
      },
      function(data) {
          if(data.error !== null) {
            $("#error").text(data.error);
          } else {
            $("#error").text("Successfully registered!");
            $("#formTitle").text('User Login');
            $("#roomInput").show();
          }

          $("#error").show();
      });
    }
  });

  $("#loginButton").click(function() {
    $("#error").hide();

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
          if(data.error !== null) {
            $("#error").text(data.error);
            return;
          }

          var room = data.room;
          if(!room)
            return;

          userId = data.userId;

          setChatUsers(room);

          roomIdViewing = room.id;
          room.messages = [];
          rooms.push(room);

          for(var i = 0; i < data.rooms.length; i++) {
            var roomId = data.rooms[i].id;
            var topic = data.rooms[i].topic;

            $("#roomsMenu").append('<li roomid="' + roomId + '" role="presentation"><a role="menuitem" tabindex="-1" onclick="selectRoomOrJoin(\'' + topic + '\')">'+topic+'</a></li>');
          }

          $("#loginContainer").fadeOut();
          $("#chatContainer").fadeIn();
      });

    } else {
      $("#formTitle").text('User Login');
      $("#roomInput").show();
    }
  });
});
