package com.goodow.android;

import org.apache.cordova.DroidGap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class App extends DroidGap {
	// monitor platform changes
	private IntentFilter mNetworkStateChangedFilter;
	private BroadcastReceiver mNetworkStateIntentReceiver;
	//default assign cache size 8MB
	private final int CACHE_SIZE = 1024 * 1024 * 5;

	@Override 
	public void onCreate(final Bundle arg0) {
		super.onCreate(arg0);
		
		//setting the cache path
		String cachePath = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath(); 
		
		this.loadUrlTimeoutValue = 0;
		super.loadUrl("http://dev.goodow.com/"); 
		
		WebView view = this.appView;
		view.getSettings().setAppCacheEnabled(true); 
		view.getSettings().setAppCacheMaxSize(CACHE_SIZE);
		view.getSettings().setAppCachePath(cachePath);
		view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
//		WebView view = new WebView(this);
//		WebSettings settings = view.getSettings(); 

		view.getSettings().setJavaScriptEnabled(true);
		view.getSettings().setAppCacheEnabled(true);
//		
//		WebChromeClient chromeClient = new WebChromeClient();
//		view.setWebChromeClient(new WebChromeClient());
//		view.setWebViewClient(new WebViewClient());  
//		view.setNetworkAvailable(true); 
//
		registratNetworkChanges(view); 
//
//		view.loadUrl("file:///android_asset/www/NewFile.html");
//		this.setContentView(view);
	}

	private void registratNetworkChanges(final WebView view) {
		/*
		 * enables registration for changes in network status from http stack
		 */
		mNetworkStateChangedFilter = new IntentFilter();
		mNetworkStateChangedFilter
				.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mNetworkStateIntentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						ConnectivityManager.CONNECTIVITY_ACTION)) {
					boolean down = intent.getBooleanExtra(
							ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
					view.setNetworkAvailable(!down);
				}
			}
		};

		registerReceiver(mNetworkStateIntentReceiver,
				mNetworkStateChangedFilter);
	}
}
