package net.bitburst.plugins.twitterlogin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.getcapacitor.PluginConfig;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class TwitterLogin extends Application {

    public TwitterAuthClient authClient;
    public TwitterConfig config;
    private String consumerKey = "";
    private String consumerSecret = "";

    public Activity activity;
    public Context context;

    TwitterLogin(TwitterLoginPlugin plugin) {
        this.consumerKey = plugin.getConfig().getString("consumerKey");
        this.consumerSecret = plugin.getConfig().getString("consumerSecret");
        this.context = plugin.getContext();
        this.activity = plugin.getActivity();
        this.config =
            new TwitterConfig.Builder(plugin.getActivity())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                .debug(true)
                .build();

        Twitter.initialize(config);
        this.authClient = new TwitterAuthClient();
    }
}
