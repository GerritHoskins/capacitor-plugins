package net.bitburst.plugins.clevertap;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import com.clevertap.android.sdk.CleverTapAPI;
import com.getcapacitor.*;

public class ClevertapChannelManager {

    public static final String FOREGROUND_NOTIFICATION_CHANNEL_ID = "Default";

    private Context context;
    private NotificationManager notificationManager;
    private PluginConfig config;

    public ClevertapChannelManager(Context context, NotificationManager manager, PluginConfig config) {
        this.context = context;
        this.notificationManager = manager;
        this.config = config;
        //createForegroundNotificationChannel();
    }

    private static String CHANNEL_ID = "Default";
    private static String CHANNEL_NAME = "Default";
    private static String CHANNEL_DESCRIPTION = "Default";

    public void createChannel(PluginCall call) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(
                context.getApplicationContext(),
                CHANNEL_ID,
                CHANNEL_NAME,
                CHANNEL_DESCRIPTION,
                NotificationManager.IMPORTANCE_HIGH,
                false
            );
        }
        call.resolve();
    }

    public void listChannels(PluginCall call) {
        call.resolve();
    }
}
