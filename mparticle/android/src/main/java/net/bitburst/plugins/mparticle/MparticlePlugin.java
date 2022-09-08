package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.mparticle.MPEvent;
import com.mparticle.MParticle;
import com.mparticle.MParticleOptions;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.IdentityApiResult;
import com.mparticle.identity.TaskSuccessListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "MparticlePlugin")
public class MparticlePlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.mparticle ";
    private static Mparticle implementation = null;

    private String planId = "bitcode_frontend_plan";
    private int planVersion = 4;

    String mParticleKey = getContext().getString(R.string.MPARTICLE_KEY);
    String mParticleSecret = getContext().getString(R.string.MPARTICLE_SECRET);

    @Override
    public void load() {
        String appId = getConfig().getString("appId");
        JSONObject mParticleConfig = getConfig().getObject("config");

        try {
            // Boolean isDevelopmentMode = mParticleConfig.getBoolean("isDevelopmentMode");
            JSONObject dataPlan = mParticleConfig.getJSONObject("dataPlan");
            planId = dataPlan.getString("planId");
            planVersion = (int) dataPlan.get("planVersion");
        } catch (JSONException e) {
            e.printStackTrace();
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
    public void echo(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void init(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void identifyUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        String other = call.getString("other");
        IdentityApiRequest request = implementation.identityRequest(email, customerId);
        Objects.requireNonNull(MParticle.getInstance()).Identity().identify(request);
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
    public void setGDPRConsent(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void getGDPRConsent(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void getMPID(PluginCall call) {
        //String mpid = implementation.getInstance().getMPID();
        JSObject data = new JSObject();
        data.put("MPID", "mpid");
        call.resolve();
    }

    @PluginMethod
    public void logEvent(PluginCall call) {
        JSObject callData = call.getData();
        if (callData == null) return;

        Map<String, String> customAttributes = new HashMap<String, String>();
        JSObject temp = callData.getJSObject("eventProperties");
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

        String name = callData.getString("eventName");
        Integer type = callData.getInteger("eventType");

        MPEvent event = new MPEvent.Builder(Objects.requireNonNull(name), implementation.getEventType(type))
            .customAttributes(customAttributes)
            .build();

        Objects.requireNonNull(MParticle.getInstance()).logEvent(event);
        call.resolve();
    }

    @PluginMethod
    public void logPageView(PluginCall call) {
        String name = call.getString("pageName");
        String link = call.getString("pageLink");
        Map<String, String> screenInfo = new HashMap<String, String>();
        screenInfo.put("page", link);
        MParticle.getInstance().logScreen(name, screenInfo);
        call.resolve(new JSObject());
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

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void addListener(PluginCall call) {
        super.addListener(call);
        addListener(call);
    }
}
