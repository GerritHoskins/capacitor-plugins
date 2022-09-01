package net.bitburst.plugins.loginprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import java.util.Objects;
import net.bitburst.plugins.loginprovider.providers.AppleProvider;
import net.bitburst.plugins.loginprovider.providers.FacebookProvider;
import net.bitburst.plugins.loginprovider.providers.GoogleProvider;
import net.bitburst.plugins.loginprovider.providers.TwitterProvider;

public class LoginProvider {

    public interface AppStatusChangeListener {
        void onAppStatusChanged(Boolean isActive);
    }

    //public Activity activity;
    public AppCompatActivity activity;
    public Context context;

    private AppStatusChangeListener statusChangeListener;

    private LoginProviderPlugin plugin;
    private PluginConfig config;

    public AppleProvider appleProvider;
    public FacebookProvider facebookProvider;
    public GoogleProvider googleProvider;
    public TwitterProvider twitterProvider;

    public LoginProvider(LoginProviderPlugin plugin, Context context, AppCompatActivity activity, PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.context = context;
        this.activity = activity;
        this.config = pluginConfig;
        this.initAuthProviderHandlers();
    }

    public void loginWithProvider(final PluginCall call, Activity activity) {
        if (call == null) return;
        if (!call.hasOption("provider")) call.reject("provider name missing");

        String providerName = call.getString("provider");
        call.setKeepAlive(true);

        Intent loginIntent = activity.getIntent();

        switch (Objects.requireNonNull(providerName)) {
            case "APPLE":
                //appleProvider.login(call);
                break;
            case "GOOGLE":
                loginIntent = googleProvider.login(call);
                plugin.startActivityForResult(call, loginIntent, "loginResult");
                break;
            case "FACEBOOK":
                plugin.startActivityForResult(call, loginIntent, "loginResult");
                facebookProvider.login(call);
                break;
            case "TWITTER":
                //  loginIntent = twitterProvider.activity.getIntent();
                Intent loginIntent2 = createIntent();
                plugin.startActivityForResult(call, loginIntent2, "loginResult");
                twitterProvider = twitterProvider.login(twitterProvider, call, activity);
                break;
        }

        call.resolve();
    }

    public void logoutFromProvider(final PluginCall call) {
        if (call == null) return;
        if (!call.hasOption("provider")) call.reject("provider name missing");

        String providerName = call.getString("provider");

        switch (Objects.requireNonNull(providerName)) {
            case "APPLE":
                //appleProvider.login(call);
                break;
            case "GOOGLE":
                googleProvider.logout();
                break;
            case "FACEBOOK":
                facebookProvider.logout(call);
                break;
            case "TWITTER":
                twitterProvider.logout(twitterProvider, call);
                break;
        }

        call.resolve();
    }

    public void setAppStatusChangeListener(@Nullable AppStatusChangeListener listener) {
        this.statusChangeListener = listener;
    }

    @Nullable
    public AppStatusChangeListener getAppStatusChangeListener() {
        return statusChangeListener;
    }

    /*public void startActivityForResult(final PluginCall call, Intent intent, String callbackName) {
        plugin.startActivityForResult(call, intent, callbackName);
    }*/

    public void handleSuccessfulSignIn(
        final PluginCall call,
        @Nullable AuthCredential credential,
        @Nullable String idToken,
        @Nullable String nonce,
        @Nullable String accessToken,
        @Nullable AdditionalUserInfo additionalUserInfo
    ) {
        JSObject response = new JSObject();
        response.put("credential", credential);
        response.put("idToken", idToken);
        response.put("nonce", nonce);
        response.put("accessToken", accessToken);
        response.put("additionalUserInfo", additionalUserInfo);
        call.resolve(response);
    }

    public void handleFailedSignIn(final PluginCall call, String message, Exception exception) {
        if (message == null && exception != null) {
            message = exception.getLocalizedMessage();
        }
        call.reject(message, exception);
    }

    public LoginProviderPlugin getPlugin() {
        return plugin;
    }

    private void initAuthProviderHandlers() {
        String appleProvider = config.getString("apple");
        String facebookProvider = config.getString("facebook");
        String googleProvider = config.getString("google");
        String twitterProvider = config.getString("twitter");

        if (!Objects.equals(appleProvider, "")) {
            //appleProvider = new AppleProvider();
        } else if (!Objects.equals(facebookProvider, "")) {
            this.facebookProvider = new FacebookProvider(this);
        } else if (!Objects.equals(googleProvider, "")) {
            //googleProvider = new GoogleProvider(this);
        } else if (!Objects.equals(twitterProvider, "")) {
            this.twitterProvider = new TwitterProvider(context, activity, config);
        }
    }

    private Intent createIntent() {
        Intent intent;
        try {
            Context context = this.plugin.getBridge().getActivity().getApplicationContext();
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // context.startActivity(intent);
            return intent;
        } catch (Exception exception) {
            Log.e(LoginProviderPlugin.LOG_TAG, "initialization failed.", exception);
            return null;
        }
    }
}
