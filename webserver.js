
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
	res.sendFile(root + "/home.html", function (err) {
		if (err) {
			console.log(err);
			res.status(err.status).end();
		}
		else {
			console.log('Sent:', req.path);
		}
	});
});

app.get('/Schedule/*', function (req, res) {
	var fileName = root + req.path;
	res.sendFile(fileName, function (err) {
		if (err) {
			console.log(err);
			res.status(err.status).end();
		}
		else {
			console.log('Sent:', req.path);
		}
	});
});

app.get('/homec.html', function (req, res) {
	res.sendFile(root + "/homec.html", function (err) {
		if (err) {
			console.log(err);
			res.status(err.status).end();
		}
		else {
			console.log('Sent:', req.path);
		}
	});
});

app.get('/ThreeRegion/*', threeregion);
function threeregion(req, res) {
	var fileName = root +req.path;
	  res.sendFile(fileName, function (err) {
	    if (err) {
	      console.log(err);
	      res.status(err.status).end();
	    }
	    else {
	      console.log('Sent:', req.path);
	    }
	});
}

app.get('/EvalTool/*', require("./EvalTool/evaluator").gettool);
app.post('/EvalTool/*', require("./EvalTool/evaluator").posttool);

app.listen(config.port, function() {
	console.log('Server running at http://127.0.0.1:' + config.port + '/');
});
