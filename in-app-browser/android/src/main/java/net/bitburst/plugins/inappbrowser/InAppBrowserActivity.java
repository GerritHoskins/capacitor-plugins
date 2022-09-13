package net.bitburst.plugins.inappbrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class InAppBrowserActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bridge_layout_main);

        //Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent = getIntent();
        String url = intent.getStringExtra(InAppBrowserPlugin.EXTRA_URL);

        webView = (WebView) findViewById(R.id.webview);
        WebViewClient webViewClient = InAppBrowser.iab;
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);
    }
}
