package net.bitburst.plugins.clevertap;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import org.json.JSONException;

@CapacitorPlugin(name = "Clevertap", permissions = @Permission(strings = {}, alias = "receive"))
public class ClevertapPlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.clevertap ";

    public static Bridge staticBridge = null;
    public static RemoteMessage lastMessage = null;

    public NotificationManager notificationManager;
    public ClevertapMessagingService firebaseMessagingService;

    public Clevertap clevertap;

    public void load() {
        super.load();
        // firebaseMessagingService = new ClevertapMessagingService();
        clevertap = new Clevertap(getActivity().getApplication());
    }

    @Override
    protected void handleOnNewIntent(Intent intent) {
        Log.d(LOG_TAG, "handleOnNewIntent " + intent.getAction());
    }

    @PluginMethod
    public void init(PluginCall call) {
        call.setKeepAlive(true);
        if (!clevertap.isReady()) {
            Log.d(LOG_TAG, " clevertap instance is not ready yet.");
        } else {
            Log.d(LOG_TAG, " clevertap instance is ready!");
            call.resolve();
            call.setKeepAlive(false);
        }
    }

    @PluginMethod
    public void cleverTap(PluginCall call) {
        call.unimplemented();
    }

    @PluginMethod
    public void getClevertapId(PluginCall call) {
        call.resolve(new JSObject().put("id", Clevertap.getClevertapId()));
    }

    @PluginMethod
    public void onUserLogin(PluginCall call) {
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Identity", call.getString("uid"));
        profileUpdate.put("Email", call.getString("email"));
        profileUpdate.put("MSG-push", true);

        HashMap<String, String> additionalIdentifiers = new HashMap<String, String>();
        additionalIdentifiers.put("Internal-Id", call.getString("internalId"));
        additionalIdentifiers.put("Client-Product", call.getString("clientProduct"));

        profileUpdate.put("Additional-Identifiers", additionalIdentifiers);

        clevertap.onUserLogin(profileUpdate);
        call.resolve();
    }

    @PluginMethod
    public void pushEvent(PluginCall call) {
        JSObject pluginData = call.getData();

        HashMap<String, Object> events = new HashMap<String, Object>();
        String evtName = pluginData.getString("evtName");
        try {
            events.put(evtName, pluginData.getJSObject("evtNameOrData", new JSObject()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        clevertap.pushEvent(evtName, events);
        call.resolve();
    }

    @PluginMethod
    public void pushNotification(PluginCall call) {
        JSObject data = call.getObject("notificationData");

        Bundle notificationBundle = ClevertapHelper.convertJSObjectToBundle(data);

        clevertap.pushNotification(notificationBundle);
        call.resolve();
    }

    @PluginMethod
    public void pushPrivacy(PluginCall call) {
        JSObject privacyData = call.getObject("privacyData");
        clevertap.pushPrivacy(null);
        call.unimplemented();
    }

    @PluginMethod
    public void pushUser(PluginCall call) {
        JSObject profileData = call.getObject("profileData");

        HashMap<String, Object> data = ClevertapHelper.convertJSObjectToHashMap(profileData);

        clevertap.pushUser(data);
        call.resolve();
    }

    @PluginMethod
    public void registerFBM(PluginCall call) {
        call.unimplemented();
        /*FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging
            .getInstance()
            .getToken()
            .addOnCompleteListener(
                task -> {
                    if (!task.isSuccessful()) {
                        Log.d(LOG_TAG, task.getException().getLocalizedMessage());
                        return;
                    }
                    task.getResult();
                }
            );*/
        call.resolve();
    }

    @PluginMethod
    public void getDeliveredNotifications(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void removeDeliveredNotifications(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void createChannel(PluginCall call) {
        JSObject channelSettings = call.getObject("channel");
        new ClevertapChannelManager(getBridge().getActivity().getApplicationContext(), channelSettings);
    }
}
