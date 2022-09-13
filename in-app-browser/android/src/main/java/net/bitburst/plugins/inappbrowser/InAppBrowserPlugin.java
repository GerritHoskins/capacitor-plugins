package net.bitburst.plugins.inappbrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;

@CapacitorPlugin(name = "InAppBrowser")
public class InAppBrowserPlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.inAppBrowser ";
    public static final String EXTRA_URL = "extra_url";

    private WebView webView;
    private boolean hidden = false;
    private String targetUrl;
    private PluginCall loadUrlCall;
    private int width;
    private int height;
    private float x;
    private float y;

    @PluginMethod
    public void echo(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void open(final PluginCall call) {
        getActivity()
            .runOnUiThread(
                new Runnable() {
                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void run() {
                        hidden = false;
                        webView = new WebView(getContext());
                        WebSettings settings = webView.getSettings();
                        settings.setAllowContentAccess(true);
                        settings.setJavaScriptEnabled(true);
                        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                        settings.setDomStorageEnabled(true);
                        settings.setSupportMultipleWindows(true);

                        // Temp fix until this setting is on by default
                        bridge.getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                        JSObject callData = call.getData();
                        assert callData != null;

                        final String javascript = callData.getString("javascript", "");
                        final int injectionTime = 0;

                        webView.setWebChromeClient(
                            new WebChromeClient() {
                                @Override
                                public void onProgressChanged(WebView view, int progress) {
                                    JSObject progressValue = new JSObject();
                                    progressValue.put("value", progress / 100.0);
                                    notifyListeners("progress", progressValue);
                                }

                                @Override
                                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                                    final WebView targetWebView = new WebView(getActivity());
                                    targetWebView.setWebViewClient(
                                        new WebViewClient() {
                                            @Override
                                            public void onLoadResource(WebView view, String url) {
                                                if (hasListeners("navigationHandler")) {
                                                    handleNavigation(url, true);
                                                    JSObject progressValue = new JSObject();
                                                    progressValue.put("value", 0.1);
                                                    notifyListeners("progress", progressValue);
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
                                    if (webView != null) {
                                        if (!hidden) {
                                            webView.setVisibility(View.VISIBLE);
                                        } else {
                                            webView.setVisibility(View.INVISIBLE);
                                            notifyListeners("updateSnapshot", new JSObject());
                                        }
                                    }

                                    if (loadUrlCall != null) {
                                        loadUrlCall.resolve();
                                        loadUrlCall = null;
                                    }
                                    notifyListeners("pageLoaded", new JSObject());
                                }

                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    if (hasListeners("navigationHandler")) {
                                        handleNavigation(url, false);
                                        return true;
                                    } else {
                                        targetUrl = null;
                                        return false;
                                    }
                                }
                            }
                        );

                        //webView.setVisibility(View.INVISIBLE);
                        String urlString = callData.getString("url", "");
                        Integer callWidth = callData.getInteger("width", 100);
                        Integer callHeight = callData.getInteger("height", 100);
                        Integer callX = callData.getInteger("y", 0);
                        Integer callY = callData.getInteger("x", 0);

                        assert urlString != null;
                        assert callWidth != null;
                        assert callHeight != null;
                        assert callX != null;
                        assert callY != null;

                        if (urlString.isEmpty()) {
                            call.reject(LOG_TAG, "url is missing");
                            return;
                        }

                        width = (int) getPixels(callWidth);
                        height = (int) getPixels(callHeight);

                        x = getPixels(callX);
                        y = getPixels(callY);

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

                        ((ViewGroup) getBridge().getWebView().getParent()).addView(webView);

                        webView.loadUrl(urlString);
                        call.resolve();
                    }
                }
            );
    }

    private float getPixels(int value) {
        return value * getContext().getResources().getDisplayMetrics().density + 0.5f;
    }

    private void handleNavigation(String url, Boolean newWindow) {
        targetUrl = url;
        boolean sameHost;
        try {
            URL currentUrl = new URL(webView.getUrl());
            URL targetUrl = new URL(url);
            sameHost = currentUrl.getHost().equals(targetUrl.getHost());

            JSObject navigationHandlerValue = new JSObject();
            navigationHandlerValue.put("url", url);
            navigationHandlerValue.put("newWindow", newWindow);
            navigationHandlerValue.put("sameHost", sameHost);

            notifyListeners("navigationHandler", navigationHandlerValue);
        } catch (MalformedURLException ignored) {}
    }

    @PluginMethod
    public void close(final PluginCall call) {
        getActivity()
            .runOnUiThread(
                () -> {
                    if (webView != null) {
                        ViewGroup rootGroup = ((ViewGroup) getBridge().getWebView().getParent());
                        int count = rootGroup.getChildCount();
                        if (count > 1) {
                            rootGroup.removeView(webView);
                            webView = null;
                        }
                        hidden = false;
                    }
                    call.resolve();
                }
            );
    }

    @PluginMethod
    public void show(final PluginCall call) {
        getActivity()
            .runOnUiThread(
                () -> {
                    hidden = false;
                    if (webView != null) {
                        webView.setVisibility(View.VISIBLE);
                    }
                    call.resolve();
                }
            );
    }

    @PluginMethod
    public void hide(final PluginCall call) {
        getActivity()
            .runOnUiThread(
                () -> {
                    hidden = true;
                    if (webView != null) {
                        webView.setVisibility(View.INVISIBLE);
                    }
                    call.resolve();
                }
            );
    }

    @PluginMethod
    public void openBasicWebView(PluginCall call) {
        String urlString = call.getString("url");
        if (urlString == null) {
            call.reject(LOG_TAG, "missing option: url");
            return;
        }
        if (urlString.isEmpty()) {
            call.reject(LOG_TAG, "url is empty");
            return;
        }

        Uri url;
        try {
            url = Uri.parse(urlString);
        } catch (Exception ex) {
            call.reject(LOG_TAG, ex.getLocalizedMessage());
            return;
        }

        Intent intent = new Intent();
        intent.setClass(getContext(), InAppBrowserActivity.class);
        intent.putExtra(EXTRA_URL, url);
        // getActivity().startActivity(intent);
        startActivityForResult(call, intent, "openIabBrowserResult");
        call.resolve();
    }

    @ActivityCallback
    public void openIabBrowserResult(PluginCall call, ActivityResult result) {
        Log.d(LOG_TAG, "result code: " + result.getResultCode());
        call.resolve();
    }
}
