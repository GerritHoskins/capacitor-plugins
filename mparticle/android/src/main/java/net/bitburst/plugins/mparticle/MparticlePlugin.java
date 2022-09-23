package net.bitburst.plugins.mparticle;

import static com.getcapacitor.JSObject.fromJSONObject;

import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.mparticle.MPEvent;
import com.mparticle.MParticle;
import com.mparticle.MParticleOptions;
import com.mparticle.identity.IdentityApiRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        String customerId = call.getString("customerId");
        IdentityApiRequest request = implementation.identityRequest(email, customerId);
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
        call.unimplemented();
    }

    @PluginMethod
    public void getGDPRConsent(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void getMPID(PluginCall call) {
        assert implementation.currentUser() != null;
        long mpid = implementation.currentUser().getId();
        JSObject ret = new JSObject().put("MPID", mpid);
        call.resolve(ret);
    }

    @PluginMethod
    public void trackEvent(PluginCall call) {
        JSObject callData = call.getData();
        if (callData == null) return;

        Map<String, String> customAttributes = new HashMap<>();
        JSObject temp = callData.getJSObject("data");
        if (temp != null) {
            Iterator<String> iter = temp.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = temp.get(key);
                    customAttributes.put(key, value.toString());
                } catch (JSONException e) {
                    call.reject(LOG_TAG, "failed to log event ", e);
                }
            }
        }

        String name = callData.getString("name");
        Integer type = callData.getInteger("eventType", 8); //EventType 8:OTHER
        assert name != null;
        assert type != null;

        MPEvent event = new MPEvent.Builder(name, implementation.getEventType(type)).customAttributes(customAttributes).build();

        Mparticle.sharedInstance().logEvent(event);
        call.resolve();
    }

    @PluginMethod
    public void trackPageView(PluginCall call) {
        String name = call.getString("name");
        assert name != null;

        Map<String, String> pageData = new HashMap<>();
        try {
            JSObject temp = fromJSONObject(call.getObject("data"));
            Iterator<String> iter = temp.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = temp.get(key);
                    pageData.put(key, value.toString());
                } catch (JSONException e) {
                    call.reject(LOG_TAG, "failed to log page view ", e);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Mparticle.sharedInstance().logScreen(name, pageData);
        call.resolve();
    }

    @PluginMethod
    public void loginUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle.sharedInstance().Identity().login(implementation.identityRequest(email, customerId));
        call.resolve();
    }

    @PluginMethod
    public void logoutUser(PluginCall call) {
        Mparticle.sharedInstance().Identity().logout(IdentityApiRequest.withEmptyUser().build());
        call.resolve();
    }

    @PluginMethod
    public void registerUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle
            .sharedInstance()
            .Identity()
            .login(implementation.identityRequest(email, customerId))
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
        call.resolve();
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void addListener(PluginCall call) {
        super.addListener(call);
    }
}
