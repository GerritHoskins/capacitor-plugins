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
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;

import java.util.HashMap;
import java.util.Objects;

public class Clevertap extends Application implements CTPushNotificationListener {

    public static final String LOG_TAG = "bitburst.clevertap ";

    public static String clevertapID = null;

    private final Application mainApplication;
    private final CleverTapAPI clevertapAPI;

    public Clevertap(Application application) {
        super();
        ActivityLifecycleCallback.register(application);
        registerCleverTapActivityLifecycleCallbacks(application);

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);

        this.mainApplication = application;
        this.clevertapAPI = CleverTapAPI.getDefaultInstance(mainApplication);

        if (clevertapAPI != null) {
            clevertapAPI.setCTPushNotificationListener(this);
            clevertapAPI.enableDeviceNetworkInfoReporting(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(application, "default", "default", "default", NotificationManager.IMPORTANCE_MAX, true);
        }
    }

    public void setClevertapId() {
        clevertapAPI
            .getCleverTapID(
                cleverTapID -> {
                    // Callback on main thread
                    // ClevertapPlugin.setCleverTapId(cleverTapID)
                    Log.d(LOG_TAG, "cleverTapID " + cleverTapID);
                    clevertapID = cleverTapID;
                }
            );
    }

    public static String getClevertapId() {
        return clevertapID;
    }

    public void setPushNotificationListener() {
        clevertapAPI.setCTPushNotificationListener(this);
    }

    public void onUserLogin(HashMap<String, Object> profileUpdate) {
        clevertapAPI.onUserLogin(profileUpdate);
    }

    public void pushEvent(String name, @Nullable HashMap<String, Object> data) {
        assert data != null;
        if (data.get(name) == null) {
            clevertapAPI.pushEvent(name);
            return;
        }

        clevertapAPI.pushEvent(name, data);
    }

    public void pushNotification(@NonNull Bundle data) {
        clevertapAPI.renderPushNotification(new ClevertapMessagingService(), getApplicationContext(), data);
    }

    public void pushPrivacy(@Nullable HashMap<String, Object> data) {}

    public void pushUser(@NonNull HashMap<String, Object> data) {
        clevertapAPI.pushProfile(data);
    }

    public void registerCleverTapActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(
            new android.app.Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    Log.d(LOG_TAG, "onActivityCreated " + activity);

                    CleverTapAPI.setAppForeground(true);
                    setClevertapId();

                    try {
                        Objects.requireNonNull(CleverTapAPI
                                .getDefaultInstance(activity.getApplicationContext()))
                            .pushNotificationClickedEvent(activity.getIntent().getExtras());
                    } catch (Throwable t) {
                        Log.d(LOG_TAG, "pushNotificationClickedEvent failed");
                    }

                    try {
                        Intent intent = activity.getIntent();
                        Uri data = intent.getData();
                        Objects.requireNonNull(CleverTapAPI.getDefaultInstance(activity.getApplicationContext())).pushDeepLink(data);
                    } catch (Throwable t) {
                        Log.d(LOG_TAG, "pushDeepLink failed");
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    try {
                        CleverTapAPI.onActivityResumed(activity, getClevertapId());
                    } catch (Throwable t) {
                        Log.d(LOG_TAG, "onActivityResumed failed");
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    try {
                        CleverTapAPI.onActivityPaused();
                    } catch (Throwable t) {
                        Log.d(LOG_TAG, "onActivityPaused failed");
                    }
                }

                @Override
                public void onActivityStopped(Activity activity) {
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                }
            }
        );
    }
}
