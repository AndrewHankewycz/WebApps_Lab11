var self = this;

/**
 * Adds a user to a specified room and streams
 * user join event if the room and user pass checks
 * @param roomId Integer representing the room to add the user to
 * @param user User object to add to the room
 */
exports.addUserToRoom = function(roomId, user) {
  var roomIdAdded = -1;
  
  for(var i = 0; i < global.rooms.length; i++) {
    if(global.rooms[i].id === roomId) {
      //Check if user is already in this room.
      for(var j = 0; j < global.rooms[i].users.length; j++) {
          var userInRoom = global.rooms[i].users[j];
          if(userInRoom.getUserId() === user.getUserId())
            return roomIdAdded;
      }
      
      global.rooms[i].users.push(user);
      roomIdAdded = global.rooms[i].id;
      break;
    }
  }
  
  //Inform the users
  if(roomIdAdded != -1) {
    self.streamEventToRoom('join', {
      userId: user.getUserId(),
      username: user.getUsername()
    }, roomId);
  }
  
  return roomIdAdded;
};

/**
 * Streams an event to specified room
 * @param event socket.io event name handled on the client
 * @param data Any type of data to be sent to the client
 * @param roomId Integer representing the Id of the room to send to
 */
exports.streamEventToRoom = function(event, data, roomId) {
  for(var i = 0; i < global.rooms.length; i++) {
    if(global.rooms[i].id === room) {
      for(var j = 0; j < global.rooms[i].length; j++) {
        var userInRoom = global.rooms[i].users[j];
        userInRoom.getConnection().emit(event, data);
      }
    }
  }
};
