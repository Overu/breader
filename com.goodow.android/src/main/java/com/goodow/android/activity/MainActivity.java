package com.goodow.android.activity;

import com.goodow.android.persist.Persistor;
import com.google.inject.Inject;

import com.retech.reader.chassis.R;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import roboguice.activity.RoboActivity;

public class MainActivity extends RoboActivity {

  @Inject
  Persistor persistor;

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    WebView webView = (WebView) findViewById(R.id.webview);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webView.setWebChromeClient(new WebChromeClient());
    webView.addJavascriptInterface(persistor, "Persistor");
    webView
        .loadUrl("http://dev.retechcorp.com/svn/retech/com.retech.reader/trunk/com.goodow.android/dev/test.html");
  }

}
