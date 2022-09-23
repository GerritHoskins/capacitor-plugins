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
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

@CapacitorPlugin(name = "Mparticle")
public class MparticlePlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.mparticle ";
    private static Mparticle implementation = null;

    @Override
    public void load() {
        implementation = new Mparticle(this);
        notifyListeners("mParticleReady", new JSObject().put("ready", Mparticle.sharedInstance() != null), true);
        super.load();
    }

    @PluginMethod
    public void identifyUser(PluginCall call) {
        String email = call.getString("email");
        String other = call.getString("other");
        String customerId = call.getString("customerId");
        IdentityApiRequest request = null;
        try {
            request = implementation.identityRequest(call, email, customerId, other);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Mparticle.sharedInstance().Identity().identify(request);
        call.resolve();
    }

    @PluginMethod
    public void setUserAttribute(PluginCall call) {
        String name = call.getString("attributeName");
        String value = call.getString("attributeValue");
        assert name != null;
        assert value != null;

        if (implementation.currentUser() != null) {
            implementation.currentUser().setUserAttribute(name, value);
        }
        call.resolve();
    }

    @PluginMethod
    public void setGDPRConsent(PluginCall call) {
        JSONArray consents = (JSONArray) call.getArray("consents");
        try {
            implementation.addGDPRConsentState(consents);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        call.resolve();
    }

    @PluginMethod
    public void getGDPRConsent(PluginCall call) {
        Map<String, GDPRConsent> consents = new HashMap();
        try {
            consents = implementation.getGDPRConsent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSObject ret = new JSObject().put("consents", consents);
        call.resolve(ret);
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
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        String other = call.getString("other");
        try {
            Mparticle.sharedInstance().Identity().login(implementation.identityRequest(call, email, customerId, other));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        call.resolve();
    }

    @PluginMethod
    public void logoutUser(PluginCall call) {
        try {
            Mparticle.sharedInstance().Identity().logout(implementation.identityRequest(call, null, null, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        call.resolve();
    }

    @PluginMethod
    public void registerUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        try {
            Mparticle
                .sharedInstance()
                .Identity()
                .login(implementation.identityRequest(call, email, customerId, null))
                .addSuccessListener(
                    result -> {
                        JSObject temp = call.getObject("userAttributes");
                        if (temp != null) {
                            Iterator<String> iter = temp.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    Object value = temp.get(key);
                                    result.getUser().setUserAttribute(key, value);
                                } catch (JSONException e) {
                                    call.reject(LOG_TAG, "failed to register user", e);
                                }
                            }
                        }
                    }
                );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        call.resolve();
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void addListener(PluginCall call) {
        super.addListener(call);
    }
}
