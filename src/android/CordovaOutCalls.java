package com.buzzit.cordovaoutcalls;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.Manifest;
import org.json.JSONArray;
import org.json.JSONException;

public class CordovaOutCalls extends CordovaPlugin {

    public static final int REAL_PHONE_CALL = 1;
    private CallbackContext callbackContext;
    private String realCallTo;
    private String chooserTitle;
    private String appPackage;
    private static CordovaInterface cordovaInterface;

    public static CordovaInterface getCordova() {
        return cordovaInterface;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        cordovaInterface = cordova;
        super.initialize(cordova, webView);
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("callNumber")) {
            realCallTo = args.getString(0);
            if (args.length() > 1) {
                chooserTitle = args.getString(1);
            }
            if (args.length() > 2) {
                appPackage = args.getString(2);
            }
            if (realCallTo != null) {
                cordova.getThreadPool().execute(() -> callNumberPhonePermission());
                this.callbackContext.success("Call Successful");
            } else {
                this.callbackContext.error("Call Failed. You need to enter a phone number.");
            }
            return true;
        }
        return false;
    }

    protected void callNumberPhonePermission() {
        if (!cordova.hasPermission(Manifest.permission.CALL_PHONE)) {
            cordova.requestPermission(this, REAL_PHONE_CALL, Manifest.permission.CALL_PHONE);
        } else {
            callNumber();
        }
    }

    private void callNumber() {
        try {
            if (appPackage != null) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", realCallTo, null));
                intent.setPackage(appPackage);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                this.cordova.getActivity().getApplicationContext().startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", realCallTo, null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                Intent chooser = Intent.createChooser(intent, chooserTitle);
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                this.cordova.getActivity().getApplicationContext().startActivity(chooser);
            }

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                Intent intent1 = new Intent(CordovaOutCalls.getCordova().getActivity().getApplicationContext(), CordovaOutCalls.getCordova().getActivity().getClass());
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                CordovaOutCalls.getCordova().getActivity().getApplicationContext().startActivity(intent1);
            }, 4000);
            this.callbackContext.success("Outgoing call successful");
            this.callbackContext.success("Call Successful");
        } catch (Exception e) {
            this.callbackContext.error("Call Failed: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "CALL_PHONE Permission Denied"));
                return;
            }
        }
        switch (requestCode) {
            case REAL_PHONE_CALL:
                this.callNumber();
                break;
        }
    }
}
