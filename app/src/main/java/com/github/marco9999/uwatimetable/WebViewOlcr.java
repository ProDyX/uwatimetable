package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewOlcr extends WebView {

	public WebViewOlcr(Context context) {
		super(context);
		setWebViewClient(new WebViewClientOlcr());
		getSettings().setBuiltInZoomControls(true);
		getSettings().setDisplayZoomControls(false);
		getSettings().setJavaScriptEnabled(true);
		addJavascriptInterface(new ProcessHtmlOlcr((MainActivity)context), "HTMLOUT");
	}
	
	// Anderp bug? Without this no virtual keyboard comes up (https://code.google.com/p/android/issues/detail?id=7189)
    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }
    
}

class WebViewClientOlcr extends WebViewClient {
	
	final static String olcrurl = "olcr.uwa.edu.au";
	
	// Stop window from losing focus when url is clicked (only for olcr.uwa.edu.au)
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String URL) {
		if(URL.contains(olcrurl)) {
			view.loadUrl(URL);
			return true;
		}
		return false;
	}
	
}