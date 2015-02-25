var util = require('../util/util');
var config = require('../config').config;
var root = process.cwd();

function gettool(req, res) {
	if (req.path === "/WebServices/roster") {
		res.redirect("http://localhost:8080/WebRoster/roster.jsp");
	}
}

function posttool(req, res) {}

exports.gettool = gettool;
exports.posttool = posttool;
