package net.bitburst.plugins.inappbrowser;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.getcapacitor.Bridge;
import com.getcapacitor.BridgeWebViewClient;

public class InAppBrowser extends BridgeWebViewClient {

    public static InAppBrowser iab;

    public InAppBrowser(Bridge bridge) {
        super(bridge);
        iab = this;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }
}
