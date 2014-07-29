package com.github.marco9999.uwatimetable;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AMain extends Activity
{	
	static final String ERRTAG = "uwatimetable";
	private static final String TAG_FRAGMENT_OVERVIEW = "Overview";
	private static final String TAG_FRAGMENT_SETTINGS = "Settings";
	private static final String TAG_FRAGMENT_HELP = "Help";

    HClassesDbUI dbhelperui;
	FMainOverview mainoverviewfrag;
	SharedPreferences uisharedpref;
    ActionBarDrawerToggle drawertoggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create db member and make it writeable
        HClassesDbSQL dbhelpersql = new HClassesDbSQL(this);
		SQLiteDatabase mDB = dbhelpersql.getWritableDatabase();

		// ui helper class
		dbhelperui = new HClassesDbUI(mDB, this);
		
		// ui preferences
		uisharedpref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// set main view
		setContentView(R.layout.main_activity);

        // setup nav drawer
        String[] nav_drawer_items = getResources().getStringArray(R.array.nav_drawer_items);
        ListView drawer = ((ListView) findViewById(R.id.nav_drawer));
        drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nav_drawer_items));
        drawer.setOnItemClickListener(new LNavDrawerItemClick(this));

        // drawer toggle
        DrawerLayout drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawertoggle = new ActionBarDrawerToggle(this, drawerlayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close  ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerlayout.setDrawerListener(drawertoggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // dont reinitialise fragment if its a configuration change
		if (savedInstanceState != null) {
			mainoverviewfrag = (FMainOverview) getFragmentManager().findFragmentByTag(TAG_FRAGMENT_OVERVIEW);
			return;
		}
		
		// add main overview fragment (default screen)
		mainoverviewfrag = new FMainOverview();
		getFragmentManager().beginTransaction().add(R.id.fragment_holder, mainoverviewfrag, TAG_FRAGMENT_OVERVIEW).commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (drawertoggle.onOptionsItemSelected(item)) {
            return true;
        }
		switch(item.getItemId()) {
		case R.id.action_settings:
			if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_SETTINGS) == null) {
				getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.replace(R.id.fragment_holder, new FSettings(), TAG_FRAGMENT_SETTINGS)
					.commit();
			}
			return true;
		case R.id.action_help:
			if(getFragmentManager().findFragmentByTag(TAG_FRAGMENT_HELP) == null) {
				getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.replace(R.id.fragment_holder, new FHelp(), TAG_FRAGMENT_HELP)
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
