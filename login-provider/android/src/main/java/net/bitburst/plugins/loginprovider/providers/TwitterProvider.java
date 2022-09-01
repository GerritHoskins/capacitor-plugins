package net.bitburst.plugins.loginprovider.providers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class TwitterProvider extends Application {

    public TwitterAuthClient authClient;
    public TwitterConfig config;
    private String consumerKey = "";
    private String consumerSecret = "";

    public Activity activity;
    public Context context;

    public TwitterProvider(Context context, Activity activity, PluginConfig pluginConfig) {
        this.consumerKey = pluginConfig.getString("twitter.consumerKey");
        this.consumerSecret = pluginConfig.getString("twitter.consumerSecret");
        this.context = context;
        this.activity = activity;
        this.config =
            new TwitterConfig.Builder(activity)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                .debug(true)
                .build();

        Twitter.initialize(config);
        this.authClient = new TwitterAuthClient();
    }

    public TwitterProvider login(TwitterProvider twitterProvider, PluginCall call, Activity activity) {
        twitterProvider.authClient.authorize(
            activity,
            new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    call.setKeepAlive(true);
                    JSObject ret = new JSObject();
                    ret.put("authToken", result.data.getAuthToken().token);
                    ret.put("authTokenSecret", result.data.getAuthToken().secret);
                    ret.put("userName", result.data.getUserName());
                    ret.put("userID", result.data.getUserId());
                    call.success(ret);
                }

                @Override
                public void failure(TwitterException exception) {
                    call.setKeepAlive(true);
                    Log.d("DEBUG", "OH NO!! THERE WAS AN ERROR");
                    call.error("error", exception);
                }
            }
        );

        return twitterProvider;
    }

    public void logout(TwitterProvider twitterProvider, PluginCall call) {
        twitterProvider.authClient.cancelAuthorize();
        SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
        sessionManager.clearActiveSession();
        call.resolve();
    }

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

    public void handleOnActivityResult(PluginCall call, ActivityResult result) {
        if (call == null) return;
        int resultCode = result.getResultCode();
        Intent data = result.getData();
        authClient.onActivityResult(TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE, resultCode, data);
    }
}
