package net.bitburst.plugins.adid;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

@CapacitorPlugin(name = "Adid")
public class AdidPlugin extends Plugin {
    private Adid adid;

    @Override
    public void load() {
        adid  = new Adid(getContext());
        super.load();
    }

    @PluginMethod
    public void getId(PluginCall call) {
        call.setKeepAlive(true);
        JSObject ret = new JSObject();
        try {
            AdvertisingIdClient.Info adInfo = adid.getAdIdInfo();
            ret.put("id", adInfo.getId());
            ret.put("limitedAdTracking", adInfo.isLimitAdTrackingEnabled());
        } catch (Exception e) {
            call.reject(Adid.LOG_TAG, "error getting advertising id.", e);
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void getStatus(PluginCall call) {
        call.unimplemented();
    }
}
