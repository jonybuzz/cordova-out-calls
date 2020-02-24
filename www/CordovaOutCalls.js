var exec = require('cordova/exec');

exports.callNumber = function(to, chooserTitle, success, error) {
    exec(success, error, "CordovaOutCalls", "callNumber", [to, chooserTitle]);
};

exports.on = function(e, f) {
    var success = function(message) {
      f(message);
    };
    var error = function() {
    };
    exec(success, error, "CordovaOutCalls", "registerEvent", [e]);
};
