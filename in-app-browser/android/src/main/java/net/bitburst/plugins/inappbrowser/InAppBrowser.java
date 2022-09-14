package net.bitburst.plugins.inappbrowser;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import java.net.MalformedURLException;
import java.net.URL;

public class InAppBrowser {

    protected InAppBrowserPlugin mPlugin;
    private String targetUrl;
    private PluginCall loadUrlCall;

    public InAppBrowser(InAppBrowserPlugin plugin) {
        mPlugin = plugin;
    }

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    protected void setupWebChromeClient() {
        WebView webView = mPlugin.webView;
        webView.setWebChromeClient(
            new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    JSObject progressValue = new JSObject();
                    progressValue.put("value", progress / 100.0);
                }

                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                    final WebView targetWebView = new WebView(mPlugin.getActivity());
                    targetWebView.setWebViewClient(
                        new WebViewClient() {
                            @Override
                            public void onLoadResource(WebView view, String url) {
                                if (mPlugin.hasEventListeners("navigationHandler")) {
                                    handleNavigation(url, true);
                                    JSObject progressValue = new JSObject();
                                    progressValue.put("value", 0.1);
                                    mPlugin.notifyEventListeners("progress", progressValue);
                                } else {
                                    webView.loadUrl(url);
                                }
                                targetWebView.removeAllViews();
                                targetWebView.destroy();
                            }
                        }
                    );
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(targetWebView);
                    resultMsg.sendToTarget();
                    return true;
                }
            }
        );
    }

    protected void setupWebViewClient(JSObject callData) {
        WebView webView = mPlugin.webView;
        final String javascript = callData.getString("javascript", "");

        webView.setWebViewClient(
            new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    assert javascript != null;
                    if (!javascript.isEmpty()) {
                        webView.evaluateJavascript(javascript, null);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!mPlugin.hidden) {
                        webView.setVisibility(View.VISIBLE);
                    } else {
                        webView.setVisibility(View.INVISIBLE);
                        mPlugin.notifyEventListeners("updateSnapshot", new JSObject());
                    }

                    if (loadUrlCall != null) {
                        loadUrlCall.resolve();
                        loadUrlCall = null;
                    }
                    mPlugin.notifyEventListeners("pageLoaded", new JSObject());
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (mPlugin.hasEventListeners("navigationHandler")) {
                        handleNavigation(url, false);
                        return true;
                    } else {
                        targetUrl = null;
                        return false;
                    }
                }
            }
        );
    }

    protected void setupWebViewLayout(JSObject callData) {
        WebView webView = mPlugin.webView;

        JSObject dimensions = callData.getJSObject("webViewDimensions");
        assert dimensions != null;
        Integer callWidth = dimensions.getInteger("width", 100);
        Integer callHeight = dimensions.getInteger("height", 100);
        Integer callX = dimensions.getInteger("y", 0);
        Integer callY = dimensions.getInteger("x", 0);

        assert callWidth != null;
        assert callHeight != null;
        assert callX != null;
        assert callY != null;

        int width = (int) getPixels(callWidth);
        int height = (int) getPixels(callHeight);

        float x = getPixels(callX);
        float y = getPixels(callY);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );

        webView.setLayoutParams(params);
        params.width = width;
        params.height = height;
        webView.setX(x);
        webView.setY(y);
        webView.requestLayout();

        ((ViewGroup) mPlugin.getBridge().getWebView().getParent()).addView(webView);
    }

    private float getPixels(int value) {
        return value * mPlugin.getContext().getResources().getDisplayMetrics().density + 0.5f;
    }

    private void handleNavigation(String url, Boolean newWindow) {
        targetUrl = url;
        boolean sameHost;
        try {
            URL currentUrl = new URL(mPlugin.webView.getUrl());
            URL targetUrl = new URL(url);
            sameHost = currentUrl.getHost().equals(targetUrl.getHost());

            JSObject navigationHandlerValue = new JSObject();
            navigationHandlerValue.put("url", url);
            navigationHandlerValue.put("newWindow", newWindow);
            navigationHandlerValue.put("sameHost", sameHost);

            mPlugin.notifyEventListeners("navigationHandler", navigationHandlerValue);
        } catch (MalformedURLException ignored) {}
    }
}
