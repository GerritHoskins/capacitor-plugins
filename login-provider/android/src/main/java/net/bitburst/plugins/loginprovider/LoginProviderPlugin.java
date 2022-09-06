package net.bitburst.plugins.loginprovider;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import net.bitburst.plugins.loginprovider.providers.FacebookProvider;
import net.bitburst.plugins.loginprovider.providers.GoogleProvider;
import net.bitburst.plugins.loginprovider.providers.TwitterProvider;

import java.util.Objects;

@CapacitorPlugin(
    name = "LoginProvider",
    permissions = { @Permission(alias = "internet", strings = {}) },
    requestCodes = { TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE }
)
public class LoginProviderPlugin extends Plugin {

    public interface AppStatusChangeListener {
        void onAppStatusChanged(Boolean isActive);
    }

    public static final String LOG_TAG = "LoginProvider ";
    public static final String AUTH_STATE_CHANGE_EVENT = "authStateChange";

    private AppStatusChangeListener statusChangeListener;

    private FacebookProvider facebookProvider;
    private GoogleProvider googleProvider;
    private TwitterProvider twitterProvider = null;

    private JSObject facebookOptions;
    private JSObject googleOptions;
    private JSObject twitterOptions;

    @Override
    public void load() {
        super.load();
        initializeProviders();
    }

    private void initializeProviders() {
        facebookOptions = LoginProviderHelper.convertJSONObject(getConfig().getObject("facebook"));
        googleOptions = LoginProviderHelper.convertJSONObject(getConfig().getObject("google"));
        twitterOptions = LoginProviderHelper.convertJSONObject(getConfig().getObject("twitter"));

        facebookProvider = new FacebookProvider(this, facebookOptions);
        googleProvider = new GoogleProvider(this, googleOptions);
        twitterProvider = getTwitterProviderInstance();
    }

    @PluginMethod
    public void loginWithProvider(PluginCall call) {
        if (call == null) return;
        if (!call.hasOption("provider")) call.reject("provider name missing");

        try {
            String providerName = call.getString("provider");

            //   call.setKeepAlive(true);
            switch (Objects.requireNonNull(providerName)) {
                case "APPLE":
                    call.unimplemented();
                    break;
                case "GOOGLE":
                    googleProvider.login(call);
                    break;
                case "FACEBOOK":
                    facebookProvider.login(call);
                    break;
                case "TWITTER":
                    getTwitterProviderInstance().login(call);
                    break;
            }
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    private TwitterProvider getTwitterProviderInstance() {
        if (twitterProvider == null) {
            twitterProvider = new TwitterProvider(this, twitterOptions);
        }

        return twitterProvider;
    }

    @PluginMethod
    public void loginWithApple(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void loginWithFacebook(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void loginWithGoogle(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void loginWithTwitter(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void logoutFromProvider(PluginCall call) {
        if (call == null) return;
        if (!call.hasOption("provider")) call.reject("provider name missing");

        try {
            String providerName = call.getString("provider");
            switch (Objects.requireNonNull(providerName)) {
                case "APPLE":
                    call.unimplemented();
                    break;
                case "GOOGLE":
                    googleProvider.logout();
                    break;
                case "FACEBOOK":
                    facebookProvider.logout(call);
                    break;
                case "TWITTER":
                    twitterProvider.logout(call);
                    break;
            }
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void addListener(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void removeAllListeners(PluginCall call) {
        call.unimplemented();
    }

    @ActivityCallback
    protected void facebookLoginRequest(PluginCall call, ActivityResult result) {
        facebookProvider.handleLoginRequest(call, result);
    }

    @ActivityCallback
    protected void googleLoginRequest(PluginCall call, ActivityResult result) {
        googleProvider.handleLoginRequest(call, result);
    }

    @ActivityCallback
    protected void twitterLoginResult(PluginCall call, ActivityResult result) {}

    @SuppressWarnings("deprecation")
    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);
        getTwitterProviderInstance().handleLoginResult(TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE, resultCode, data);
    }

    public void notifyListeners(String eventName, JSObject data) {
        super.notifyListeners(eventName, data);
    }

    public void setAppStatusChangeListener(@Nullable AppStatusChangeListener listener) {
        statusChangeListener = listener;
    }

    @Nullable
    public AppStatusChangeListener getAppStatusChangeListener() {
        return statusChangeListener;
    }
}
