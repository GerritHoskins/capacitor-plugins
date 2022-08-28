package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.mparticle.MParticle;
import com.mparticle.MParticleOptions;
import com.mparticle.MParticle.EventType;
import com.mparticle.MParticle.ServiceProviders;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.commerce.Product;
import com.mparticle.commerce.CommerceEvent;
import com.mparticle.commerce.CommerceEvent.Builder;
import com.mparticle.commerce.TransactionAttributes;
import com.mparticle.identity.IdentityApiResult;
import com.mparticle.identity.TaskSuccessListener;
import com.mparticle.*;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "MparticlePlugin")
public class MparticlePlugin extends Plugin {

    private MparticlePlugin implementation = new MparticlePlugin();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void mParticleInit(PluginCall call) {
        // String key = call.getString("key");
        // String secret = call.getString("secret");
        // MparticleOptions options = MparticleOptions.builder(this.getContext())
        //         .credentials(
        //             key,
        //             secret
        //             )
        //         .environment(MParticle.Environment.Development)
        //         .logLevel(Mparticle.LogLevel.DEBUG)
        //         .build();
        // Mparticle.start(options);
        // call.resolve(new JSObject());
    }

    @PluginMethod
    public void logMparticleEvent(PluginCall call) {
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
                    // Something went wrong!
                }
            }
        }
        String name = call.getString("eventName");
        int type = call.getInt("eventType");

        MPEvent event = new MPEvent.Builder(name, implementation.getEventType(type))
            .customAttributes(customAttributes)
            .build();

        Mparticle.getInstance().logEvent(event);
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void logMparticlePageView(PluginCall call) {
        String name = call.getString("pageName");
        String link = call.getString("pageLink");
        Map<String, String> screenInfo = new HashMap<String, String>();
        screenInfo.put("page", link);
        Mparticle.getInstance().logScreen(name, screenInfo );
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void setUserAttribute(PluginCall call) {
        String name = call.getString("attributeName");
        String value = call.getString("attributeValue");
        if (implementation.currentUser() != null) {
            implementation.currentUser().setUserAttribute(name,value);
        }
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void setUserAttributeList(PluginCall call) {
        String name = call.getString("attributeName");
        JSArray list_tmp = call.getArray("attributeValues");
        List<String> attributeList = new ArrayList<>();
        try {
            attributeList = list_tmp.toList();
        } catch (JSONException e) {}
        if (implementation.currentUser() != null) {
            implementation.currentUser().setUserAttributeList(name,attributeList);
        }
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void loginMparticleUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle.getInstance().Identity().login(implementation.identityRequest(email,customerId));
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void logoutMparticleUser(PluginCall call) {
        Mparticle.getInstance().Identity().logout(IdentityApiRequest.withEmptyUser().build());
        call.resolve(new JSObject());
    }

    @PluginMethod
    public void registerMparticleUser(PluginCall call) {
        String email = call.getString("email");
        String customerId = call.getString("customerId");
        Mparticle.getInstance().Identity().login(implementation.identityRequest(email,customerId))
        .addSuccessListener(new TaskSuccessListener() {
            public void onSuccess(IdentityApiResult result) {
                //proceed with login
                JSObject temp = call.getObject("userAttributes");
                if (temp != null) {
                    Iterator<String> iter = temp.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            Object value = temp.get(key);
                            result.getUser().setUserAttribute(key,value);
                            } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                }
            }
        });;
        call.resolve(new JSObject());
    }
}
