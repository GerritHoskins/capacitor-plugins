package net.bitburst.plugins.loginprovider.handlers;

import static net.bitburst.plugins.loginprovider.LoginProviderPlugin.LOG_TAG;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitterAuthProviderHandler extends Application {

    public TwitterAuthClient authClient;
    public TwitterConfig config;
    private String consumerKey = "";
    private String consumerSecret = "";

    public Activity activity;
    public Context context;

    public TwitterAuthProviderHandler(Context context, Activity activity, PluginConfig pluginConfig) {
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

    public void login(TwitterAuthProviderHandler twitterAuthProviderHandler, PluginCall call) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(
            () -> {
                try {
                    twitterAuthProviderHandler.authClient.authorize(
                        activity,
                        new Callback<TwitterSession>() {
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
                                call.reject(LOG_TAG + "login error", exception);
                            }
                        }
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                    call.reject(LOG_TAG + "error retrieving access token", exception);
                }
            }
        );
    }

    public void logout(TwitterAuthProviderHandler twitterAuthProviderHandler, PluginCall call) {
        twitterAuthProviderHandler.authClient.cancelAuthorize();
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
        if (result.getResultCode() == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            authClient.onActivityResult(TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE, result.getResultCode(), result.getData());
            call.resolve();
        } else {
            call.reject("fail!");
        }
    }
}
