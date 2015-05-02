var request = require('request'),
  config = require('../config'),
  RoomHelper = require('../util/room_helper');

/**
 * Command name mapped to the command function
 */
module.exports = {
  'join' : function(io, socket, args) {
    request.post(config.DAO_URL, {
        form: {
          action: 'searchRoom',
          topic: args
        }
      },
      function (error, response, body) {
          if (!error && response.statusCode == 200 && body != "-1") {
              RoomHelper.addUserToRoom(parseInt(body))
          }
      });
  }
};
