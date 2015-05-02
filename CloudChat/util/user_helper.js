var RoomHelper = require('./room_helper');

/**
 * Removes a user with a specific socket id from
 * all rooms that they were a part of
 * @param socketId Socket Id of the user to remove
 */
exports.removeBySocketId = function(socketId) {
  for(var i = 0; i < global.rooms.length; i++) {
    for(var j = 0; j < global.rooms[i].users.length; j++) {
      var room = global.rooms[i];
      var user = room.users[j];
      
      if(user.getConnection().id === socketId) {
        //Remove user from this room
        global.rooms[i].users.splice(j, 1);
        //Send event to users in this room to remove this user's name
        RoomHelper.streamEventToRoom('user_disconnected', {
          userId: user.getUserId()
        }, room.id);
      }
    }
  }
};
