var soap = require('soap');
  var url = 'http://localhost:8282/iGrade?wsdl';
  var args = {
        "tns:arg0": "7",
        "tns:arg1": "7"
  };
  soap.createClient(url, function(err, client) {
      client.toHundredScale(args, function(err, result) {
          console.log(result);
      });
  });

/*
var soap = require('soap');
  var url = 'http://localhost:8282/CloudChatUserManager?wsdl';
  var args = {
	'username': 'test', 
	'password': 'test'
  };
  soap.createClient(url, function(err, client) {
      client.userLogin(args, function(err, result) {
          console.log(result);
      });
  });
*/
