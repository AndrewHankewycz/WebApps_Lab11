var User = require('./user');
var config = require('./config');
var util = require('./util');
var io = require('socket.io')(config.port);

global.users = [];

io.on('connection', function(socket){
  socket.on('chat message', function(data) {
    var username = getUserBySession(data.session).username;
    io.emit('chat message', username + ': ' + data.message);
  });
  
  socket.on('login', function(username) {
    var session = addUser(username).getSession();
    socket.emit('login', session);
  });
});

function getUserBySession(session) {
	for(var i = 0; i < global.users.length; i++) {
		if(global.users[i].getSession() === session)
			return global.users[i];
	}
	return null;
}

function addUser(username) {
	global.users.push(new User(username, generateNewUserId()));
	return user;
}

function generateNewUserId() {
	var uId = util.generateRandomString(config.sessionIdSize);

	for(var i = 0; i < global.users.length; i++) {
		if(global.users[i].id == uId)
			return generateNewUserId();
	}

	return uId;
}
