package net.bitburst.plugins.twitterlogin;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import  com.twitter.sdk.android.core.Twitter;

public class TwitterInstance extends Application {

    public TwitterAuthClient authClient;

    TwitterInstance(TwitterLoginPlugin plugin) {
        String consumerKey = plugin.getConfig().getString("consumerKey");
        String consumerSecret = plugin.getConfig().getString("consumerSecret");
        TwitterConfig config = new TwitterConfig.Builder(plugin.getActivity())
            .logger(new DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
            .debug(true)
            .build();

        Twitter.initialize(config);
        this.authClient = new TwitterAuthClient();
    }
}