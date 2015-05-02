var User = require('./user');
var config = require('./config');
var util = require('../util/util');
var io = require('socket.io')(config.port);
var xss = require('xss');
var CommandProcessor = require('./commands/command_processor');

global.rooms = [];
global.users = [];

//TODO: Request all existing rooms from CloudChatNavigator

io.on('connection', function(socket){
  socket.on('chat message', function(data) {
    var message = xss(data.message);
    
    if(CommandProcessor.process(io, socket, message))
      return;
    
    //TODO: Send message to room user is in
  });
  
  socket.on('login', function(username, password, roomName) {
    request.post(NAVIGATOR_URL, {
        form: {
          action: 'login',
          username: data.username,
          password: data.password
        }
      },
      function (error, response, body) {
          if (!error && response.statusCode == 200 && body != "-1") {
              var joinedRoom = RoomHelper.addUserToRoom(data.roomName);
              
              if(joinedRoom === -1) {
                console.log("Could not join room!");
              } else {
                console.log("User: " + username + " joined " + data.roomName);
              }
          }
      });
  });
});
