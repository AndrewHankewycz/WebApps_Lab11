var aws = require("aws-lib");

var ACCESS_ID = "AKIAJ4NAS6CJ4BJ54YEQ";
var SECRET_KEY = "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";
var ASSOCIATE_TAG = "kalin";

var root = process.cwd();

function posttool(req, res) {
	if (req.path==="/Services/AmazonBook/api") {
		amazonRequest(req, res);
	} else {
		res.sendFile('/Services/AmazonBook/index.html', {root: root });
	}
}

function sendItemRes(res, type, result) {
        res.json({
                type: type,
        	data: result.Items.Item,
	});
}

function amazonRequest(req, res) {
	var isbn = req.body.isbn;
	var itemId = req.body.itemId;
	var author = req.body.author;

	if(!isbn && !itemId && !author) {
		res.json({ 
			data: { 
				error: "Error: Please enter at least one field!" 
			}
		});
	}

	prodAdv = aws.createProdAdvClient(ACCESS_ID, SECRET_KEY, ASSOCIATE_TAG);
	
	if(isbn) {
		prodAdv.call("ItemLookup", { SearchIndex: "All", IdType: "ISBN", ItemId: isbn }, function(err, result) {
			sendItemRes(res, "isbn", result);
		});
	} else if(itemId) {
		prodAdv.call("ItemLookup", { ItemId: itemId }, function(err, result) {
			sendItemRes(res, "item", result);
		});
	} else if(author) {
		prodAdv.call("ItemSearch", { SearchIndex: "Books", Author: author }, function(err, result) {
			sendItemRes(res, "book", result);
		});
	}
}

exports.posttool = posttool;
