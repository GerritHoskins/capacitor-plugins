package net.bitburst.plugins.loginprovider;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import net.bitburst.plugins.loginprovider.handlers.FacebookAuthProviderHandler;

@CapacitorPlugin(name = "LoginProvider", requestCodes = { FacebookAuthProviderHandler.RC_FACEBOOK_AUTH })
public class LoginProviderPlugin extends Plugin {

    public static final String TAG = "LoginProvier";
    public static final String AUTH_STATE_CHANGE_EVENT = "authStateChange";

    //private LoginProvider implementation = new LoginProvider();

    private LoginProviderConfig config;
    private LoginProvider implementation;

    public void load() {
        config = getLoginProviderConfig();
        implementation = new LoginProvider(this, config);
        implementation.setAppStatusChangeListener(this::updateAuthState);
    }

    @PluginMethod
    public void signInWithFacebook(PluginCall call) {
        try {
            implementation.signInWithFacebook(call);
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void signInWithGoogle(PluginCall call) {
        try {
            startActivityForResult(call, signInIntent, "signInResult");
            implementation.signInWithGoogle(call);
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void signInWithApple(PluginCall call) {
        try {
            implementation.signInWithApple(call);
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void signInWithTwitter(PluginCall call) {
        try {
            implementation.signInWithTwitter(call);
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void signOut(PluginCall call) {
        try {
            implementation.signOut(call);
        } catch (Exception ex) {
            call.reject(ex.getLocalizedMessage());
        }
    }

    @Override
    public void startActivityForResult(PluginCall call, Intent intent, String callbackName) {
        super.startActivityForResult(call, intent, callbackName);
    }

    public void notifyListeners(String eventName, JSObject data) {
        super.notifyListeners(eventName, data);
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);
        implementation.handleOnActivityResult(requestCode, resultCode, data);
    }

    private void updateAppState() {
        FirebaseUser user = implementation.getCurrentUser();
        JSObject userResult = LoginProviderHelper.createUserResult(user);
        JSObject result = new JSObject();
        result.put("user", userResult);
        notifyListeners(AUTH_STATE_CHANGE_EVENT, result);
    }

    @ActivityCallback
    private void handleGoogleAuthProviderActivityResult(PluginCall call, ActivityResult result) {
        implementation.handleGoogleAuthProviderActivityResult(call, result);
    }

    private LoginProviderConfig getLoginProviderConfig() {
        LoginProviderConfig config = new LoginProviderConfig();

        boolean skipNativeAuth = getConfig().getBoolean("skipNativeAuth", config.getSkipNativeAuth());
        config.setSkipNativeAuth(skipNativeAuth);
        String[] providers = getConfig().getArray("providers", config.getProviders());
        config.setProviders(providers);

        return config;
    }
}
