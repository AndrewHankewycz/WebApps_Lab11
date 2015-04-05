
var express = require('express');
var bodyParser  = require('body-parser');
var util = require('./util/util');
var config = require('./config').config;
var root = __dirname;

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	extended: true
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

app.get('/CloudChat/*', function(req, res) {
	util.sendFile(root + req.path, req, res);
});

app.listen(config.port, function() {
	console.log('Server running at http://127.0.0.1:' + config.port + '/');
});
