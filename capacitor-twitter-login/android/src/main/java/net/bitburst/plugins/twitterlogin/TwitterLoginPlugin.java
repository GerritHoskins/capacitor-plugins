package net.bitburst.plugins.twitterlogin;

import android.content.Intent;
import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

@CapacitorPlugin(name = "TwitterPlugin", requestCodes = { TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE })
public class TwitterLoginPlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.twitter ";

    public TwitterInstance twitterInstance;

    @Override
    public void load() {
        getInstance();
        super.load();
    }

    private TwitterInstance getInstance() {
        if (twitterInstance == null) {
            twitterInstance = new TwitterInstance(this);
        }

        return twitterInstance;
    }

    @PluginMethod
    public void login(final PluginCall call) {
        getInstance()
            .authClient.authorize(
                getActivity(),
                new Callback<>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        JSObject ret = new JSObject();
                        ret.put("authToken", result.data.getAuthToken().token);
                        ret.put("authTokenSecret", result.data.getAuthToken().secret);
                        ret.put("userName", result.data.getUserName());
                        ret.put("userID", result.data.getUserId());
                        call.resolve(ret);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d(LOG_TAG, exception.getLocalizedMessage());
                        call.reject(LOG_TAG + "login error ", exception);
                    }
                }
            );
    }

    @PluginMethod
    public void logout(PluginCall call) {
        getInstance().authClient.cancelAuthorize();
        SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
        sessionManager.clearActiveSession();
        call.resolve();
    }

    @PluginMethod
    public void isLogged(PluginCall call) {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        JSObject ret = new JSObject();

        if (session != null) {
            TwitterAuthToken authToken = session.getAuthToken();

            String token = authToken.token;
            String secret = authToken.secret;

            ret.put("in", true);
            ret.put("authToken", token);
            ret.put("authTokenSecret", secret);
        } else {
            ret.put("in", false);
        }

        call.resolve(ret);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 140) {
            getInstance().authClient.onActivityResult(requestCode, resultCode, data);
        } else {
            super.handleOnActivityResult(requestCode, resultCode, data);
        }
    }
}
