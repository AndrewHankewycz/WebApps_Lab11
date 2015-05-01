var soap = require('soap');
  var url = 'http://localhost:8282/CloudChatUserManager?wsdl';
  var args = {
        arg0 : "austin",
        arg1 : "test"
  };
  soap.createClient(url, function(err, client) {
      client.userLogin(args, function(err, result) {
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
