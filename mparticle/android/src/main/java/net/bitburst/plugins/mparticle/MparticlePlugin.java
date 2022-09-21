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

    private String planId = "bitcode_frontend_plan";
    private int planVersion = 4;

    String mParticleKey = "eu1-f7ebd620020cce4e9672de46c1f443ac"; //this.getContext().getString(R.string.mparticle_key);
    String mParticleSecret = ""; // this.getContext().getString(R.string.mparticle_secret);

    @Override
    public void load() {
        try {
            JSObject mParticleConfig = fromJSONObject(getConfig().getObject("config"));
            /* Boolean isDevelopmentMode = mParticleConfig.getBoolean("isDevelopmentMode"); */
            //mParticleKey = getConfig().getString("key");
            mParticleSecret = getConfig().getString("secret");

            JSObject dataPlan = mParticleConfig.getJSObject("dataPlan");
            assert dataPlan != null;
            planId = dataPlan.getString("planId");
            planVersion = dataPlan.getInt("planVersion");
        } catch (JSONException e) {
            Log.d(LOG_TAG, "load failed ", e);
        }

        MParticleOptions options = MParticleOptions
            .builder(getBridge().getContext())
            .credentials(mParticleKey, mParticleSecret)
            .environment(MParticle.Environment.AutoDetect)
            .dataplan(planId, planVersion)
            .build();
        implementation = new Mparticle(this, options);

        super.load();
        notifyListeners("mParticleReady", new JSObject().put("ready", Mparticle.getInstance() != null), true);
    }

    @PluginMethod
    public void init(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void identifyUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        IdentityApiRequest request = implementation.identityRequest(email, customerId);
        Mparticle.getInstance().Identity().identify(request);
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
        Integer type = callData.getInteger("eventType");
        assert name != null;
        assert type != null;

        MPEvent event = new MPEvent.Builder(name, implementation.getEventType(type)).customAttributes(customAttributes).build();

        Mparticle.getInstance().logEvent(event);
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

        Mparticle.getInstance().logScreen(name, pageData);
        call.resolve();
    }

    @PluginMethod
    public void loginUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle.getInstance().Identity().login(implementation.identityRequest(email, customerId));
        call.resolve();
    }

    @PluginMethod
    public void logoutUser(PluginCall call) {
        Mparticle.getInstance().Identity().logout(IdentityApiRequest.withEmptyUser().build());
        call.resolve();
    }

    @PluginMethod
    public void registerUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle
            .getInstance()
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
