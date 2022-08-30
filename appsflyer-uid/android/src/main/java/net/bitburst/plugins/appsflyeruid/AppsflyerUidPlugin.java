package net.bitburst.plugins.appsflyeruid;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "AppsflyerUid")
public class AppsflyerUidPlugin extends Plugin {

    private AppsflyerUid implementation = new AppsflyerUid();

    @PluginMethod
    public void getUID(PluginCall call) {
        String uid = call.getString("uid");

        JSObject ret = new JSObject();
        ret.put("uid", implementation.getUID(uid));
        call.resolve(ret);
    }
}