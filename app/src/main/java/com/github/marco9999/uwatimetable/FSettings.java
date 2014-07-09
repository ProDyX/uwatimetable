package com.github.marco9999.uwatimetable;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class FSettings extends PreferenceFragment implements OnPreferenceClickListener {
	
	private static final String ERRTAG = "uwatimetable";
	private static final String KEY_FIRSTTIMEUSE = "FIRSTTIMEUSE";
	private static final String TAG_FRAGMENT_MANUAL_ENTRY = "manual_entry";
	private static final String TAG_FRAGMENT_DELETE_ENTRY = "delete_entry";
	private static final String TAG_FRAGMENT_READ_OLCR = "read_olcr";
	
	private AMain mainactivity;
	
	private static final String k_action_read_from_olcr = "action_read_from_olcr";
	private static final String k_action_read_from_file = "action_read_from_file";
	private static final String k_action_manual_entry = "action_manual_entry";
	private static final String k_action_delete_database = "action_delete_database";
	private static final String k_action_delete_selected_entries = "action_delete_selected_entries";
	private static final String k_action_test_entry = "action_test_entry";
	private static final String k_option_display_id = "option_display_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_fragment);
        
        // add callback to main activity
        mainactivity = (AMain) getActivity();
        
        // add listener to preferences
        this.findPreference(k_action_read_from_olcr).setOnPreferenceClickListener(this);
        this.findPreference(k_action_read_from_file).setOnPreferenceClickListener(this);
        this.findPreference(k_action_manual_entry).setOnPreferenceClickListener(this);
        this.findPreference(k_action_delete_database).setOnPreferenceClickListener(this);
        this.findPreference(k_action_delete_selected_entries).setOnPreferenceClickListener(this);
        this.findPreference(k_action_test_entry).setOnPreferenceClickListener(this);
        this.findPreference(k_option_display_id).setOnPreferenceClickListener(this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	// set title
    	mainactivity.getActionBar().setSubtitle(R.string.title_settings);
    }
    
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if (key.equals(k_action_read_from_olcr)) {
			readfromolcrActionEvent();
		} else if (key.equals(k_action_read_from_file)) {
			readfromfileActionEvent();
		} else if (key.equals(k_action_manual_entry)) {
			manualentryActionEvent();
		} else if (key.equals(k_action_delete_database)) {
			deletedbActionEvent();
		} else if (key.equals(k_action_delete_selected_entries)) {
			deleteselectedActionEvent();
		} else if (key.equals(k_action_test_entry)) {
			testentryActionEvent();
		}
		return false;
	}

	void readfromolcrActionEvent() {
		// launch read from olcr fragment
		if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_READ_OLCR) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new FReadOLCR())
				.commit();
		}
	}
	
	void deletedbActionEvent() {
        new DDeleteDbActionEvent().show(mainactivity.getFragmentManager(), null);
	}
	
	void deleteselectedActionEvent() {
		// launch delete entry fragment
		if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DELETE_ENTRY) == null) {
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
	        mainactivity.uisharedpref.edit().putBoolean(KEY_FIRSTTIMEUSE, false).commit();
			
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
		new EClassesFileAsyncTask(mainactivity).execute(new Void[] {null});
		
		// set first time use to false
        mainactivity.uisharedpref.edit().putBoolean(KEY_FIRSTTIMEUSE, false).commit();
		
	}
	
	void manualentryActionEvent() {
		// launch manual entry fragment
		if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_MANUAL_ENTRY) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new FManualEntry())
				.commit();
		}
	}

}
