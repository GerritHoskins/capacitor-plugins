package net.bitburst.plugins.mparticle;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.Nullable;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.mparticle.MPEvent;
import com.mparticle.MParticle;
import com.mparticle.MParticle.EventType;
import com.mparticle.MParticleOptions;
import com.mparticle.commerce.CommerceEvent;
import com.mparticle.commerce.Product;
import com.mparticle.commerce.TransactionAttributes;
import com.mparticle.consent.ConsentState;
import com.mparticle.consent.GDPRConsent;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.MParticleUser;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

public class Mparticle {

    private static MParticle instance = MParticle.getInstance();
    private MparticlePlugin mPlugin;

    public Mparticle(MparticlePlugin plugin) {
        this.mPlugin = plugin;
        start(plugin.getActivity().getApplication());
    }

    public Mparticle(Application application) {
        start(application);
    }

    @SuppressLint("MParticleInitialization")
    public void start(Application application) {
        String mParticleKey = mPlugin.getConfig().getString("key", "");
        String mParticleSecret = mPlugin.getConfig().getString("secret", "");
        MParticleOptions options = MParticleOptions
            .builder(application.getApplicationContext())
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

    public MParticleUser currentUser() {
        return Objects.requireNonNull(MParticle.getInstance()).Identity().getCurrentUser();
    }

    public boolean setUserAttribute(final String userId, final String name, final JSObject value) {
        long mpid = MparticleHelper.parseString(userId);
        MParticleUser selectedUser = Objects.requireNonNull(MParticle.getInstance()).Identity().getUser(mpid);
        return selectedUser.setUserAttribute(name, value);
    }

    public boolean setUserAttributes(final String userId, final List<Object> attributes) throws JSONException {
        long mpid = MparticleHelper.parseString(userId);
        MParticleUser selectedUser = MParticle.getInstance().Identity().getUser(mpid);
        assert attributes != null;
        Map<String, Object> attributeMap = MparticleHelper.MapObjectList(attributes);
        assert selectedUser != null;
        return selectedUser.setUserAttributes(attributeMap);
    }

    public MPEvent trackEvent(final JSObject data) throws JSONException {
        Map<String, String> customAttributes = MparticleHelper.ConvertStringMap((JSONObject) data);
        String name = data.getString("name");
        EventType eventType = getEventType(data.getInteger("eventType", 8)); //EventType 8:OTHER
        assert name != null;
        assert eventType != null;
        return new MPEvent.Builder(name, eventType).customAttributes(customAttributes).build();
    }

    public CommerceEvent trackPurchaseEvent(final JSObject data) throws JSONException {
        Product product = Mparticle.createMParticleProduct(data);
        List<Product> products = new ArrayList<>();
        products.add(product);

        TransactionAttributes attributes = new TransactionAttributes();
        attributes.setId(Objects.requireNonNull(data.getString("transactionId")));
        attributes.setRevenue(product.getTotalAmount());

        return new CommerceEvent.Builder(Product.PURCHASE, product)
            .products(products)
            .customAttributes(product.getCustomAttributes())
            .transactionAttributes(attributes)
            .build();
    }

    public void addGDPRConsentState(final JSObject consents) throws JSONException, ParseException {
        MParticleUser user = currentUser();
        if (consents != null) {
            GDPRConsent newConsent;
            Iterator<String> iter = consents.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = consents.get(key);
                    newConsent = ConvertGDPRConsent((JSONObject) value);
                    var state = ConsentState.builder().addGDPRConsentState(key, newConsent).build();
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

    public IdentityApiRequest identityRequest(PluginCall call, JSObject data) throws JSONException {
        call.setKeepAlive(true);
        return ConvertIdentityAPIRequest((JSONObject) data);
    }

    private static IdentityApiRequest ConvertIdentityAPIRequest(JSONObject map) throws JSONException {
        IdentityApiRequest.Builder identityRequest = IdentityApiRequest.withEmptyUser();
        Map<MParticle.IdentityType, String> userIdentities = ConvertUserIdentities(map);
        identityRequest.userIdentities(userIdentities);
        return identityRequest.build();
    }

    private static Map<MParticle.IdentityType, String> ConvertUserIdentities(JSONObject jsonObject) throws JSONException {
        Map<MParticle.IdentityType, String> map = new HashMap<>();
        if (jsonObject != null) {
            Map<String, String> stringMap = MparticleHelper.ConvertStringMap(jsonObject);
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                MParticle.IdentityType identity = ConvertIdentityType(entry.getKey());
                String value = entry.getValue();
                map.put(identity, value);
            }
        }
        return map;
    }

    private static GDPRConsent ConvertGDPRConsent(JSONObject consent) throws JSONException {
        boolean consented = false;
        consented = consent.getBoolean("consented");
        Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        String document = "";
        String location = "";
        String hardwareId = "";
        hardwareId = consent.getString("hardwareId");

        GDPRConsent.Builder builder = GDPRConsent.builder(consented);
        builder.document(document);
        builder.timestamp(timestamp);
        builder.location(location);
        builder.hardwareId(hardwareId);
        return builder.build();
    }

    private static Product createMParticleProduct(final JSObject data) throws JSONException {
        Map<String, String> customAttributes = MparticleHelper.ConvertStringMap((JSONObject) data.get("attributes"));
        return new Product.Builder(
            Objects.requireNonNull(data.getString("name")),
            Objects.requireNonNull(data.getString("sku")),
            (double) Objects.requireNonNull(data.getInteger("price"))
        )
            .quantity((double) Objects.requireNonNull(data.getInteger("quantity")))
            .customAttributes(customAttributes)
            .build();
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

    private EventType getEventType(Integer eType) {
        int ord = eType;
        for (EventType e : EventType.values()) {
            if (e.ordinal() == ord) {
                return e;
            }
        }
        return EventType.Other;
    }
}
