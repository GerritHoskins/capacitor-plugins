package net.bitburst.plugins.clevertap;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.interfaces.OnInitCleverTapIDListener;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;

import java.util.HashMap;

public class Clevertap extends Application implements CTPushNotificationListener {

    public static final String LOG_TAG = "bitburst.clevertap ";
    public CleverTapAPI clevertapAPI;
    public static String clevertapID = null;

    private Application mainApplication;
    private static Boolean ready = false;

    public Clevertap(Application application) {
        super();
        ActivityLifecycleCallback.register(application);
        registerCleverTapActivityLifecycleCallbacks(application);

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);

        this.mainApplication = application;
        this.clevertapAPI = CleverTapAPI.getDefaultInstance(mainApplication);

        if(clevertapAPI != null) {
            clevertapAPI.setCTPushNotificationListener(this);
            clevertapAPI.enableDeviceNetworkInfoReporting(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(application, "default", "default", "default", NotificationManager.IMPORTANCE_MAX, true);
        }
    }

    public void setClevertapId() {
        getDefaultInstance().getCleverTapID(new OnInitCleverTapIDListener() {
            @Override
            public void onInitCleverTapID(final String cleverTapID) {
                // Callback on main thread
                // ClevertapPlugin.setCleverTapId(cleverTapID)
                Log.d(LOG_TAG, "cleverTapID " + cleverTapID);
                clevertapID = cleverTapID;
            }
        });
    }

    public void setClevertapInstance() {
       clevertapAPI = CleverTapAPI.getDefaultInstance(mainApplication);
    }

    public void setPushNotificationListener() {
        getDefaultInstance().setCTPushNotificationListener(this);
    }

    public CleverTapAPI getDefaultInstance(){
        //return CleverTapAPI.getDefaultInstance(mainApplication);
        return clevertapAPI;
    }

    public static String getClevertapId() {
        return clevertapID;
    }

    public static Boolean isReady() {
        return ready;
    }

    public void onUserLogin(HashMap<String, Object> profileUpdate) {
       clevertapAPI.onUserLogin(profileUpdate);
    };

    public void pushEvent(String name, HashMap<String, Object> data) {
        clevertapAPI.pushEvent(name, data);
    };

    public void pushEvent(String name) {
        clevertapAPI.pushEvent(name);
    };

    public void registerCleverTapActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new android.app.Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Log.d(LOG_TAG,  "onActivityCreated " + activity);

                CleverTapAPI.setAppForeground(true);
                setClevertapId();

                try {
                    CleverTapAPI.getDefaultInstance(getApplicationContext()).pushNotificationClickedEvent(activity.getIntent().getExtras());
                } catch (Throwable t) {
                    Log.d(LOG_TAG,  "pushNotificationClickedEvent failed");
                }

                try {
                    Intent intent = activity.getIntent();
                    Uri data = intent.getData();
                    CleverTapAPI.getDefaultInstance(getApplicationContext()).pushDeepLink(data);
                } catch (Throwable t) {
                    Log.d(LOG_TAG,  "pushDeepLink failed");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(LOG_TAG,  "onActivityStarted");
                ready = true;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(LOG_TAG,  "onActivityResumed");
                try {
                    CleverTapAPI.getDefaultInstance(getApplicationContext()).onActivityResumed(activity);
                    ready = true;
                } catch (Throwable t) {
                    Log.d(LOG_TAG,  "onActivityResumed failed");
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(LOG_TAG,  "onActivityResumed");
                ready = false;
                try {
                    CleverTapAPI.getDefaultInstance(getApplicationContext()).onActivityPaused();
                } catch (Throwable t) {
                    Log.d(LOG_TAG,  "onActivityPaused failed");
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ready = false;
                Log.d(LOG_TAG,  "onActivityResumed");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Log.d(LOG_TAG,  "onActivityResumed");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ready = false;
            }
        });
    }

    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
        Log.d(LOG_TAG, "onNotificationClickedPayloadReceived " + String.valueOf(payload));
    }
}
