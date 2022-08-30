package net.bitburst.plugins.appsflyeruid;

import android.content.Context;

import com.appsflyer.AppsFlyerLib;
import com.mparticle.MParticle;

import java.util.Objects;

public class AppsflyerUid {

    public String getUID(Context context) {
        String uid = "";
        if (Objects.requireNonNull(MParticle.getInstance()).isKitActive(MParticle.ServiceProviders.APPSFLYER)) {
            AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
            uid = appsflyer.getAppsFlyerUID(context);
        }
        return uid;
    }
}
