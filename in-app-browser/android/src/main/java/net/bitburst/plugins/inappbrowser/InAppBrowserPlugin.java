package net.bitburst.plugins.inappbrowser;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "InAppBrowser")
public class InAppBrowserPlugin extends Plugin {

    public static final String EXTRA_URL = "extra_url";

    private InAppBrowser implementation = new InAppBrowser();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void open(PluginCall call) {
        String url = call.getString("url");

        Intent intent = new Intent();
        intent.setClass(getContext(), InAppBrowserActivity.class);
        intent.putExtra(EXTRA_URL, url);
        getActivity().startActivity(intent);
        call.resolve();
    }
}
