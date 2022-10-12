package com.freestar.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.freestar.android.ads.FreeStarAds;

import androidx.annotation.Nullable;

public class WebviewActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FreeStarAds.init(this, "XqjhRR");
        setContentView(R.layout.webview_layout);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("https://www.google.com/");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
