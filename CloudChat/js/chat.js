var commands = {
  'join': function(args) {
    for(var i = 0; i < rooms.length; i++) {
      if(rooms[i].topic === args) {
        roomIdViewing = rooms[i].id;

        $("#messages").html("");
        $("#users").html("");

        setChatUsers(rooms[i]);

        for(var j = 0; j < rooms[i].messages.length; j++) {
          var message = rooms[i].messages[j];
          $("#messages").append("<li>" + message.username + ": " + message.message + "</li>");
        }
        //We don't need to make a request to the server if this room is cached on the client
        return true;
      }
    }
    return false;
  }
};

function commandProcessor(message) {
  if(message[0] !== '/')
    return false;

  var command = message.substr(1, message.indexOf(' ') - 1);
  var exeCmd = commands[command];

  if(typeof(exeCmd) !== 'function')
    return false;

  var remainingMsg = message.substr(message.indexOf(' ') + 1, message.length - 1);
  return exeCmd(remainingMsg);
};

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

  var commandHandledOnClient = commandProcessor(message);


  if(!commandHandledOnClient) {
    socket.emit('message', {
      message: $("#messageBox").val(),
      roomId: roomIdViewing
    });
  }

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

  //Check if the room already exists in
  //the local cache before adding it
  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].id === data.roomData.id) {
      //Update the users inside of this room
      //to the most recent list
      rooms[i].users = data.roomData.users;
      return;
    }
  }

  data.roomData.messages = [];
  rooms.push(data.roomData);
});
