var aws = require("aws-lib"); 
var ACCESS_ID = "AKIAJ4NAS6CJ4BJ54YEQ"; 
var SECRET_KEY = "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"; 
var ASSOCIATE_TAG = "kalin"; 
prodAdv = aws.createProdAdvClient(ACCESS_ID, SECRET_KEY, ASSOCIATE_TAG); 

prodAdv.call("ItemLookup", { SearchIndex: "All", IdType: "ISBN", ItemId: "076243631X" }, function(err, result) {
	console.log(JSON.stringify(result));
});
