/**
* Generates a random string
* @var strLen Length of random string
*/
exports.generateRandomString = function(strLen) {
	var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for(var i = 0; i < strLen; i++)
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
};

/**
* Takes an array and shuffles the order of the elements
*/
exports.shuffleArray = function(array) {
    for (var i = array.length - 1; i > 0; i--) {
        var j = Math.floor(Math.random() * (i + 1));
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    return array;
}