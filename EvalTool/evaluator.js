var nodemailer = require('nodemailer');
var User = require('../user');
var util = require('../util/util');
var questions = require('./questions.js').questions;
var smtpTransport = nodemailer.createTransport();
//Get base path of project
var root = process.env.PWD;
var curNum = 1;

function gettool(req, res) {
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

function posttool(req, res) {
	if (req.path==="/EvalTool/sendmail") {
		sendmail(req, res);
	} else if (req.path === "/EvalTool/start") {
		if(!req.session.user) {
			req.session.user = new User(req.sessionID);
		}

		res.sendFile('/EvalTool/evaluation.html', {root: root });
	} else if (req.path === "/EvalTool/testing") {
		if(req.session.user) {
			validateAnswer(req, res);
		} else {
			//Restart the quiz, the user is not logged in.
			res.sendFile('/EvalTool/evaluation.html', {root: root });
		}
	} else if(req.path === "/EvalTool/questions") {
		sendQuestions(req, res);
	} else if(req.path === "/EvalTool/submit") {
		var correctAnswers = 0;

		for(var i = 0; i < questions.length; i++) {
			for(var j = 0; j < req.session.user.answers.length; j++) {
				if(parseInt(req.session.user.answers[j].questionId) == i) {
					if(req.session.user.answers[j].answerId == questions[i].correctAnswer) {
						correctAnswers++;
					}
				}
			}
		}

		res.json({
			correctAnswers: correctAnswers
		});
	}
}

function validateAnswer(req, res) {
	var questionId = req.body.questionId;
	var userAnswer = req.body.answer;

	if(typeof(questionId) !== 'undefined' && typeof(userAnswer) !== 'undefined') {
		var serverQuestion = questions[questionId];

		for(var i = 0; i < serverQuestion.answers.length; i++) {
			if(serverQuestion.answers[i] == userAnswer) {

				//Check if the question has already been answered
				var index = req.session.user.answers.map(function(e) {
					return e.questionId;
				}).indexOf(questionId);

				//If the question has not been answered, add it
				if(index == -1) {
					req.session.user.answers.push({
						questionId: parseInt(questionId),
						answerId: i
					});
				} else {
					//If the question has already been answered, update the answer
					req.session.user.answers[index].answerId = i;
				}

				break;
			}
		}
	}

	res.json({
		done: true
	});
}

function sendQuestions(req, res) {
	res.contentType('application/json');
	res.setHeader("Access-Control-Allow-Origin", "*");

	//Remove the correct answer property when sending
	//to the client. Additionally, shuffle the answers
	//so they're never in a consistent order.
	var questionsWithoutAnswers = [];
	for(var i = 0; i < questions.length; i++) {
		questionsWithoutAnswers.push({
			question: questions[i].question,
			answers: questions[i].answers
			//util.shuffleArray(questions[i].answers)
		});
	}

	res.json({
		questions: questionsWithoutAnswers
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
