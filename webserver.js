
var express = require('express');
var bodyParser  = require('body-parser');
var util = require('./util/util');
var config = require('./config').config;
var multer = require('multer');
var request = require('request');
var root = __dirname;

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	extended: true
}));

app.use(multer({
    dest: "./uploads/"
}));

app.set("jsonp callback", true);

global.users = [];

app.get('/', function (req, res) {
	util.sendFile(root + "/home.html", req, res);
});

app.get('/Schedule/*', function (req, res) {
	util.sendFile(root + req.path, req, res);
});

app.get('/homec.html', function (req, res) {
	util.sendFile(root + "/homec.html", req, res);
});

app.get('/ThreeRegion/*', function (req, res) {
	util.sendFile(root + req.path, req, res);
});

app.get('/NavigationBar/*', function (req, res) {
	util.sendFile(root + req.path, req, res);
});


app.get('/EvalTool/*', require("./EvalTool/evaluator").gettool);
app.post('/EvalTool/*', require("./EvalTool/evaluator").posttool);

app.get('/Services/AutoSpeller/*', function (req, res) {
        util.sendFile(root + req.path, req, res);
});

app.get('/Services/AmazonBook/*', function (req, res) {
        util.sendFile(root + req.path, req, res);
});

app.post('/Services/AmazonBook/*', require("./Services/AmazonBook/amazon_api").posttool);

app.get('/Services/roster.html', function(req, res) {
	util.sendFile(root + req.path, req, res);
});

app.get('/Services/WebRoster/roster', function(req, res) {
	res.redirect("http://localhost:8080/WebRoster/roster.jsp");
});

app.get('/Services/RosterMVC', function(req, res) {
	res.redirect("http://localhost:8080/RosterMVC/roster.jsp");
});

app.get('/Services/WebRosterJTable', function(req, res) {
	res.redirect("http://localhost:8080/WebRosterJTable/index.html");
});

app.get('/Services/WebRosterRS', function(req, res) {
	res.redirect("http://localhost:8080/RosterRS/roster.jsp");
});

app.get('/Services/WebRosterRestlet', function(req, res) {
	res.redirect("http://localhost:8282/WebRosterRestlet/roster.jsp");
});

app.get('/Services/iGrade', function(req, res) {
	res.redirect("http://localhost:8080/GradeAgents/index.html");
});

app.get('/Services/SpellChecker2', function(req, res) {
	res.redirect("http://localhost:8080/SpellCheckService/index.html");
});

app.get('/CloudChat/*', function(req, res) {
	util.sendFile(root + req.path, req, res);
});

app.post('/upload', function(req, res) {
    var serverPath = req.files.userUpload.name;
		console.log(serverPath);

		var fullPath = config.GLASS_FISH_DIR + serverPath;

		var serverPath = req.files.userUpload.name;
		console.log(serverPath);

    require('fs').rename(
			req.files.userUpload.path,
			fullPath,
			function(error) {
				if(error) {
					res.json({
						error: 'Something went wrong!'
					});
					return;
				}

				console.log("Attempting to import");

				request.post("http://127.0.0.1:8080/CloudChatDAO", {
						form: {
							action: 'import',
							path: serverPath
						}
					},
					function (error, response, body) {
							if (!error && response.statusCode == 200 && body != "-1") {
								console.log("Successfully imported!");
							}
					});
			});
});

app.listen(config.port, function() {
	console.log('Server running at http://127.0.0.1:' + config.port + '/');
});
