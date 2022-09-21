package net.bitburst.plugins.adid;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.huawei.hms.ads.identifier.AdvertisingIdClient;
import com.huawei.hms.api.HuaweiApiAvailability;

public class Adid {

    public int getId() {
        int advertisingId = 0;

        if (isAvailable("com.google.android.gms.common.GoogleApiAvailabilityLight")) {
            int result = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context);
            if (ConnectionResult.SUCCESS == result) {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
                if (null != info) advertisingId = info.getId();
            }
        }

        if (isAvailable("com.huawei.hms.api.HuaweiApiAvailability")) {
            int result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context);
            if (ConnectionResult.SUCCESS == result) {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
                if (null != info) advertisingId = info.getId();
            }
        }

        return advertisingId;
    }

    private boolean isAvailable(@NonNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
