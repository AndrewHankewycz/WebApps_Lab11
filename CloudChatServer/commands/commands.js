var request = require('request'),
  config = require('../config'),
  RoomHelper = require('../util/room_helper'),
  UserHelper = require('../util/user_helper'),
  self = this;

/**
 * Command name mapped to the command function
 */
var Commands = module.exports = {
  /**
   * Command for joining a room
   *
   * ex: /join pokemon
   */
  'join' : {
    doc: "Join a room. ex: /join pokemon",
    command : function(io, socket, args) {
      request.post(config.DAO_URL, {
          form: {
            action: 'searchRoom',
            topic: args
          }
        },
        function (error, response, body) {
            if (!error && response.statusCode == 200 && body != "-1") {
              var user = UserHelper.getUserFromRoom(socket.id);

              if(user === null) {
                return;
              }

              var roomData = RoomHelper.addUserToRoom(args, user);

              if(roomData === null)
                return;

              socket.emit('me_joined', {
                roomData: roomData
              });
            }
        });
    }
  },
  /**
   * Command for creating a room. Sends a request to add the room to
   * the database, creates a global room, and adds the user to the room.
   *
   * ex: /create pokemon
   */
  'create' : {
    doc: "Create a room and join it ex: /create pokemon",
    command : function(io, socket, args) {
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
              var user = UserHelper.getUserFromRoom(socket.id);

              if(user === null) {
                return;
              }

              var roomData = RoomHelper.addUserToRoom(topic, user);

              if(roomData === null)
                return;

              socket.emit('me_joined', {
                roomData: roomData
              });
            }
        });
    }
  },
  /**
   * Command for leaving a specific room. Removes a user from
   * the room and notifies the other user's in the room that the
   * user has left.
   *
   * ex: /leave pokemon
   */
   'leave' : {
     doc: "Leave a room. ex: /leave pokemon. If a user leaves the only room they're in then the user will logout.",
     command : function(io, socket, args) {
       console.log("Leaving room");
       UserHelper.leaveRoomBySocketId(args, socket.id);
     }
   },
  /**
   * Command for 'logging out' the user. Removes the user from all
   * rooms that they're in, and notifies every user in each of those
   * rooms
   */
   'logout' : {
     doc: "Removes user from all rooms and notifies other users in those rooms.",
     command : function(io, socket, args) {
       console.log("User logging out");
       UserHelper.removeBySocketId(socket.id);
       socket.emit('logout');
     }
   },
  /**
  * Command for sending the user information about all commands
  */
  'help' : {
    doc: "Sending the user documentation about all commands.",
    command : function(io, socket, args) {
      var commandDocs = [];
      //Send help documentation
      for(var key in Commands) {
        var value = Commands[key].doc;
        commandDocs.push({
          command: key,
          doc: value
        })
      }

      socket.emit("help", commandDocs);
    }
  }
};
