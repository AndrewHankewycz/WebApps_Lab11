var nodemailer = require('nodemailer');
var User = require('../user');
var util = require('../util/util');
var questions = require('./questions.js').questions;
var config = require('../config').config;
var smtpTransport = nodemailer.createTransport();
//Get base path of project
var root = process.cwd();
var curNum = 1;

function gettool(req, res) {
	if (req.path === "/EvalTool/testing") {
		var userId = req.query.userId;
		var userSessPosition = getUserSessPositionFromUserId(userId);

		//Next = 1, previous = -1
		var direction = parseInt(req.query.direction);

		if(typeof(userId) !== 'undefined' && userSessPosition != null) {
			validateAnswer(userSessPosition, req, res);
			global.users[userSessPosition].currentQuestionId += direction;

			var user = global.users[userSessPosition];

			sendQuestion(user, req, res);
		}
	} else if(req.path == "/EvalTool/login") {
		var uId = generateNewUserId();
		global.users.push(new User(uId));

		var userSessPosition = getUserSessPositionFromUserId(uId);
		global.users[userSessPosition].currentQuestionId++;

		res.jsonp({
			userId: uId,
			question: {
				questionId: 0,
				question: questions[0].question,
				answers: questions[0].answers
			}
		});
	} else if(req.path === "/EvalTool/submit") {
		var userId = req.query.userId;
		var userSessPosition = getUserSessPositionFromUserId(userId);
		var userAnswers = global.users[userSessPosition].answers;
		var correctAnswers = 0;

		for(var i = 0; i < questions.length; i++) {
			for(var j = 0; j < userAnswers.length; j++) {
				if(i == userAnswers[j].questionId) {
					if(questions[i].correctAnswer == userAnswers[j].answerId) {
						correctAnswers++;
						break;
					}
				}
			}
		}

		res.jsonp({
			correctAnswers: correctAnswers
		});
	} else {
		var fileName = root + req.path;
		res.sendFile(fileName, function (err) {
		    if (err) {
		      console.log(err);
		      res.status(err.status).end();
		    } else {
		      console.log('Sent:', req.path);
		    }
		});
	}
}

function generateNewUserId() {
	var uId = util.generateRandomString(config.sessionIdSize);

	for(var i = 0; i < global.users.length; i++) {
		if(global.users[i].id == uId) {
			return generateNewUserId();
		}
	}

	return uId;
}

function getUserSessPositionFromUserId(userId) {
	for(var i = 0; i < global.users.length; i++) {
		if(global.users[i].id == userId) return i;
	}
	return null;
}

function posttool(req, res) {
	if (req.path==="/EvalTool/sendmail") {
		sendmail(req, res);
	} else if (req.path === "/EvalTool/start") {
		res.sendFile('/EvalTool/evaluation.html', {root: root });
	}
}

function validateAnswer(userSessPosition, req, res) {
	var questionId = req.query.questionId;
	var userAnswerId = req.query.answer;

	if(typeof(questionId) !== 'undefined' && typeof(userAnswerId) !== 'undefined' && userAnswerId !== 'NaN') {
		for(var i = 0; i < global.users[userSessPosition].answers.length; i++) {
			if(global.users[userSessPosition].answers[i].questionId == questionId) {
				global.users[userSessPosition].answers[i].answerId = userAnswerId;
				return;
			}
		}

		global.users[userSessPosition].answers.push({
			questionId: parseInt(questionId),
			answerId: parseInt(userAnswerId)
		});
	}
}

function sendQuestion(user, req, res) {
	var questionId = user.currentQuestionId;

	if(questionId > questions.length - 1 || questionId < 0) return;

	var userQuestionAnswer = -1;

	for(var i = 0; i < user.answers.length; i++) {
		if(user.answers[i].questionId == questionId) {
			userQuestionAnswer = user.answers[i].answerId;
			break;
		}
	}

	res.jsonp({
		question: {
			questionId: questionId,
			question: questions[questionId].question,
			answers: questions[questionId].answers,
			userAnswerId: userQuestionAnswer
		}
	});
}

function sendmail(req, res) {
	var mymail = {};
	mymail['from']=req.body.fname+"<"+req.body.femail+">"; // sender address
	mymail['to']=req.body.tname+"<"+req.body.temail+">"; // comma separated list of receivers
	mymail['subject']=req.body.subject;
	mymail['text']=req.body.message // plaintext body
	smtpTransport.sendMail(mymail, function(error, info){
	   if(error){
		   console.log(error);
	   }else{
		   console.log("Message sent: " + info.response);
	   }
	});
	var msg = '<p>You sent the following message: </p>'+
	'<p>'+mymail.from+'</p>'+
	'<p>'+mymail.to+'</p>'+
	'<p>'+mymail.subject+'</p>'+
	'<p>'+mymail.text+'</p>';
	res.send(msg);
}

exports.gettool = gettool;
exports.posttool = posttool;
