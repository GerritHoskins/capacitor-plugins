package net.bitburst.plugins.adid;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Adid")
public class AdidPlugin extends Plugin {
    private final Adid adid = new Adid(getContext());

    @PluginMethod
    public void getId(PluginCall call) {
        call.setKeepAlive(true);
        JSObject ret = new JSObject();
        try {
            ret.put("id", adid.getAdIdInfo().getId());
            ret.put("limitedAdTracking", adid.getAdIdInfo().isLimitAdTrackingEnabled());
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
