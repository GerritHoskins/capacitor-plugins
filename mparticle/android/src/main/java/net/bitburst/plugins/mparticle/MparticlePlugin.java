package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.mparticle.consent.GDPRConsent;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.MParticleUser;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;

@CapacitorPlugin(name = "Mparticle")
public class MparticlePlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.mparticle.plugin ";
    private static Mparticle implementation = null;

    @Override
    public void load() {
        implementation = new Mparticle(this);
        super.load();
    }

    @PluginMethod
    public void init(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void identifyUser(PluginCall call) {
        JSObject data = call.getData();
        try {
            IdentityApiRequest request = implementation.identityRequest(call, data);
            Mparticle
                .sharedInstance()
                .Identity()
                .identify(request)
                .addFailureListener(identityHttpResponse -> call.reject(LOG_TAG, Objects.requireNonNull(identityHttpResponse).toString()))
                .addSuccessListener(
                    identityApiResult -> {
                        if (data != null) {
                            Iterator<String> iterator = data.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                try {
                                    MParticleUser user = identityApiResult.getUser();
                                    user.setUserAttribute(key, data.get(key));

                                    JSObject ret = new JSObject();
                                    ret.put("userId", Long.toString(user.getId()));
                                    call.resolve();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    call.reject(LOG_TAG, e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                );
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @PluginMethod
    public void setUserAttribute(PluginCall call) {
        String userId = call.getString("userId");
        String name = call.getString("name");
        JSObject value = call.getObject("value");
        var success = implementation.setUserAttribute(userId, name, value);
        if (!success) {
            call.reject(LOG_TAG, "failed to set user attribute");
            return;
        }
        call.resolve();
    }

    @PluginMethod
    public void setUserAttributes(PluginCall call) {
        try {
            List<Object> attributes = call.getArray("attributes").toList();
            String userId = call.getString("userId");
            var success = implementation.setUserAttributes(userId, attributes);
            if (!success) {
                call.reject(LOG_TAG, "failed to set user attributes");
                return;
            }
            call.resolve();
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, e.getLocalizedMessage());
        }
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
        JSObject ret = new JSObject().put("userId", Long.toString(mpid));
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
    public void trackPurchase(PluginCall call) {
        JSObject callData = call.getData();
        if (callData == null) return;
        try {
            Mparticle.sharedInstance().logEvent(implementation.trackPurchaseEvent(callData));
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(LOG_TAG, "failed to track purchase event ", e);
        }
        call.resolve();
    }
}
