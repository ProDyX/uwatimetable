package com.github.marco9999.uwatimetable;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**  uwatimetable/AMain: Activity class where the entry point lies for Android.
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

public class AMain extends Activity {

    HClassesDbUI dbhelperui;
	SharedPreferences uisharedpref;
    ActionBarDrawerToggle drawertoggle;
    ListView drawerlist;
    DrawerLayout drawerlayout;

    private SQLiteDatabase mDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create db member and make it writeable
        HClassesDbSQL dbhelpersql = new HClassesDbSQL(this);
		mDB = dbhelpersql.getWritableDatabase();

		// ui helper class
		dbhelperui = new HClassesDbUI(mDB, this);
		
		// ui preferences
		uisharedpref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// set main view
		setContentView(R.layout.main_activity);

        // setup nav drawer
        String[] nav_drawer_items = getResources().getStringArray(R.array.nav_drawer_items);
        drawerlist = ((ListView) findViewById(R.id.nav_drawer));
        drawerlist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, nav_drawer_items));
        drawerlist.setOnItemClickListener(new LNavDrawerItemClick(this));

        // drawer toggle setup
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawertoggle = new ActionBarDrawerToggle(this, drawerlayout, R.string.drawer_open, R.string.drawer_close  ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View drawerview) {
                super.onDrawerClosed(drawerview);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerlayout.setDrawerListener(drawertoggle);

        // setup action bar for drawer

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // get fragment manager
        FragmentManager fm = getFragmentManager();

        // find the data fragment on activity restarts, and create if first time run.
        HDataFragment dataFragment = (HDataFragment) fm.findFragmentByTag(Tag.H_FRAGMENT_DATA);
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new HDataFragment();
            fm.beginTransaction().add(dataFragment, Tag.H_FRAGMENT_DATA).commit();
        }

        // dont reinitialise main fragment if its a configuration change
		if (savedInstanceState != null) {
			return;
		}
		
		// add main overview fragment (default screen)
		FMainOverview mainoverviewfrag = new FMainOverview();
		fm.beginTransaction().add(R.id.fragment_holder, mainoverviewfrag, Tag.FRAGMENT_OVERVIEW).commit();
        drawerlist.setItemChecked(0, true); // set the default fragment (overview) to be checked in nav drawer
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (drawertoggle.onOptionsItemSelected(item)) {
            return true;
        }
		switch(item.getItemId()) {
		case R.id.action_settings:
			if(getFragmentManager().findFragmentByTag(Tag.FRAGMENT_SETTINGS) == null) {
                getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
					.replace(R.id.fragment_holder, new FSettings(), Tag.FRAGMENT_SETTINGS)
                    .commit();
			}
			return true;
		case R.id.action_help:
			if(getFragmentManager().findFragmentByTag(Tag.FRAGMENT_HELP) == null) {
                getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
					.replace(R.id.fragment_holder, new FHelp(), Tag.FRAGMENT_HELP)
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
		return true;
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawertoggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawertoggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        mDB.close(); // stop leaking the reference.
        super.onDestroy();
    }

}
