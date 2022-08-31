package net.bitburst.plugins.appsflyeruid;

import android.content.Context;
import com.appsflyer.AppsFlyerLib;

public class AppsflyerUid {

    public String getUID(Context context) {
        String uid = "";
        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
        if (appsflyer != null) {
            uid = appsflyer.getAppsFlyerUID(context);
        }
        return uid;
    }
}
