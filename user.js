var User = function(id) {
	this.id = id;
	this.answers = [];
	this.currentQuestionId = -1;
};

User.prototype.getId = function() {
	return this.id;
};

module.exports = User;
