package net.bitburst.plugins.adid;

import androidx.annotation.NonNull;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class Adid {
    public static final String LOG_TAG = "adid-plugin ";

    private final Context pluginContext;
    private AdvertisingIdClient.Info adInfo = null;

    public Adid(Context context) {
        this.pluginContext = context;
    }

    public AdvertisingIdClient.Info getAdIdInfo() {
        if (isAvailable("com.google.android.gms.common.GoogleApiAvailabilityLight")) {
            if (ConnectionResult.SUCCESS == GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(pluginContext)) {
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(pluginContext);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, e.getLocalizedMessage());
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    Log.d(LOG_TAG, e.getLocalizedMessage());
                }
            }
        }
        return adInfo;
    }

    private boolean isAvailable(@NonNull String packageName) {
        try {
            Class.forName(packageName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
