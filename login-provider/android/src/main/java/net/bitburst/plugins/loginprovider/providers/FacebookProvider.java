package net.bitburst.plugins.loginprovider.providers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResult;
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
import java.util.Collection;
import java.util.Objects;
import net.bitburst.plugins.loginprovider.LoginProviderHelper;
import net.bitburst.plugins.loginprovider.LoginProviderPlugin;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookProvider {

    public static final int FACEBOOK_SDK_REQUEST_CODE_OFFSET = 0xface;
    public static final String ERROR_SIGN_IN_CANCELED = "Sign in canceled.";

    private LoginProviderPlugin pluginImplementation;
    private JSObject configSettings;
    private CallbackManager mCallbackManager;
    private String callbackId;
    private static final String EMAIL = "email";

    public FacebookProvider(LoginProviderPlugin loginProviderPlugin, JSObject config) {
        pluginImplementation = loginProviderPlugin;
        configSettings = config;

        try {
            mCallbackManager = CallbackManager.Factory.create();

            LoginManager
                .getInstance()
                .registerCallback(
                    mCallbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            if (guardSavedCalls() == null) return;
                            GraphRequest request = completeLogin();
                            request.executeAsync();

                            pluginImplementation.getBridge().saveCall(null);
                        }

                        @Override
                        public void onCancel() {
                            guardSavedCalls()
                                .resolve(LoginProviderHelper.createLoginProviderResponsePayload("FACEBOOK", null, null, null, null, null));

                            pluginImplementation.getBridge().saveCall(null);
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            guardSavedCalls().reject(LoginProviderPlugin.LOG_TAG, exception.toString());
                            pluginImplementation.getBridge().saveCall(null);
                        }
                    }
                );
        } catch (Exception exception) {
            Log.e(LoginProviderPlugin.LOG_TAG, "initialization failed.", exception);
        }
    }

    public void handleLoginRequest(PluginCall call, ActivityResult result) {
        if (call == null) return;
        mCallbackManager.onActivityResult(FACEBOOK_SDK_REQUEST_CODE_OFFSET, result.getResultCode(), result.getData());
    }

    public void login(PluginCall call) {
        if (call == null) return;

        callbackId = call.getCallbackId();
        rejectSavedCalls(call);

        JSArray jsArray = new JSArray();
        Collection<String> permissions;
        try {
            LoginProviderHelper.convertStringArray(Objects.requireNonNull(configSettings.getString("permissions")).split(" "));
            permissions = jsArray.toList();
        } catch (JSONException e) {
            call.reject(LoginProviderPlugin.LOG_TAG, "invalid permissions argument", e);
            return;
        }

        call.setKeepAlive(true);

        LoginManager.FacebookLoginActivityResultContract contract = LoginManager
            .getInstance()
            .createLogInActivityResultContract(mCallbackManager);
        Intent loginIntent = contract.createIntent(pluginImplementation.getActivity(), permissions);
        pluginImplementation.startActivityForResult(call, loginIntent, "facebookLoginRequest");
    }

    public GraphRequest completeLogin() {
        GraphRequest request = GraphRequest.newMeRequest(
            getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    FacebookRequestError requestError = response.getError();
                    if (requestError != null) return;

                    JSONObject graphData = response.getJSONObject();
                    try {
                        assert graphData != null;
                        JSONObject picture = graphData.getJSONObject("picture");
                        JSONObject pictureData = picture.getJSONObject("data");
                        String pictureUrl = pictureData.getString("url");

                        PluginCall tempCall = guardSavedCalls();

                        JSObject payload = LoginProviderHelper.createLoginProviderResponsePayload(
                            "FACEBOOK",
                            getCurrentAccessToken().getToken(),
                            null,
                            graphData.getString("email"),
                            Uri.parse(pictureUrl),
                            tempCall.getData().getString("inviteCode")
                        );

                        guardSavedCalls().resolve(payload);
                    } catch (JSONException e) {
                        guardSavedCalls().reject(LoginProviderPlugin.LOG_TAG, "failed to complete login response", e);
                    }
                }
            }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,picture");
        request.setParameters(parameters);

        return request;
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

    public AccessToken getCurrentAccessToken() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Log.d(LoginProviderPlugin.LOG_TAG, "accessToken is null");
        }
        return accessToken;
    }

    private void rejectSavedCalls(PluginCall call) {
        String callbackId = call.getCallbackId();
        if (callbackId != null && guardSavedCalls() != null) {
            call.reject(LoginProviderPlugin.LOG_TAG, "overlapped calls are not supported");
        }
    }

    private PluginCall guardSavedCalls() {
        PluginCall call = pluginImplementation.getBridge().getSavedCall(callbackId);
        if (call == null) {
            Log.d(LoginProviderPlugin.LOG_TAG, "no saved plugin call found.");
        }

        return call;
    }
}
