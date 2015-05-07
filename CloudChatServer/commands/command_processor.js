var commands = require('./commands');

/**
 * Validates whether or a message is or isn't a command
 * and executes the command if it exists in the command
 * table
 */
exports.process = function(io, socket, message) {
  if(message[0] !== '/')
    return false;

  var argsBeginning = message.indexOf(' ');
  var command;

  if(argsBeginning === -1) {
    command = message.substr(1, message.length - 1);
  } else {
    command = message.substr(1, message.indexOf(' ') - 1);
  }

  var exeCmd = commands[command].command;

  if(typeof(exeCmd) !== 'function')
    return false;

  var remainingMsg = message.substr(message.indexOf(' ') + 1, message.length - 1);
  exeCmd(io, socket, remainingMsg);

  return true;
};
