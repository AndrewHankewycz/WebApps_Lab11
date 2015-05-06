function setChatUsers(room) {
  for(var i = 0; i < room.users.length; i++) {
    var username = room.users[i].username;
    $("#users").append("<p>" + username + "</p>");
  }
}
