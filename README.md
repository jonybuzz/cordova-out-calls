# cordova-out-calls

Cordova Plugin to place outgoing phone calls (only Android).

## Installation

`cordova plugin add cordova-out-calls.git`

## Usage

Letting the user choose app:

```
cordova.plugins.CordovaOutCalls.callNumber({
        to: '111-3333',
        chooserTitle: 'Choose an app to call', //optional
    }, console.log, console.error);
```

Search for specific application to handle call:

```
cordova.plugins.CordovaOutCalls.callNumber({
        to: '111-3333',
        appPackage: 'com.android.server.telecom'
    }, console.log, console.error);
```