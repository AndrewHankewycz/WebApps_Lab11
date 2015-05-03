var request = require('request'),
  User = require('./user'),
  config = require('./config'),
  util = require('../util/util'),
  io = require('socket.io')(config.port),
  xss = require('xss'),
  CommandProcessor = require('./commands/command_processor'),
  RoomHelper = require('./util/room_helper.js'),
  UserHelper = require('./util/user_helper.js');

global.rooms = [];
global.users = [];

RoomHelper.insertRoomsFromDB();

io.on('connection', function(socket) {
  socket.on('disconnect', function () {
    UserHelper.removeBySocketId(socket.id);
  });
  
  socket.on('chat message', function(data) {
    if(data === null)
      return;

    var message = xss(data.message);

    if(CommandProcessor.process(io, socket, message))
      return;

    //TODO: Send message to room user is in
  });
  
  socket.on('edit', function(data) {
    if(data === null)
      return;
  });

  socket.on('login', function(data, cb) {
    if(data === null)
      return;

    var username = data.username;
    
    if(config.DEBUG) {
      //TODO: Create a method to increment total users online for unique debugging user Ids.
      //TODO: Reuse the code below
      var user = new User(1, username, socket);
      var joinedRoom = RoomHelper.addUserToRoom(data.roomId.toLowerCase(), user);

      if(joinedRoom === -1) {
        console.log("Could not login!");
        cb({
          error: "Error on login!",
          user: null
        });
      } else {
        console.log("Logged in!");
        cb({
          error: null,
          user: {
            userId: user.getUserId(),
            username: username
          }
        });
      }
      return;
    }

    request.post(config.NAVIGATOR_URL, {
        form: {
          action: 'login',
          username: username,
          password: data.password
        }
      },
      function (error, response, body) {
          if (!error && response.statusCode == 200 && body != "-1") {
              var userResp = JSON.parse(body);
              var user = new User(userResp.id, username, socket);
              var joinedRoom = RoomHelper.addUserToRoom(data.roomId.toLowerCase(), user);

              if(joinedRoom === -1) {
                console.log("Could not login!");
                cb({
                  error: "Error on login!",
                  user: null
                });
              } else {
                console.log("Logged in!");
                cb({
                  error: null,
                  user: {
                    userId: user.getUserId(),
                    username: username
                  }
                });
              }
          }
      });
  });
});
