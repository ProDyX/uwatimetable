package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment implements OnPreferenceClickListener {
	
	static final String ERRTAG = "uwatimetable";
	static final String KEY_FIRSTTIMEUSE = "FIRSTTIMEUSE";
	static final String TAG_FRAGMENT_MANUAL_ENTRY = "manual_entry";
	static final String TAG_FRAGMENT_DELETE_ENTRY = "delete_entry";
	static final String TAG_FRAGMENT_READ_OLCR = "read_olcr";
	
	MainActivity mainactivity;
	
	static final String k_action_read_from_olcr = "action_read_from_olcr";
	static final String k_action_read_from_file = "action_read_from_file";
	static final String k_action_manual_entry = "action_manual_entry";
	static final String k_action_delete_database = "action_delete_database";
	static final String k_action_delete_selected_entries = "action_delete_selected_entries";
	static final String k_action_test_entry = "action_test_entry";
	static final String k_option_display_id = "option_display_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_fragment);
        
        // add callback to main activity
        mainactivity = (MainActivity) getActivity();
        
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

	public void readfromolcrActionEvent() {
		// launch read from olcr fragment
		if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_READ_OLCR) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new ReadOLCRFragment())
				.commit();
		}
	}
	
	public void deletedbActionEvent() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
		builder.setTitle("Wipe Database?").setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("Are you sure you want to delete the database? You can not recover the data after this has been done!");
		builder.setPositiveButton("Wipe", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int id) {
		    	mainactivity.dbhelperui.recreateClassesDB();
		    	
				// display toast notifying success.
				String toastmsg = "Deleted old database.";
				Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
				
				Log.i(ERRTAG, "Deleted DB");
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int id) {
		    	dialog.dismiss();
		    	
		    	// display toast notifying success.
				String toastmsg = "Canceled.";
				Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
		    }
		});
		builder.show();
	}
	
	public void deleteselectedActionEvent() {
		// launch delete entry fragment
		if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DELETE_ENTRY) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new DeleteEntryFragment())
				.commit();
		}
	}
	
	public void testentryActionEvent() {
		if(mainactivity.dbhelperui.writeClassToDB(new ContentValues[] {StaticHelper.createTestEntry()})) {
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

	public void readfromfileActionEvent() {
		// See the ClassesFileAsyncTask class for more details.
		new ClassesFileAsyncTask(mainactivity).execute(new Void[] {null});
		
		// set first time use to false
        mainactivity.uisharedpref.edit().putBoolean(KEY_FIRSTTIMEUSE, false).commit();
		
	}
	
	public void manualentryActionEvent() {
		// launch manual entry fragment
		if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_MANUAL_ENTRY) == null) {
			getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.replace(R.id.fragment_holder, new ManualEntryFragment())
				.commit();
		}
	}

}
