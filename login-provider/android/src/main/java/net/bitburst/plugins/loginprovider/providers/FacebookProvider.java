package net.bitburst.plugins.loginprovider.providers;

import android.content.Intent;
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

import net.bitburst.plugins.loginprovider.LoginProviderHelper;
import net.bitburst.plugins.loginprovider.LoginProviderPlugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

public class FacebookProvider {

    CallbackManager callbackManager;

    public static final int RC_FACEBOOK_AUTH = 0xface;
    public static final int FACEBOOK_SDK_REQUEST_CODE_OFFSET = 0xface;
    public static final String ERROR_SIGN_IN_CANCELED = "Sign in canceled.";

    private LoginProviderPlugin pluginImplementation;
    private JSObject configSettings;
    private CallbackManager mCallbackManager;
    private String callbackId;
    private static final String EMAIL = "email";

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
                            PluginCall savedCall = pluginImplementation.getBridge().getSavedCall(callbackId);
                            if (savedCall == null) {
                                Log.e(LoginProviderPlugin.LOG_TAG, "LoginManager.onSuccess: no plugin saved call found.");
                            } else {
                                JSObject ret = new JSObject();
                                JSObject data = LoginProviderHelper.createLoginProviderResponsePayload("FACEBOOK", loginResult.getAccessToken().getToken(), null, null, null, savedCall.getData().getString("inviteCode"));
                                savedCall.resolve(data);

                                pluginImplementation.getBridge().saveCall(null);
                            }
                        }

                        @Override
                        public void onCancel() {
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
        callbackManager.onActivityResult(FACEBOOK_SDK_REQUEST_CODE_OFFSET, result.getResultCode(), result.getData());
        call.resolve();
    }

    public void login(PluginCall call) {
        if(call == null) return;
        callbackId = call.getCallbackId();

        rejectSavedCalls(call);

        JSArray permissionArray = new JSArray();
        permissionArray.put(EMAIL);

        Collection<String> permissions;
        try {
            permissions = permissionArray.toList();
        } catch (Exception e) {
            call.reject("Invalid permissions argument");
            return;
        }

        pluginImplementation.getBridge().saveCall(call);
        //LoginManager.getInstance().logIn(pluginImplementation.getActivity(), permissions);


        LoginManager.FacebookLoginActivityResultContract contract = LoginManager.getInstance().createLogInActivityResultContract(callbackManager);
        Intent loginIntent = contract.createIntent(pluginImplementation.getActivity(), permissions);
        pluginImplementation.startActivityForResult(call, loginIntent, "facebookLoginResult");
    }

    public void logout(PluginCall call) {
        LoginManager.getInstance().logOut();
        call.resolve();
    }

    public void reauthorize(PluginCall call) {
        rejectSavedCalls(call);

        LoginManager.getInstance().reauthorizeDataAccess(pluginImplementation.getActivity());
        pluginImplementation.getBridge().saveCall(call);
    }

    public void getCurrentAccessToken(PluginCall call) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        JSObject ret = new JSObject();

        if (accessToken == null) {
            Log.d(LoginProviderPlugin.LOG_TAG, "getCurrentAccessToken: accessToken is null");
        } else {
            Log.d(LoginProviderPlugin.LOG_TAG, "getCurrentAccessToken: accessToken found");
            ret.put("accessToken", accessToken.getToken());
        }

        call.resolve(ret);
    }

    public void getProfilePicture(PluginCall call) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    FacebookRequestError requestError = response.getError();
                    if (requestError != null) {
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
        graphRequest.executeAsync();
    }

    private void rejectSavedCalls(PluginCall call) {
        String callbackId = call.getCallbackId();
        if(callbackId != null && pluginImplementation.getBridge().getSavedCall(callbackId) != null) {
            call.reject("Overlapped calls call not supported");
            return;
        };
    };
}
