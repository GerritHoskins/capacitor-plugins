package net.bitburst.plugins.clevertap;

import android.content.Context;
import com.clevertap.android.sdk.CleverTapAPI;
import com.getcapacitor.JSObject;
import org.json.JSONException;

public class ClevertapChannelManager {

    public static final String FOREGROUND_NOTIFICATION_CHANNEL_ID = "Default";

    private final Context appContext;
    private final String channelId;
    private final CharSequence channelName;
    private final String channelDescription;
    private int channelImportance = CHANNEL_IMPORTANCE;
    private boolean showBadge = true;

    private static final String CHANNEL_ID = "Default";
    private static final String CHANNEL_NAME = "Default";
    private static final String CHANNEL_DESCRIPTION = "Default";
    private static final int CHANNEL_IMPORTANCE = 4; // for SDK compatibility or else use NotificationManager.IMPORTANCE_HIGH;

    public ClevertapChannelManager(Context context, JSObject channel) {
        this.appContext = context;
        this.channelId = channel.getString("id", CHANNEL_ID);
        this.channelName = channel.getString("name", CHANNEL_NAME);
        this.channelDescription = channel.getString("description", CHANNEL_DESCRIPTION);
        try {
            this.channelImportance = channel.getInt("importance");
        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();
        }
        //createForegroundNotificationChannel();
        createChannel();
    }

    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(
                appContext.getApplicationContext(),
                channelId,
                channelName,
                channelDescription,
                channelImportance,
                showBadge
            );
        }
    }
}
