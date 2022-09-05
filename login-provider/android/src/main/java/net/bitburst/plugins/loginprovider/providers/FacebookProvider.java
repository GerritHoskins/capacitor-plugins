package net.bitburst.plugins.loginprovider.providers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import net.bitburst.plugins.loginprovider.LoginProviderPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class FacebookProvider {

    CallbackManager callbackManager;

    public static final int RC_FACEBOOK_AUTH = 0xface;
    public static final int FACEBOOK_SDK_REQUEST_CODE_OFFSET = 0xface;
    public static final String ERROR_SIGN_IN_CANCELED = "Sign in canceled.";

    private LoginProviderPlugin pluginImplementation;
    private JSObject configSettings;
    private CallbackManager mCallbackManager;
    private String callbackId;

    @Nullable
    private PluginCall savedCall;

    public FacebookProvider(LoginProviderPlugin loginProviderPlugin, JSObject config) {
        pluginImplementation = loginProviderPlugin;
        configSettings = config;

        try {
            this.callbackManager = CallbackManager.Factory.create();

            LoginManager
                .getInstance()
                .registerCallback(
                    callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d(LoginProviderPlugin.LOG_TAG, "LoginManager.onSuccess");

                            PluginCall savedCall = pluginImplementation.getBridge().getSavedCall(callbackId);

                            if (savedCall == null) {
                                Log.e(LoginProviderPlugin.LOG_TAG, "LoginManager.onSuccess: no plugin saved call found.");
                            } else {
                                JSObject ret = new JSObject();
                                ret.put("accessToken", accessTokenToJson(loginResult.getAccessToken()));
                                ret.put("recentlyGrantedPermissions", collectionToJson(loginResult.getRecentlyGrantedPermissions()));
                                ret.put("recentlyDeniedPermissions", collectionToJson(loginResult.getRecentlyDeniedPermissions()));

                                savedCall.resolve(ret);

                                pluginImplementation.getBridge().saveCall(null);
                            }
                        }

                        @Override
                        public void onCancel() {
                            Log.d(LoginProviderPlugin.LOG_TAG, "LoginManager.onCancel");

                            PluginCall savedCall = pluginImplementation.getBridge().getSavedCall(callbackId);

                            if (savedCall == null) {
                                Log.e(LoginProviderPlugin.LOG_TAG, "LoginManager.onCancel: no plugin saved call found.");
                            } else {
                                JSObject ret = new JSObject();
                                ret.put("accessToken", null);

                                savedCall.resolve(ret);

                                pluginImplementation.getBridge().saveCall(null);
                            }
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e(LoginProviderPlugin.LOG_TAG, "LoginManager.onError", exception);

                            PluginCall savedCall = pluginImplementation.getBridge().getSavedCall(callbackId);

                            if (savedCall == null) {
                                Log.e(LoginProviderPlugin.LOG_TAG, "LoginManager.onError: no plugin saved call found.");
                            } else {
                                savedCall.reject(exception.toString());

                                pluginImplementation.getBridge().saveCall(null);
                            }
                        }
                    }
                );
        } catch (Exception exception) {
            Log.e(LoginProviderPlugin.LOG_TAG, "initialization failed.", exception);
        }
    }

    public void handleFacebookLoginResult(PluginCall call, ActivityResult result) {
        if (call == null) return;

        if (result.getResultCode() == FACEBOOK_SDK_REQUEST_CODE_OFFSET) {
            callbackManager.onActivityResult(FACEBOOK_SDK_REQUEST_CODE_OFFSET, result.getResultCode(), result.getData());
            call.resolve();
        } else {
            call.reject("fail!");
        }
    }

    public void login(PluginCall call, Activity activity) {
        Log.d(LoginProviderPlugin.LOG_TAG, "Entering login()");


        Intent loginIntent = activity.getIntent();
        //Activity activity = pluginImplementation.activity;

        callbackId = call.getCallbackId();
        PluginCall savedCall = pluginImplementation.getBridge().getSavedCall(callbackId);

        if (savedCall != null) {
            Log.e(LoginProviderPlugin.LOG_TAG, "login: overlapped calls not supported");

            call.reject("Overlapped calls call not supported");

            return;
        }

        JSONArray arg = null;
        try {
            arg = call.getObject("loginOptions").getJSONArray("permissions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSArray permissionArray =  new JSArray();

        for (int i = 0; i < arg.length(); i++) {
            try {
                permissionArray.put(i, arg.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collection<String> permissions;

        try {
            permissions = permissionArray.toList();
        } catch (Exception e) {
            Log.e(LoginProviderPlugin.LOG_TAG, "login: invalid 'permissions' argument", e);

            call.reject("Invalid permissions argument");

            return;
        }

        pluginImplementation.getBridge().saveCall(call);

        pluginImplementation.startActivityForResult(call, loginIntent, "facebookLoginResult");
        LoginManager.getInstance().logIn(activity, permissions);
    }

    public void logout(PluginCall call) {
        Log.d(LoginProviderPlugin.LOG_TAG, "Entering logout()");

        LoginManager.getInstance().logOut();

        call.resolve();
    }

    public void reauthorize(PluginCall call) {
        Log.d(LoginProviderPlugin.LOG_TAG, "Entering reauthorize()");

        Activity activity = pluginImplementation.getActivity();
        callbackId = call.getCallbackId();
        PluginCall savedCall = pluginImplementation.getBridge().getSavedCall(callbackId);


        if (savedCall != null) {
            Log.e(LoginProviderPlugin.LOG_TAG, "reauthorize: overlapped calls not supported");

            call.reject("Overlapped calls call not supported");

            return;
        }

        LoginManager.getInstance().reauthorizeDataAccess(activity);

        pluginImplementation.getBridge().saveCall(call);
    }

    public void getCurrentAccessToken(PluginCall call) {
        Log.d(LoginProviderPlugin.LOG_TAG, "Entering getCurrentAccessToken()");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        JSObject ret = new JSObject();

        if (accessToken == null) {
            Log.d(LoginProviderPlugin.LOG_TAG, "getCurrentAccessToken: accessToken is null");
        } else {
            Log.d(LoginProviderPlugin.LOG_TAG, "getCurrentAccessToken: accessToken found");

            ret.put("accessToken", accessTokenToJson(accessToken));
        }

        call.resolve(ret);
    }

    public void getProfile(final PluginCall call) {
        Log.d(LoginProviderPlugin.LOG_TAG, "Entering getProfile()");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null) {
            Log.d(LoginProviderPlugin.LOG_TAG, "getProfile: accessToken is null");
            call.reject("You're not logged in. Call FacebookLogin.login() first to obtain an access token.");

            return;
        }

        if (accessToken.isExpired()) {
            Log.d(LoginProviderPlugin.LOG_TAG, "getProfile: accessToken is expired");
            call.reject("AccessToken is expired.");

            return;
        }

        Bundle parameters = new Bundle();

        try {
            JSArray fields = call.getArray("fields");
            String fieldsString = TextUtils.join(",", fields.toList());

            parameters.putString("fields", fieldsString);
        } catch (JSONException e) {
            call.reject("Can't handle fields", e);

            return;
        }

        GraphRequest graphRequest = GraphRequest.newMeRequest(
            accessToken,
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    FacebookRequestError requestError = response.getError();

                    if (requestError != null) {
                        call.reject(requestError.getErrorMessage());

                        return;
                    }

                    try {
                        JSONObject jsonObject = response.getJSONObject();
                        JSObject jsObject = JSObject.fromJSONObject(jsonObject);

                        call.resolve(jsObject);
                    } catch (JSONException e) {
                        call.reject("Can't create response", e);
                    }
                }
            }
        );

        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    /**
     * Convert date to ISO 8601 format.
     */
    private String dateToJson(Date date) {
        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");

        return simpleDateFormat.format(date);
    }

    private JSArray collectionToJson(Collection<String> list) {
        JSArray json = new JSArray();

        for (String item : list) {
            json.put(item);
        }

        return json;
    }

    private JSObject accessTokenToJson(AccessToken accessToken) {
        JSObject ret = new JSObject();
        ret.put("applicationId", accessToken.getApplicationId());
        ret.put("declinedPermissions", collectionToJson(accessToken.getDeclinedPermissions()));
        ret.put("expires", dateToJson(accessToken.getExpires()));
        ret.put("lastRefresh", dateToJson(accessToken.getLastRefresh()));
        ret.put("permissions", collectionToJson(accessToken.getPermissions()));
        ret.put("token", accessToken.getToken());
        ret.put("userId", accessToken.getUserId());
        ret.put("isExpired", accessToken.isExpired());

        return ret;
    }
}
