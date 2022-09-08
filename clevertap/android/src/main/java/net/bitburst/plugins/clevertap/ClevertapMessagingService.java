package net.bitburst.plugins.clevertap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.clevertap.android.sdk.CleverTapInstanceConfig;
import com.clevertap.android.sdk.pushnotification.INotificationRenderer;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ClevertapMessagingService extends FirebaseMessagingService implements INotificationRenderer {

    public static final String LOG_TAG = "bitburst.clevertap ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(LOG_TAG, "Message Notification Body: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(LOG_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(LOG_TAG, "Refreshed token: " + token);
        //send the FCM registration token
        //sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Log.d(LOG_TAG, "sendRegistrationToServer: " + token);
        //CleverTapAPI.getDefaultInstance(getActivity().getApplication()).pushFcmRegistrationId(token, true);
    }

    @Nullable
    @Override
    public Object getCollapseKey(Bundle extras) {
        return null;
    }

    @Nullable
    @Override
    public String getMessage(Bundle extras) {
        return null;
    }

    @Nullable
    @Override
    public String getTitle(Bundle extras, Context context) {
        return null;
    }

    @Nullable
    @Override
    public NotificationCompat.Builder renderNotification(
        Bundle extras,
        Context context,
        NotificationCompat.Builder nb,
        CleverTapInstanceConfig config,
        int notificationId
    ) {
        return null;
    }

    @Override
    public void setSmallIcon(int smallIcon, Context context) {}

    @Override
    public String getActionButtonIconKey() {
        return null;
    }
}
