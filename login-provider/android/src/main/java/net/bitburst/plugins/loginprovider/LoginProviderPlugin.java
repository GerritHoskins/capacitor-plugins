package net.bitburst.plugins.loginprovider;

import android.Manifest;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

//@CapacitorPlugin(name = "LoginProvider", requestCodes = { FacebookProvider.RC_FACEBOOK_AUTH, FacebookProvider.FACEBOOK_SDK_REQUEST_CODE_OFFSET, TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE })
@CapacitorPlugin(name = "LoginProvider", permissions = { @Permission(alias = "internet", strings = { Manifest.permission.INTERNET }) })
public class LoginProviderPlugin extends Plugin {

    public static final String LOG_TAG = "LoginProvider ";
    public static final String AUTH_STATE_CHANGE_EVENT = "authStateChange";

    private PluginConfig config;
    private LoginProvider implementation;
    private String callbackId;

    @Override
    public void load() {
        implementation = new LoginProvider(this, getContext(), getActivity(), getConfig());
        implementation.setAppStatusChangeListener(null);

        super.load();
    }

    @PluginMethod
    public void loginWithProvider(PluginCall call) {
        call.setKeepAlive(true);

        callbackId = call.getCallbackId();
        bridge.saveCall(call);

        try {
            if (getPermissionState("internet") != PermissionState.GRANTED) {
                requestPermissionForAlias("internet", call, "internetPermissionCallback");
            } else {
                implementation.loginWithProvider(call, getActivity());
            }
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @PermissionCallback
    public void internetPermissionCallback(PluginCall call) {
        if (getPermissionState("internet") == PermissionState.GRANTED) {
            try {
                implementation.loginWithProvider(call, getActivity());
            } catch (Exception ex) {
                call.reject(ex.getLocalizedMessage());
            }
        } else {
            call.reject("Permission is required");
        }
    }

    @PluginMethod
    public void loginWithApple(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void loginWithFacebook(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void loginWithGoogle(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void loginWithTwitter(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void logoutFromProvider(PluginCall call) {
        try {
            implementation.logoutFromProvider(call);
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }

        call.resolve();
    }

    @PluginMethod
    public void addListener(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void removeAllListeners(PluginCall call) {
        call.unimplemented();
    }

    @Override
    public void startActivityForResult(PluginCall call, Intent intent, String callbackName) {
        super.startActivityForResult(call, intent, callbackName);
    }

    @ActivityCallback
    protected void loginResult(PluginCall call, ActivityResult result, String provider) {
        switch (provider) {
            case "FACEBOOK":
                implementation.facebookProvider.handleOnActivityResult(call, result);
                break;
            case "GOOGLE":
                implementation.googleProvider.handleOnActivityResult(call, result);
                break;
            case "TWITTER":
                implementation.twitterProvider.handleOnActivityResult(call, result);
                break;
        }
    }

    public void notifyListeners(String eventName, JSObject data) {
        super.notifyListeners(eventName, data);
    }
    /* private void updateAppState() {
       LoginProviderUserModel user = implementation.get();
        JSObject userResult = LoginProviderHelper.createUserResult(user);
        JSObject result = new JSObject();
        result.put("user", userResult);
        notifyListeners(AUTH_STATE_CHANGE_EVENT, result);
    }*/
}
