package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled") // url is a trusted website (UWA OLCR) and browsing outside the domain will cause a seperate launch window to open
class WrWebViewOlcr extends WebView {

	@SuppressLint("AddJavascriptInterface")
    public WrWebViewOlcr(Context context) {
		super(context);
		setWebViewClient(new WrWebViewClientOlcr());
		getSettings().setBuiltInZoomControls(true);
		getSettings().setDisplayZoomControls(false);
		getSettings().setJavaScriptEnabled(true);
		addJavascriptInterface(new HClassesWebViewOlcrImporter((AMain)context), "HTMLOUT");
	}
	
	// Anderp bug? Without this no virtual keyboard comes up (https://code.google.com/p/android/issues/detail?id=7189)
    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }
    
}

class WrWebViewClientOlcr extends WebViewClient {
	
	private final static String olcrurl = "olcr.uwa.edu.au";
	
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