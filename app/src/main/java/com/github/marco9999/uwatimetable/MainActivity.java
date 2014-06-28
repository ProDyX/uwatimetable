package com.github.marco9999.uwatimetable;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity 
{	
	static final String ERRTAG = "uwatimetable";
	static final String TAG_FRAGMENT_OVERVIEW = "overview";
	static final String TAG_FRAGMENT_SETTINGS = "settings";
	static final String TAG_FRAGMENT_HELP = "help";

    ClassesDBHelperSQL dbhelpersql;
	ClassesDBHelperUI dbhelperui;
	MainOverviewFragment mainoverviewfrag;
	SharedPreferences uisharedpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create db member and make it writeable
		dbhelpersql = new ClassesDBHelperSQL(this);
		SQLiteDatabase mDB = dbhelpersql.getWritableDatabase();

		// ui helper class
		dbhelperui = new ClassesDBHelperUI(mDB, this);
		
		// ui preferences
		uisharedpref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// set main view
		setContentView(R.layout.main_activity);
		
		// dont reinitialise fragment if its a configuration change
		if (savedInstanceState != null) {
			mainoverviewfrag = (MainOverviewFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT_OVERVIEW);
			return;
		}
		
		// add main overview fragment (default screen)
		mainoverviewfrag = new MainOverviewFragment();
		getFragmentManager().beginTransaction().add(R.id.fragment_holder, mainoverviewfrag, TAG_FRAGMENT_OVERVIEW).commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_SETTINGS) == null) {
				getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.replace(R.id.fragment_holder, new SettingsFragment(), TAG_FRAGMENT_SETTINGS)
					.commit();
			}
			return true;
		case R.id.action_help:
			if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_HELP) == null) {
				getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.replace(R.id.fragment_holder, new HelpFragment(), TAG_FRAGMENT_HELP)
					.commit();
			}
			return true;
		default: 
			return false;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// inflate menu items
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(!mainoverviewfrag.isVisible()) {
			menu.findItem(R.id.action_settings).setVisible(false);
			menu.findItem(R.id.action_help).setVisible(false);
		}
		return true;
	}
}
