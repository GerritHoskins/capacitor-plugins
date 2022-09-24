package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.mparticle.consent.GDPRConsent;
import com.mparticle.identity.IdentityApiRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;

@CapacitorPlugin(name = "Mparticle")
public class MparticlePlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.mparticle ";
    private static Mparticle implementation = null;

    @Override
    public void load() {
        implementation = new Mparticle(this);
        //notifyListeners("mParticleReady", new JSObject().put("ready", Mparticle.sharedInstance() != null), true);
        super.load();
    }

    @PluginMethod
    public void init(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void identifyUser(PluginCall call) {
        try {
            IdentityApiRequest request = implementation.identityRequest(call, call.getData());
            Mparticle
                .sharedInstance()
                .Identity()
                .identify(request)
                .addFailureListener(
                    identityHttpResponse -> call.reject("message ", Objects.requireNonNull(identityHttpResponse).toString())
                )
                .addSuccessListener(
                    identityApiResult -> {
                        implementation.identityResultHandler(call.getData(), identityApiResult);
                        call.resolve();
                    }
                );
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void setUserAttribute(PluginCall call) {
        String name = call.getString("name");
        String value = call.getString("value");
        assert name != null;
        assert value != null;
        implementation.setUserAttribute(name, value);
        call.resolve();
    }

    @PluginMethod
    public void setGDPRConsent(PluginCall call) {
        try {
            implementation.addGDPRConsentState(call.getData());
            call.resolve();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void getGDPRConsent(PluginCall call) {
        try {
            Map<String, GDPRConsent> consents = implementation.getGDPRConsent();
            JSObject ret = new JSObject().put("consents", consents);
            call.resolve(ret);
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void getMPID(PluginCall call) {
        long mpid = implementation.currentUser().getId();
        JSObject ret = new JSObject().put("MPID", mpid);
        call.resolve(ret);
    }

    @PluginMethod
    public void trackEvent(PluginCall call) {
        JSObject callData = call.getData();
        if (callData == null) return;
        try {
            Mparticle.sharedInstance().logEvent(implementation.trackEvent(callData));
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, "failed to track event ", e);
        }
        call.resolve();
    }

    @PluginMethod
    public void trackPageView(PluginCall call) {
        JSObject callData = call.getData();
        if (callData == null) return;
        try {
            Mparticle.sharedInstance().logScreen(implementation.trackEvent(callData));
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, "failed to track page view ", e);
        }
        call.resolve();
    }

    @PluginMethod
    public void loginUser(PluginCall call) {
        try {
            Mparticle.sharedInstance().Identity().login(implementation.identityRequest(call, call.getData()));
            call.resolve();
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void logoutUser(PluginCall call) {
        try {
            Mparticle.sharedInstance().Identity().logout(implementation.identityRequest(call, call.getData()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        call.resolve();
    }

    @PluginMethod
    public void registerUser(PluginCall call) {
        try {
            Mparticle
                .sharedInstance()
                .Identity()
                .login(implementation.identityRequest(call, call.getData()))
                .addFailureListener(
                    identityHttpResponse -> {
                        call.reject("message ", Objects.requireNonNull(identityHttpResponse).toString());
                    }
                )
                .addSuccessListener(identityApiResult -> implementation.identityResultHandler(call.getData(), identityApiResult));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void addListener(PluginCall call) {
        super.addListener(call);
    }
}
