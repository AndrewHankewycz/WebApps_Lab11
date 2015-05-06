function setChatUsers(room) {
  for(var i = 0; i < room.users.length; i++) {
    var username = room.users[i].username;
    $("#users").append("<p>" + username + "</p>");
  }
}

function sendMessage() {
  var message = $("#messageBox").val();

  if(message === '')
    return;

  socket.emit('message', {
    message: $("#messageBox").val(),
    roomId: roomIdViewing
  });

  $("#messageBox").val("");
}
