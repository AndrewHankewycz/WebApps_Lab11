var User = function(id) {
	this.id = id;
	this.answers = [];
	this.currentQuestionId = -1;
};

module.exports = User;