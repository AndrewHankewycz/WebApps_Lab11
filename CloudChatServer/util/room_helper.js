var request = require('request'),
  config = require('../config'),
  User = require('../user'),
  self = this;

exports.insertRoomsFromDB = function() {
  if(config.DEBUG) {
    self.createRoom(1, 'pokemon');
    self.createRoom(2, 'dragons');
    return;
  }

  request.post(config.DAO_URL, {
      form: {
        action: 'getRooms'
      }
    },
    function (error, response, body) {
        if (!error && response.statusCode == 200) {
          var rooms = JSON.parse(body);
          for(var i = 0; i < rooms.length; i++) {
            self.createRoom(rooms[i].id, rooms[i].topic.toLowerCase());
          }
        }
    });
};

/**
 * Adds a user to a specified room and streams
 * user join event if the room and user pass checks
 * @param roomTopic Room name to add the user to
 * @param user User object to add to the room
 */
exports.addUserToRoom = function(roomTopic, user) {
  var roomData = null;

  for(var i = 0; i < global.rooms.length; i++) {
    if(global.rooms[i].topic === roomTopic) {
      //Stores non-sensitive user data
      var usersInRoom = [];

      //Check if user is already in this room.
      for(var j = 0; j < global.rooms[i].users.length; j++) {
          var userInRoom = global.rooms[i].users[j];
          usersInRoom.push({
            userId: userInRoom.getUserId(),
            username: userInRoom.getUsername()
          });
          if(userInRoom.getUserId() === user.getUserId()) {
            return null;
          }
      }

      global.rooms[i].users.push(user);

      usersInRoom.push({
        userId: user.getUserId(),
        username: user.getUsername()
      });

      roomData = {
        id: global.rooms[i].id,
        topic: global.rooms[i].topic,
        users: usersInRoom
      };
      break;
    }
  }

  //Inform the users
  if(roomData !== null) {
    self.streamEventToRoom('join', {
      roomId: roomData.id,
      userId: user.getUserId(),
      username: user.getUsername()
    }, roomData.id);
  }

  return roomData;
};

/**
 * Streams an event to specified room
 * @param event socket.io event name handled on the client
 * @param data Any type of data to be sent to the client
 * @param roomId Integer representing the Id of the room to send to
 */
exports.streamEventToRoom = function(event, data, roomId) {
  for(var i = 0; i < global.rooms.length; i++) {
    if(global.rooms[i].id === roomId) {
      for(var j = 0; j < global.rooms[i].users.length; j++) {
        var userInRoom = global.rooms[i].users[j];
        userInRoom.getConnection().emit(event, data);
      }
      return;
    }
  }
};

/**
 * Creates a room with an empty list of users
 */
exports.createRoom = function(roomId, topic) {
  for(var i = 0; i < global.rooms.length; i++) {
    if(global.rooms[i].id === roomId || global.rooms[i].topic === topic) {
      console.log("A room with this Id or topic already exists!");
      return;
    }
  }

  global.rooms.push({
    id: roomId,
    topic: topic,
    users: []
  });
};
