function selectRoom(topic) {
  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].topic === topic) {
      roomIdViewing = rooms[i].id;

      $("#messages").html("");
      $("#users").html("");

      setChatUsers(rooms[i]);

      for(var j = 0; j < rooms[i].messages.length; j++) {
        var message = rooms[i].messages[j];
        if(message.userId === userId) {
          $("#messages").append('<li messageid="' + message.id + '"><b>' + message.username + '</b>: ' + message.message + '<span class="btn btn-default message_button glyphicon glyphicon-remove" onclick="deleteMsg(\'' + message.id + '\',\'' + message.message + '\')"></span><span class="btn btn-default message_button glyphicon glyphicon-edit" onclick="editMsg(\'' + message.id + '\',\'' + message.message + '\')"></span></li>');
        } else {
          $("#messages").append("<li messageid='" + message.id+ "'>" + message.username + ": " + message.message + "</li>");
        }
      }
      //We don't need to make a request to the server if this room is cached on the client
      return true;
    }
  }
  return false;
}

function selectRoomOrJoin(topic) {
  if(selectRoom(topic)) {
    return;
  }

  socket.emit('message', {
    message: '/join ' + topic,
    roomId: roomIdViewing
  });
}

var commands = {
  'join': function(args) {
    return selectRoom(args);
  }
}

function commandProcessor(message) {
  if(message[0] !== '/')
    return false;

  var command = message.substr(1, message.indexOf(' ') - 1);
  var exeCmd = commands[command];

  if(typeof(exeCmd) !== 'function')
    return false;

  var remainingMsg = message.substr(message.indexOf(' ') + 1, message.length - 1);
  return exeCmd(remainingMsg);
}

function setChatUsers(room) {
  $("#users").append("<p style'text-align:center'>Users:</p>");

  for(var i = 0; i < room.users.length; i++) {
    var userId = room.users[i].userId;
    var username = room.users[i].username;
    $("#users").append("<p class='username' userid='" + userId + "'>" + username + "</p>");
  }
}

function deleteMsg(messageId, message) {
  socket.emit('delete_message', {
    messageId: messageId,
    roomId: roomIdViewing
  });

  messageIdEditing = -1;
}

function sendEditMessage() {
  var message = $("#messageBox").val();

  if(message === '') {
    deleteMsg();
    return;
  }

  socket.emit('edit_message', {
    messageId: messageIdEditing,
    message: message,
    roomId: roomIdViewing
  });

  messageIdEditing = -1;
  $("#messageBox").val("");
}

function sendMessage(messageSend) {
  var message = messageSend || $("#messageBox").val();

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

function editMsg(messageId, message) {
  $("#messageBox").val(message);
  messageIdEditing = messageId;
}

function logout() {
  roomIdViewing = -1;
  userId = -1;
  rooms = [];
  messageIdEditing = -1;

  $("#chatContainer").fadeOut();
  $("#loginContainer").fadeIn();
}

socket.on('logout', function() {
  logout();
});

function removeRoomUsernameByUserId(userIdRemove) {
  $('p').each(function() {
    var $this = $(this);
    var id = $this.attr('userid');

    if(id == userIdRemove) {
      $(this).remove();
    }
  });
}

socket.on('help', function(docs) {
  var message = "";
  for(var i = 0; i < docs.length; i++) {
    message += ("Command: " + docs[i].command + "\n" + "Doc: " + docs[i].doc + "\n\n");
  }
  alert(message);
});

socket.on('user_disconnected', function(data) {
  var roomId = data.roomId;
  var userIdDisconnect = data.userId;

  //If the user disconnected is this user logged in
  //then remove them from the room, attempt to join
  //another room if they're leaving the room they're looking at,
  //and if they're a part of none, log them out.
  if(userIdDisconnect === userId) {
    var roomIndex = -1;

    if(roomId === roomIdViewing) {
      for(var i = 0; i < rooms.length; i++) {
          if(rooms[i].id != roomId) {
            roomIdViewing = rooms[i].id;
            selectRoom(rooms[i].topic);
            break;
          } else {
            roomIndex = i;
          }
      }

      //If the current room of the user never changed, then we
      //know they were only a part of one room and cannot switch.
      //We just log them out
      if(roomId === roomIdViewing) {
        socket.emit('message', {
          message: '/logout',
          roomId: roomIdViewing
        });
        return;
      }
    }

    if(roomIndex === -1) {
      for(var i = 0; i < rooms.length; i++) {
        if(rooms[i].id === roomId) {
          roomIndex = i;
          break;
        }
      }
    }

    //Remove the room from the array if the current
    //user decided to leave the room
    rooms.splice(roomIndex, 1);
  }

  if(roomId === roomIdViewing) {
    removeRoomUsernameByUserId(userIdDisconnect);
  }

  for(var i = 0; i < rooms.length; i++) {
    for(var j = 0; j < rooms[i].users.length; j++) {
      var user = rooms[i].users[j];

      if(user.userId === userIdDisconnect) {
        rooms[i].users.splice(j, 1);
        return;
      }
    }
  }
});

socket.on('message_deleted', function(data) {
  if(data.roomId === roomIdViewing) {
    $("ul").find("[messageid='" + data.messageId + "']").html("<i>Message deleted</i>");
  }

  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].id === data.roomId) {
      for(var j = 0; j < rooms[i].messages.length; j++) {
        var message = rooms[i].messages[j];

        //If this is message is found, remove the old one
        if(message.id === data.messageId) {
          rooms[i].messages.splice(j, 1);
          return;
        }
      }
    }
  }
});

socket.on('message_edited', function(data) {
  if(data.roomId === roomIdViewing) {
    //Update the visible li for this message Id with the new message
    $("ul").find("[messageid='" + data.messageId + "']").html(
      '<li messageid="' + data.messageId + '">' + data.username + ': ' + data.message + '<span class="btn btn-default message_button glyphicon glyphicon-remove" onclick="deleteMsg(\'' + data.messageId + '\',\'' + data.message + '\')"></span><span class="btn btn-default message_button glyphicon glyphicon-edit" onclick="editMsg(\'' + data.messageId + '\',\'' + data.message + '\')"></span></li>'
    );
  }

  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].id === data.roomId) {
      for(var j = 0; j < rooms[i].messages.length; j++) {
        var message = rooms[i].messages[j];

        //If this is message is found, update the old one
        if(message.id === data.messageId) {
          rooms[i].messages[j].message = data.message;
          return;
        }
      }
    }
  }
});

socket.on('message', function(data) {
  if(data.roomId === roomIdViewing) {
    if(data.userId === userId) {
      $("#messages").append('<li messageid="' + data.messageId + '"><b>' + data.username + '</b>: ' + data.message + '<span class="btn btn-default message_button glyphicon glyphicon-remove" onclick="deleteMsg(\'' + data.messageId + '\',\'' + data.message + '\')"></span><span class="btn btn-default message_button glyphicon glyphicon-edit" onclick="editMsg(\'' + data.messageId + '\',\'' + data.message + '\')"></span></li>');
    } else {
      $("#messages").append("<li messageid='" + data.messageId + "'><b>" + data.username + "</b>: " + data.message + "</li>");
    }
  }

  //Keep track of the messages the user should see
  //regardless of whether they're currently viewing
  //the room that this message is being sent to. That
  //way when they switch to this room, they will immediately
  //be shown these messages without having to poll the server
  for(var i = 0; i < rooms.length; i++) {
    if(rooms[i].id === data.roomId) {
      rooms[i].messages.push({
        id: data.messageId,
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

      $("#users").append("<p userid='" + data.userId + "'>" + username + "</p>");
      break;
    }
  }
});

socket.on('me_joined', function(data) {
  roomIdViewing = data.roomData.id;
  var topic = data.roomData.topic;

  $("#messages").html("");
  $("#users").html("");
  setChatUsers(data.roomData);

  var roomTopicExists = $("ul").find("[roomid='" + roomIdViewing + "']").html();

  if(typeof(roomTopicExists) === 'undefined') {
    $("#roomsMenu").append('<li roomid="' + roomIdViewing + '" role="presentation"><a role="menuitem" tabindex="-1" onclick="selectRoomOrJoin(\'' + topic + '\')">'+topic+'</a></li>');
  }

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
