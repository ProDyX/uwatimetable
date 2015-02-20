package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FHelp extends Fragment
{
	private WebView helpview;
	
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
        ((AMain) getActivity()).getActionBar().setSubtitle(R.string.title_help);

        // lock drawer closed
        AMain mainactivity = (AMain) getActivity();
        mainactivity.drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainactivity.drawertoggle.setDrawerIndicatorEnabled(false);
        mainactivity.getActionBar().setHomeButtonEnabled(false);
        mainactivity.getActionBar().setDisplayHomeAsUpEnabled(false);
    	
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

    @Override
    public void onStop() {
        // unlock drawer
        AMain mainactivity = (AMain) getActivity();
        mainactivity.drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mainactivity.drawertoggle.setDrawerIndicatorEnabled(true);
        mainactivity.getActionBar().setHomeButtonEnabled(true);
        mainactivity.getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onStop();
    }
}