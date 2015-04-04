var User = function(username, session) {
	this.username = username;
	this._session = session;
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

module.exports = User;

