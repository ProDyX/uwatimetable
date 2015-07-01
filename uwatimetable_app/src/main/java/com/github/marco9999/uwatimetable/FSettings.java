package com.github.marco9999.uwatimetable;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

public class FSettings extends PreferenceFragment implements OnPreferenceClickListener {
	
	private AMain mainactivity;
	
	private static final String p_action_read_from_webview_olcr = "action_read_from_webview_olcr";
	private static final String p_action_read_from_https_olcr = "action_read_from_https_olcr";
	private static final String p_action_read_from_file = "action_read_from_file";
	private static final String p_action_manual_entry = "action_manual_entry";
	private static final String p_action_delete_database = "action_delete_database";
	private static final String p_action_delete_selected_entries = "action_delete_selected_entries";
	private static final String p_action_test_entry = "action_test_entry";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_fragment);
        
        // add callback to main activity
        mainactivity = (AMain) getActivity();
        
        // add listener to preferences
        this.findPreference(p_action_read_from_webview_olcr).setOnPreferenceClickListener(this);
		this.findPreference(p_action_read_from_https_olcr).setOnPreferenceClickListener(this);
		this.findPreference(p_action_read_from_file).setOnPreferenceClickListener(this);
        this.findPreference(p_action_manual_entry).setOnPreferenceClickListener(this);
        this.findPreference(p_action_delete_database).setOnPreferenceClickListener(this);
        this.findPreference(p_action_delete_selected_entries).setOnPreferenceClickListener(this);
        this.findPreference(p_action_test_entry).setOnPreferenceClickListener(this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	// set title
    	mainactivity.getActionBar().setSubtitle(R.string.title_settings);

        // lock drawer closed
        mainactivity.drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainactivity.drawertoggle.setDrawerIndicatorEnabled(false);
        mainactivity.getActionBar().setHomeButtonEnabled(false);
        mainactivity.getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onStop() {
        // unlock drawer
        mainactivity.drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mainactivity.drawertoggle.setDrawerIndicatorEnabled(true);
        mainactivity.getActionBar().setHomeButtonEnabled(true);
        mainactivity.getActionBar().setDisplayHomeAsUpEnabled(true);

        super.onStop();
    }
    
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if (key.equals(p_action_read_from_webview_olcr)) {
			readfromwebviewolcrActionEvent();
		} else if (key.equals(p_action_read_from_https_olcr)) {
			readfromhttpsolcrActionEvent();
		} else if (key.equals(p_action_read_from_file)) {
			readfromfileActionEvent();
		} else if (key.equals(p_action_manual_entry)) {
			manualentryActionEvent();
		} else if (key.equals(p_action_delete_database)) {
			deletedbActionEvent();
		} else if (key.equals(p_action_delete_selected_entries)) {
			deleteselectedActionEvent();
		} else if (key.equals(p_action_test_entry)) {
			testentryActionEvent();
		}
		return false;
	}

	void readfromhttpsolcrActionEvent() {
        DHttpsOLCRActionEvent.newInstance(mainactivity).show(mainactivity.getFragmentManager(), null);
	}

	void readfromwebviewolcrActionEvent() {
		// launch read from webview olcr fragment
		if(getFragmentManager().findFragmentByTag(Tag.FRAGMENT_WEBVIEW_OLCR) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new FReadOLCRWebView())
				.commit();
		}
	}
	
	void deletedbActionEvent() {
        DDeleteDbActionEvent.newInstance(mainactivity).show(mainactivity.getFragmentManager(), null);
	}
	
	void deleteselectedActionEvent() {
		// launch delete entry fragment
		if(getFragmentManager().findFragmentByTag(Tag.FRAGMENT_DELETE_ENTRY) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new FDeleteEntry())
				.commit();
		}
	}
	
	void testentryActionEvent() {
		if(mainactivity.dbhelperui.writeClassToDB(new ContentValues[] {HStatic.createTestEntry()})) {
			// set first time use to false
	        mainactivity.uisharedpref.edit().putBoolean(Key.FIRSTTIMEUSE, false).commit();
			
			// display toast notifying success.
			String toastmsg = "Created a test entry.";
			Toast.makeText(this.getActivity(), toastmsg, Toast.LENGTH_LONG).show();
		} else {
			// failure.
			String toastmsg = "Failed to create test entry!";
			Toast.makeText(this.getActivity(), toastmsg, Toast.LENGTH_LONG).show();
		}
	}

	void readfromfileActionEvent() {
		// See the EClassesFileAsyncTask class for more details.
		new HClassesFileOLCRImporter(mainactivity, mainactivity.dbhelperui).read();
		
		// set first time use to false
        mainactivity.uisharedpref.edit().putBoolean(Key.FIRSTTIMEUSE, false).commit();
		
	}
	
	void manualentryActionEvent() {
		// launch manual entry fragment
		if(getFragmentManager().findFragmentByTag(Tag.FRAGMENT_MANUAL_ENTRY) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new FManualEntry())
				.commit();
		}
	}

}
