package net.bitburst.plugins.mparticle;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.mparticle.MPEvent;
import com.mparticle.MParticle;
import com.mparticle.MParticle.EventType;
import com.mparticle.MParticleOptions;
import com.mparticle.consent.ConsentState;
import com.mparticle.consent.GDPRConsent;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.MParticleUser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mparticle {

    public interface OnReadyListener {
        void onReady();
    }

    private OnReadyListener onReadyListener;
    private PluginCall callbackContext;
    private static MParticle instance = MParticle.getInstance();

    public Mparticle(MparticlePlugin plugin) {
        this.onReadyListener =
            () -> {
                if (onReadyListener != null) {
                    onReadyListener.onReady();
                }
            };
        start(plugin);
    }

    public void setOnReadyListener(@Nullable OnReadyListener listener) {
        this.onReadyListener = listener;
    }

    @Nullable
    public OnReadyListener getAuthStateChangeListener() {
        return onReadyListener;
    }

    @SuppressLint("MParticleInitialization")
    public void start(MparticlePlugin plugin) {
        String mParticleKey = plugin.getConfig().getString("key");
        String mParticleSecret = plugin.getConfig().getString("secret");
        MParticleOptions options = MParticleOptions
            .builder(plugin.getBridge().getContext())
            .credentials(mParticleKey, mParticleSecret)
            .environment(MParticle.Environment.AutoDetect)
            .dataplan("bitcode_frontend_plan", 4)
            .build();
        MParticle.start(options);
    }

    public static MParticle sharedInstance() {
        if (instance == null) {
            instance = MParticle.getInstance();
        }
        return instance;
    }

    public EventType getEventType(Integer eType) {
        int ord = eType;
        for (EventType e : EventType.values()) {
            if (e.ordinal() == ord) {
                return e;
            }
        }
        return EventType.Other;
    }

    public MParticleUser currentUser() {
        MParticleUser user = MParticle.getInstance().Identity().getCurrentUser();
        assert user != null;
        return user;
    }

    public MPEvent trackEvent(JSObject data) throws JSONException {
        JSONObject callData = (JSONObject) data.getJSObject("data");

        Map<String, String> customAttributes = new HashMap<>();
        customAttributes = ConvertStringMap(callData);

        String name = data.getString("name");
        Integer type = data.getInteger("eventType", 8); //EventType 8:OTHER
        assert name != null;
        assert type != null;
        return new MPEvent.Builder(name, getEventType(type)).customAttributes(customAttributes).build();
    }

    public void addGDPRConsentState(final JSONArray consents) throws JSONException, ParseException {
        MParticleUser user = currentUser();
        final JSONObject map = consents.getJSONObject(0);
        String purpose = consents.getString(1);
        if (map != null && purpose != null) {
            GDPRConsent newConsent = ConvertGDPRConsent(map);
            ConsentState state = ConsentState.builder().addGDPRConsentState(purpose, newConsent).build();

            user.setConsentState(state);
        }
    }

    public Map<String, GDPRConsent> getGDPRConsent() throws JSONException {
        MParticleUser user = currentUser();
        assert user != null;
        ConsentState consentState = user.getConsentState();
        return consentState.getGDPRConsentState();
    }

    public IdentityApiRequest identityRequest(PluginCall call, @Nullable String email, @Nullable String customerId, @Nullable String other)
        throws JSONException {
        callbackContext = call;
        callbackContext.setKeepAlive(true);
        if (other != null) {
            JSONObject map = new JSONObject();
            map.put("email", email);
            map.put("customerId", customerId);
            map.put("other", other);
            IdentityApiRequest request = ConvertIdentityAPIRequest(map);
            return request;
        }
        return IdentityApiRequest.withEmptyUser().email(email).customerId(customerId).build();
    }

    public void identify(final JSONArray args) throws JSONException {
        final JSONObject map = args.getJSONObject(0);
        if (map != null) {
            IdentityApiRequest request = ConvertIdentityAPIRequest(map);
            MParticle
                .getInstance()
                .Identity()
                .identify(request)
                .addFailureListener(
                    identityHttpResponse -> {
                        callbackContext.reject("message ", identityHttpResponse.toString());
                    }
                )
                .addSuccessListener(
                    identityApiResult -> {
                        MParticleUser user = identityApiResult.getUser();
                        String userID = Long.toString(user.getId());

                        JSObject result = new JSObject();
                        result.put("user", user);
                        callbackContext.resolve(result);
                    }
                );
        }
    }

    public static Map<String, String> ConvertStringMap(JSONObject jsonObject) throws JSONException {
        Map<String, String> map = new HashMap();

        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, jsonObject.getString(key));
            }
        }

        return map;
    }

    private static IdentityApiRequest ConvertIdentityAPIRequest(JSONObject map) throws JSONException {
        IdentityApiRequest.Builder identityRequest = IdentityApiRequest.withEmptyUser();

        Map<MParticle.IdentityType, String> userIdentities = ConvertUserIdentities(map);
        identityRequest.userIdentities(userIdentities);

        return identityRequest.build();
    }

    private static Map<MParticle.IdentityType, String> ConvertUserIdentities(JSONObject jsonObject) throws JSONException {
        Map<MParticle.IdentityType, String> map = new HashMap();
        if (jsonObject != null) {
            Map<String, String> stringMap = ConvertStringMap(jsonObject);
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                MParticle.IdentityType identity = ConvertIdentityType(entry.getKey());
                String value = entry.getValue();
                map.put(identity, value);
            }
        }

        return map;
    }

    private static GDPRConsent ConvertGDPRConsent(JSONObject map) throws JSONException, ParseException {
        String dateStr = map.getString("timestamp");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timestamp = sdf.parse(dateStr);

        GDPRConsent consent = GDPRConsent
            .builder(map.getBoolean("consented"))
            .document(map.getString("document"))
            .timestamp(timestamp.getTime())
            .location(map.getString("location"))
            .hardwareId(map.getString("hardwareId"))
            .build();

        return consent;
    }

    private static MParticle.IdentityType ConvertIdentityType(String val) {
        if (val.equals("customerId")) {
            return MParticle.IdentityType.CustomerId;
        } else if (val.equals("email")) {
            return MParticle.IdentityType.Email;
        } else {
            return MParticle.IdentityType.Other;
        }
    }
}
