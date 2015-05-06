function setChatUsers(room) {
  for(var i = 0; i < room.users.length; i++) {
    var username = room.users[i].username;
    $("#users").append("<p>" + username + "</p>");
  }
}

function sendMessage() {
  var message = $("#messageBox").val();

  console.log(message + " " + roomIdViewing);

  if(message === '')
    return;

  socket.emit('message', {
    message: $("#messageBox").val(),
    roomId: roomIdViewing
  });

  $("#messageBox").val("");
}

socket.on('message', function(data) {
  if(data.roomId === roomIdViewing) {
    $("#messages").append("<li>" + data.username + ": " + data.message + "</li>");
  }

  //Keep track of the messages the user should see
  //regardless of whether they're currently viewing
  //the room that this message is being sent to. That
  //way when they switch to this room, they will immediately
  //be shown these messages without having to poll the server
  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].id === data.roomId) {
      rooms[i].messages.push({
        userId: data.userId,
        username: data.username,
        message: data.message
      });
    }
  }
});

socket.on('join', function(data) {
  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].id === data.roomId) {
      var username = data.username;

      rooms[i].users.push({
        userId: data.userId,
        username: username
      });

      $("#users").append("<p>" + username + "</p>");
      break;
    }
  }
});

socket.on('me_joined', function(data) {
  roomIdViewing = data.roomData.id;
  $("#messages").html("");
  $("#users").html("");
  setChatUsers(data.roomData);
});
