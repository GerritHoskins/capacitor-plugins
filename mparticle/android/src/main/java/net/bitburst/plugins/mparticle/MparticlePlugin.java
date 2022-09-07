package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.mparticle.*;
import com.mparticle.MParticle;
import com.mparticle.MParticle.EventType;
import com.mparticle.MParticle.ServiceProviders;
import com.mparticle.MParticleOptions;
import com.mparticle.commerce.CommerceEvent;
import com.mparticle.commerce.CommerceEvent.Builder;
import com.mparticle.commerce.Product;
import com.mparticle.commerce.TransactionAttributes;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.IdentityApiResult;
import com.mparticle.identity.TaskSuccessListener;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import net.bitburst.plugins.mparticle.Mparticle;

@CapacitorPlugin(name = "MparticlePlugin")
public class MparticlePlugin extends Plugin {

    private Mparticle implementation = new Mparticle();

    private String appId;
    private String planId;
    private final int planVersion = 4;
    private Boolean isDevelopmentMode;

    String mParticleKey = this.getString(R.string.mparticle_key);
    String mParticleSecret = this.getString(R.string.mparticle_secret);

    public void load() {
        appId = getConfig().getString("appId");
        JSONObject mParticleConfig = getConfig().getObject("config");
        isDevelopmentMode = mParticleConfig.getBoolean("isDevelopmentMode");

        JSONObject dataPlan = mParticleConfig.getObject("dataPlan");
        planId = dataPlan.getString("planId", "bitcode_frontend_plan");
        planVersion = dataPlan.getInt("planVersion", 4);

        MParticleOptions options = MParticleOptions.builder(this)
                .credentials(mParticleKey, mParticleSecret)
                .environment(MParticle.Environment.AutoDetect)
                .dataplan(planId, planVersion)
                .build();

        MParticle.start(options);
        notifyListeners("mParticleReady", MParticle.getInstance().isInitialized(), false);
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void init(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void initConfig(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void identifyUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        String other = call.getString("other");
        IdentityApiRequest request = implementation.identityRequest(email, customerId);
        Mparticle.getInstance().Identity().identifyUser(request);
        call.resolve(new JSObject());
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
        String mpid = Mparticle.getInstance().getMPID();
        JSObject data = new JSObject();
        call.resolve(data.put("MPID", mpid));
    }

    @PluginMethod
    public void logEvent(PluginCall call) {
        Map<String, String> customAttributes = new HashMap<String, String>();
        JSObject temp = call.getObject("eventProperties");
        if (temp != null) {
            Iterator<String> iter = temp.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = temp.get(key);
                    customAttributes.put(key, value.toString());
                } catch (JSONException e) {
                    call.reject("failed to log event", e);
                }
            }
        }
        String name = call.getString("eventName");
        int type = call.getInt("eventType");

        MPEvent event = new MPEvent.Builder(name, implementation.getEventType(type)).customAttributes(customAttributes).build();

        Mparticle.getInstance().logEvent(event);
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void logPageView(PluginCall call) {
        String name = call.getString("pageName");
        String link = call.getString("pageLink");
        Map<String, String> screenInfo = new HashMap<String, String>();
        screenInfo.put("page", link);
        Mparticle.getInstance().logScreen(name, screenInfo);
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void setUserAttribute(PluginCall call) {
        String name = call.getString("attributeName");
        String value = call.getString("attributeValue");
        if (implementation.currentUser() != null) {
            implementation.currentUser().setUserAttribute(name, value);
        }
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void loginUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle.getInstance().Identity().login(implementation.identityRequest(email, customerId));
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void logoutUser(PluginCall call) {
        Mparticle.getInstance().Identity().logout(IdentityApiRequest.withEmptyUser().build());
        call.resolve(new JSObject());
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
                new TaskSuccessListener() {
                    public void onSuccess(IdentityApiResult result) {
                        JSObject temp = call.getObject("userAttributes");
                        if (temp != null) {
                            Iterator<String> iter = temp.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    Object value = temp.get(key);
                                    result.getUser().setUserAttribute(key, value);
                                } catch (JSONException e) {
                                    call.reject("failed to register user", e);
                                }
                            }
                        }
                    }
                }
            );
        call.resolve(new JSObject());
    }
}
