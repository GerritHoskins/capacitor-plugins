package net.bitburst.plugins.inappbrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "InAppBrowser")
public class InAppBrowserPlugin extends Plugin {

    public static final String LOG_TAG = "bitburst.inAppBrowser ";
    public static final String EXTRA_URL = "extra_url";

    private final InAppBrowser inAppBrowser = new InAppBrowser(this);
    protected WebView webView;
    protected boolean hidden = false;

    @PluginMethod
    public void echo(PluginCall call) {
        call.resolve();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @PluginMethod
    public void open(final PluginCall call) {
        getActivity()
            .runOnUiThread(
                () -> {
                    JSObject callData = call.getData();
                    assert callData != null;

                    String urlString = callData.getString("url", "");
                    assert urlString != null;
                    if (urlString.isEmpty()) {
                        call.reject(LOG_TAG, "url is missing");
                        return;
                    }

                    hidden = false;

                    webView = new WebView(getContext());
                    WebSettings settings = webView.getSettings();
                    settings.setAllowContentAccess(true);
                    settings.setJavaScriptEnabled(true);
                    settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    settings.setDomStorageEnabled(true);
                    settings.setSupportMultipleWindows(true);
                    // setting is off by default
                    bridge.getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                    inAppBrowser.setupWebChromeClient();
                    inAppBrowser.setupWebViewClient(callData);
                    //webView.setVisibility(View.INVISIBLE);
                    inAppBrowser.setupWebViewLayout(callData);

                    webView.loadUrl(urlString);
                    call.resolve();
                }
            );
    }

    public boolean hasEventListeners(@NonNull String eventName) {
        return hasListeners(eventName);
    }

    public void notifyEventListeners(@NonNull String eventName, @Nullable JSObject eventValue) {
        notifyListeners(eventName, eventValue);
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
