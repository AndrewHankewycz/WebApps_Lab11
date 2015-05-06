var request = require('request'),
  config = require('../config'),
  RoomHelper = require('../util/room_helper'),
  UserHelper = require('../util/user_helper');

/**
 * Command name mapped to the command function
 */
module.exports = {
  /**
   * Command for joining a room
   *
   * ex: /join pokemon
   */
  'join' : function(io, socket, args) {
    request.post(config.DAO_URL, {
        form: {
          action: 'searchRoom',
          topic: args
        }
      },
      function (error, response, body) {
          if (!error && response.statusCode == 200 && body != "-1") {
            var user = UserHelper.getUserFromRoom(socket.id);
            var roomData = RoomHelper.addUserToRoom(args, user);

            if(roomData === null)
              return;

            socket.emit('me_joined', {
              roomData: roomData
            });
          }
      });
  },
  /**
   * Command for creating a room. Sends a request to add the room to
   * the database, creates a global room, and adds the user to the room.
   *
   * ex: /create pokemon
   */
  'create' : function(io, socket, args) {
    var topic = args.toLowerCase();

    request.post(config.DAO_URL, {
        form: {
          action: 'createRoom',
          topic: topic
        }
      },
      function (error, response, body) {
          if (!error && response.statusCode == 200 && body != "-1") {
            var roomId = parseInt(body);
            RoomHelper.createRoom(roomId, topic);
            var roomData = RoomHelper.addUserToRoom(roomId);

            if(roomData === null)
              return;

            socket.emit('me_joined', {
              roomData: roomData
            });
          }
      });
  },
  /**
   * Command for leaving a specific room. Removes a user from
   * the room and notifies the other user's in the room that the
   * user has left.
   *
   * ex: /leave pokemon
   */
  'leave' : function(io, socket, args) {
    UserHelper.leaveRoomBySocketId(args, socket.id);
  },
  /**
   * Command for 'logging out' the user. Removes the user from all
   * rooms that they're in, and notifies every user in each of those
   * rooms
   */
  'exit' : function(io, socket, args) {
    UserHelper.removeBySocketId(socket.id);
  }
};
