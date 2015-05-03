var User = function(userId, username, connection) {
  this._userId = userId;
	this._username = username;
	this._connection = connection;
	this._messages = [];
};

User.prototype.addMessage = function(msg) {
	this._messages.push(msg);
};

User.prototype.getSession = function() {
	return this._session;
};

User.prototype.getMessages = function() {
	return this._messages;
};

User.prototype.getConnection = function() {
  return this._connection;
};

User.prototype.getUserId = function() {
  return this._userId;
};

User.prototype.getUsername = function() {
  return this._username;
};

module.exports = User;
