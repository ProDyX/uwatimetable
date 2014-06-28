package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelpFragment extends Fragment 
{
	WebView helpview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate and get web view
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.help_fragment, container, false);
		helpview = (WebView) vg.findViewById(R.id.helpview);
		return vg;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// set title
    	getActivity().getActionBar().setSubtitle(R.string.title_help);
    	
		// load help
		helpview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String URL) {
				return true;
			}
		});
		helpview.getSettings().setBuiltInZoomControls(true);
		helpview.getSettings().setDisplayZoomControls(false);
		helpview.loadUrl("file:///android_asset/help/main.html");
	}
}