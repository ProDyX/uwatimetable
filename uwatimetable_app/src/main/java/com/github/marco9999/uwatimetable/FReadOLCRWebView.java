package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FReadOLCRWebView extends Fragment implements OnClickListener {

	private static final String olcrurl = "https://server1.olcr.uwa.edu.au/olcrstudent/";

    private static final String BTN_TAG_CANCEL = "cancel";
    private static final String BTN_TAG_PROCESS = "process";

	private AMain mainactivity;
	private ViewGroup olcr;
	private WrWebViewOlcr olcrwebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflate menu items
		inflater.inflate(R.menu.read_olcr, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_back:
			if(olcrwebview.canGoBack()) olcrwebview.goBack();
			return true;
		}
		return false;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final ViewGroup rootvg = (ViewGroup) inflater.inflate(R.layout.read_olcr_fragment, container, false);
    	
    	// get webview container
    	olcr = (ViewGroup) rootvg.findViewById(R.id.olcr_webview_container);
    	
    	// set button listeners
        Button cancel = (Button) rootvg.findViewById(R.id.olcr_cancel);
    	cancel.setTag(BTN_TAG_CANCEL);
        Button process = (Button) rootvg.findViewById(R.id.olcr_process);
    	process.setTag(BTN_TAG_PROCESS);
    	
    	cancel.setOnClickListener(this);
    	process.setOnClickListener(this);
    	
    	return rootvg;
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
        // set callback/context
        mainactivity = (AMain) getActivity();
		
		// load page and view
    	olcrwebview = new WrWebViewOlcr(mainactivity);
		olcr.addView(olcrwebview);
	}
	
    @Override
    public void onResume() {
    	super.onResume();
    	
    	// set title
    	mainactivity.getActionBar().setSubtitle(R.string.title_read_olcr);
    	
    	// show menu items
    	setMenuVisibility(true);
    	
		// load page
    	olcrwebview.loadUrl(olcrurl);
    }
    
	@Override
	public void onStop() {
		// hide menu items
		setMenuVisibility(false);
		
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if(tag.equals(BTN_TAG_CANCEL)) {
	        // pop this fragment
			mainactivity.getFragmentManager().popBackStack();
		} else if (tag.equals(BTN_TAG_PROCESS)) {
			// Html source grab when someone closes the window
	        olcrwebview.loadUrl("javascript:window.HTMLOUT.processHTML(document.URL, '<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
	        
	        // set first time use to false
	        mainactivity.uisharedpref.edit().putBoolean(Key.FIRSTTIMEUSE, false).commit();
	        
	        // pop this fragment
	        mainactivity.getFragmentManager().popBackStack();
		}
		// Clear all cookies
        android.webkit.CookieManager.getInstance().removeAllCookies(null);
	}
    
}
