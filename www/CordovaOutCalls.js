var exec = require('cordova/exec');

exports.callNumber = function(options, success, error) {
    exec(success, error, "CordovaOutCalls", "callNumber", [options.to, options.chooserTitle, options.appPackage]);
};

exports.on = function(e, f) {
    var success = function(message) {
      f(message);
    };
    var error = function() {
    };
    exec(success, error, "CordovaOutCalls", "registerEvent", [e]);
};
