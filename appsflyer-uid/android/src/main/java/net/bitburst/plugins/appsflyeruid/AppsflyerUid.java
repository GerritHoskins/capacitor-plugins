package net.bitburst.plugins.appsflyeruid;

import android.util.Log;

public class AppsflyerUid {

    public String getUID(String uid) {
        if (MParticle.getInstance().isKitActive(ServiceProviders.APPSFLYER)) {
             return MParticle.getInstance().appsflyeruid;
        }
        return '';
    }
}
