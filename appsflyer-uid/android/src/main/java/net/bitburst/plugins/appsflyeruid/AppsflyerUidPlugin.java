package net.bitburst.plugins.appsflyeruid;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.Objects;

@CapacitorPlugin(name = "AppsflyerUid")
public class AppsflyerUidPlugin extends Plugin {

    private final AppsflyerUid implementation = new AppsflyerUid();

    @PluginMethod
    public void getUID(PluginCall call) {
        JSObject ret = new JSObject();
        String uid = implementation.getUID(getContext());
        if (!Objects.equals(uid, "")) {
            ret.put("uid", uid);
            call.resolve(ret);
            return;
        }

        call.reject("AppsflyerUidPlugin: failed to retrieve uid.");
    }
}
