var User = function(userId, username, connection) {
  this._userId = userId;
	this.username = username;
	//this._session = session;
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

module.exports = User;
