package net.bitburst.plugins.clevertap;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ClevertapMessagingService extends FirebaseMessagingService {

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
}
