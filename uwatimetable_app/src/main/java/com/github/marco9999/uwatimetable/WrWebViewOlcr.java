package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**  uwatimetable/WrWebViewOlcr: Wrapper class for a WebView which contains some settings and an interface to the HTMLOUT function.
 *    Copyright (C) 2015 Marco Satti
 *    Contact: marcosatti@gmail.com
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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