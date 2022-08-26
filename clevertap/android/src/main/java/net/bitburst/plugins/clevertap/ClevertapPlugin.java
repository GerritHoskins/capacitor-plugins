package net.bitburst.plugins.clevertap;

import android.app.NotificationManager;
import android.content.Intent;

import android.util.Log;
import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;

@CapacitorPlugin(name = "Clevertap", permissions = @Permission(strings = {}, alias = "receive"))
public class ClevertapPlugin extends Plugin {
    public static final String LOG_TAG = "bitburst.clevertap ";

    public static Bridge staticBridge = null;
    public static RemoteMessage lastMessage = null;

    public NotificationManager notificationManager;
    public ClevertapMessagingService firebaseMessagingService;


    public Clevertap clevertap;

    public void load() {
       // firebaseMessagingService = new ClevertapMessagingService();
        clevertap = new Clevertap(getActivity().getApplication());
    }

    @Override
    protected void handleOnNewIntent(Intent intent) {
        Log.d(LOG_TAG,  "handleOnNewIntent " + intent.getAction());
    }

    @PluginMethod
    public void isReady(PluginCall call) {
        call.resolve(new JSObject().put("ready", Clevertap.isReady()));
    }

    @PluginMethod
    public void getClevertapId(PluginCall call) {
       call.resolve(new JSObject().put("id",  Clevertap.getClevertapId()));
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

        if(pluginData.getJSObject("value") == null) {
            clevertap.pushEvent(pluginData.getString("name"));
            return;
        }

        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(pluginData.getString("name"), 1234);

        clevertap.pushEvent("Test event", prodViewedAction);
    }

    @PluginMethod
    public void registerFBM(PluginCall call) {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging
                .getInstance()
                .getToken()
                .addOnCompleteListener(
                        task -> {
                            if (!task.isSuccessful()) {
                                Log.d(LOG_TAG,  task.getException().getLocalizedMessage());
                                return;
                            }
                           task.getResult();
                        }
                );
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
        call.resolve();
    }

    protected void handleOnStart() {
        Log.d(LOG_TAG,  "handleOnStart ");
    }

    protected void handleOnRestart() {
        Log.d(LOG_TAG,  "handleOnRestart ");
    }

    protected void handleOnResume() {
        Log.d(LOG_TAG,  "handleOnResume ");
    }
}
