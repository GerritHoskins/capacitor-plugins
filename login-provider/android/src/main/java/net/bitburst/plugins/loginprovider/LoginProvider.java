package net.bitburst.plugins.loginprovider;

import android.util.Log;
import net.bitburst.plugins.loginprovider.handlers.AppleAuthProviderHandler;
import net.bitburst.plugins.loginprovider.handlers.FacebookAuthProviderHandler;
import net.bitburst.plugins.loginprovider.handlers.GoogleAuthProviderHandler;
import net.bitburst.plugins.loginprovider.handlers.OAuthProviderHandler;

public class LoginProvider {

    public interface AppStatusChangeListener {
        void onAppStatusChanged(Boolean isActive);
    }

    private AppStatusChangeListener statusChangeListener;

    private LoginProviderPlugin plugin;
    private LoginProviderConfig config;

    private AppleAuthProviderHandler appleAuthProviderHandler;
    private FacebookAuthProviderHandler facebookAuthProviderHandler;
    private GoogleAuthProviderHandler googleAuthProviderHandler;

    public LoginProvider(LoginProviderPlugin plugin, LoginProviderConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.initAuthProviderHandlers(config);
        // add auth listeners?
        this.statusChangeListener =
            execute(
                () -> {
                    if (statusChangeListener != null) {
                        statusChangeListener.onAppStatusChanged();
                    }
                }
            );
    }

    public void setAppStatusChangeListener(@Nullable AppStatusChangeListener listener) {
        this.statusChangeListener = listener;
    }

    @Nullable
    public AppStatusChangeListener getAppStatusChangeListener() {
        return statusChangeListener;
    }

    public void signInWithApple(final PluginCall call) {
        appleAuthProviderHandler.signIn(call);
    }

    public void signInWithFacebook(final PluginCall call) {
        facebookAuthProviderHandler.signIn(call);
    }

    public void signInWithGoogle(final PluginCall call) {
        googleAuthProviderHandler.signIn(call);
    }

    public void signInWithTwitter(final PluginCall call) {
        oAuthProviderHandler.signIn(call, "TWITTER");
    }

    public void signOut(final PluginCall call) {
        if (googleAuthProviderHandler != null) {
            googleAuthProviderHandler.signOut();
        }
        if (facebookAuthProviderHandler != null) {
            facebookAuthProviderHandler.signOut();
        }
        call.resolve();
    }

    public void startActivityForResult(final PluginCall call, Intent intent, String callbackName) {
        plugin.startActivityForResult(call, intent, callbackName);
    }

    public void handleGoogleAuthProviderActivityResult(final PluginCall call, ActivityResult result) {
        googleAuthProviderHandler.handleOnActivityResult(call, result);
    }

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FacebookAuthProviderHandler.RC_FACEBOOK_AUTH && facebookAuthProviderHandler != null) {
            facebookAuthProviderHandler.handleOnActivityResult(requestCode, resultCode, data);
        }
    }

    public void handleSuccessfulSignIn(
        final PluginCall call,
        @Nullable AuthCredential credential,
        @Nullable String idToken,
        @Nullable String nonce,
        @Nullable String accessToken,
        @Nullable AdditionalUserInfo additionalUserInfo
    ) {
        //return auth data
    }

    public void handleFailedSignIn(final PluginCall call, String message, Exception exception) {
        if (message == null && exception != null) {
            message = exception.getLocalizedMessage();
        }
        call.reject(message, exception);
    }

    public LoginProvider getLoginProviderInstance() {
        return loginProviderInstance;
    }

    public LoginProviderPlugin getPlugin() {
        return plugin;
    }

    public LoginProviderConfig getConfig() {
        return config;
    }

    private void initAuthProviderHandlers(LoginProviderConfig config) {
        List providerList = Arrays.asList(config.getProviders());
        if (providerList.contains("FACEBOOK")) {
            facebookAuthProviderHandler = new FacebookAuthProviderHandler(this);
        }
        if (providerList.contains("GOOGLE")) {
            googleAuthProviderHandler = new GoogleAuthProviderHandler(this);
        }
        if (providerList.contains("APPLE")) {
            appleAuthProviderHandler = new AppleAuthProviderHandler(this);
        }
        oAuthProviderHandler = new OAuthProviderHandler(this);
    }
}
