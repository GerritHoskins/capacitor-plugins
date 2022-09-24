package net.bitburst.plugins.mparticle;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
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
        start(plugin.getActivity().getApplication());
    }

    public Mparticle(Application application) {
        start(application);
    }

    public void setOnReadyListener(@Nullable OnReadyListener listener) {
        this.onReadyListener = listener;
    }

    @Nullable
    public OnReadyListener getAuthStateChangeListener() {
        return onReadyListener;
    }

    @SuppressLint("MParticleInitialization")
    public void start(Application application) {
        MParticleOptions options = MParticleOptions
            .builder(application.getApplicationContext())
            .credentials("eu1-f7ebd620020cce4e9672de46c1f443ac", "5xhdPuvut8IbRN4qe3siZw7Rg9RqnF9Ef3-McNLJVDnFEjH9fM1-O38SyYZtgCoC")
            .environment(MParticle.Environment.AutoDetect)
            .dataplan("bitcode_frontend_plan", 4)
            .build();

        MParticle.start(options);
        /*   String mParticleKey = plugin.getConfig().getString("key");
        String mParticleSecret = plugin.getConfig().getString("secret");
        MParticleOptions options = MParticleOptions
            .builder(plugin.getBridge().getContext())
            .credentials(mParticleKey, mParticleSecret)
            .environment(MParticle.Environment.AutoDetect)
            .dataplan("bitcode_frontend_plan", 4)
            .build();
        MParticle.start(options);*/
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
        MParticleUser user = Objects.requireNonNull(MParticle.getInstance()).Identity().getCurrentUser();
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

    public void addGDPRConsentState(final JSONObject consents) throws JSONException, ParseException {
        MParticleUser user = currentUser();
        JSONObject map;
        String purpose;

        if (consents != null) {
            JSONArray keys = consents.names();
            GDPRConsent newConsent;
            ConsentState state;

            for (int i = 0; i < Objects.requireNonNull(keys).length(); i++) {
                String key = null;
                JSONObject value = null;
                try {
                    key = keys.getString(i);
                    value = consents.getJSONObject(key);
                    purpose = key;
                    newConsent = ConvertGDPRConsent(value);
                    state = ConsentState.builder().addGDPRConsentState(purpose, newConsent).build();
                    user.setConsentState(state);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
            Objects
                .requireNonNull(MParticle.getInstance())
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
        Map<String, String> map = new HashMap<String, String>();

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
        Map<MParticle.IdentityType, String> map = new HashMap<MParticle.IdentityType, String>();
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
        Boolean consented = false;
        consented = map.getBoolean("consented");
        Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        String document = "";
        String location = "";
        String hardwareId = "";
        hardwareId = map.getString("hardwareId");

        GDPRConsent consent = GDPRConsent
            .builder(consented)
            .document(document)
            .timestamp(timestamp)
            .location(location)
            .hardwareId(hardwareId)
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
