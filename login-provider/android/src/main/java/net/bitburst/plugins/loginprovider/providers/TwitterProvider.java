package net.bitburst.plugins.loginprovider.providers;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
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
import net.bitburst.plugins.loginprovider.LoginProviderHelper;
import net.bitburst.plugins.loginprovider.LoginProviderPlugin;

public class TwitterProvider extends AppCompatActivity {

    public TwitterAuthClient mAuthClient;

    private TwitterAuthConfig mAuthConfig;
    private TwitterConfig mConfig;
    private LoginProviderHelper helper;

    public Plugin pluginImplementation;

    private final String mTwitterApiKey;
    private final String mTwitterSecreteKey;

    public TwitterProvider(Plugin loginProviderPlugin, @NonNull JSObject config) {
        pluginImplementation = loginProviderPlugin;
        mTwitterApiKey = config.getString("consumerKey");
        mTwitterSecreteKey = config.getString("consumerSecret");
        mAuthConfig = new TwitterAuthConfig(mTwitterApiKey, mTwitterSecreteKey);
        mConfig =
            new TwitterConfig.Builder(pluginImplementation.getContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(mAuthConfig)
                .debug(true)
                .build();

        Twitter.initialize(mConfig);
        mAuthClient = new TwitterAuthClient();
    }

    public void login(PluginCall call) {
        if (getTwitterSession() != null) {
            TwitterSession session = getTwitterSession();
            JSObject data = LoginProviderHelper.createLoginProviderResponsePayload(
                "TWITTER",
                session.getAuthToken().token,
                session.getAuthToken().secret,
                null,
                null,
                call.getData().getString("inviteCode")
            );
            call.resolve(data);
            return;
        }

        mAuthClient.authorize(
            pluginImplementation.getActivity(),
            new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession session = getTwitterSession();
                    JSObject data = LoginProviderHelper.createLoginProviderResponsePayload(
                        "TWITTER",
                        session.getAuthToken().token,
                        session.getAuthToken().secret,
                        null,
                        null,
                        call.getData().getString("inviteCode")
                    );
                    call.resolve(data);
                }

                @Override
                public void failure(TwitterException exception) {
                    call.reject(LoginProviderPlugin.LOG_TAG, "twitter was not able to authorize correctly");
                }
            }
        );
    }

    public void logout(PluginCall call) {
        mAuthClient.cancelAuthorize();
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

            ret.put("loggedIn", true);
            ret.put("token", token);
            ret.put("secret", secret);
        } else {
            ret.put("loggedIn", false);
        }

        call.resolve(ret);
    }

    public void handleLoginResult(int requestCode, int resultCode, Intent data) {
        if (mAuthClient != null) mAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private TwitterSession getTwitterSession() {
        return TwitterCore.getInstance().getSessionManager().getActiveSession();
    }
}
