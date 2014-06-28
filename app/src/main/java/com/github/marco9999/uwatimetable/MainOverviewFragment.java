package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class MainOverviewFragment extends Fragment
{
	static final String ERRTAG = "uwatimetable";
	static final String KEY_DISPLAYALLCLASSES = "DISPLAYALLCLASSES";
	static final String KEY_FIRSTTIMEUSE = "FIRSTTIMEUSE";

	ListView classeslist;
	Spinner today;
	Button week;
	WListBaseAdapter classadapter;
	WDayAdapterView dayadapter;
	WWeekButtonDialog weekdialog;
	MainActivity mainactivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate view to use
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.main_overview_fragment, container, false);
		
		// classes listview
		classeslist = (ListView) vg.findViewById(R.id.main_classeslist);
		// This was tricky to get right... setEmptyView requires that the inflated view be inside its container (ie: from onCreateView above), 
		// and the function changes the visibility to none automatically
		inflater.inflate(R.layout.empty_classes_list, vg, true);
		classeslist.setEmptyView(vg.findViewById(R.id.empty_classes_list));
		
		// today spinner
		today = (Spinner) vg.findViewById(R.id.today);

		// week button
		week = (Button) vg.findViewById(R.id.week);
		
		return vg;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
		// get root activity (set callback)
		mainactivity = (MainActivity) this.getActivity();
		
		// for displaying in the list
		classadapter = new WListBaseAdapter(mainactivity);
		
		// for getting a custom day
		dayadapter = new WDayAdapterView(this);
		
		// classes listview
		classeslist.setAdapter(classadapter);
		
		// today spinner and adapter setup
		ArrayAdapter<CharSequence> dayslistadapter = ArrayAdapter.createFromResource(mainactivity, R.array.weekdays_array, android.R.layout.simple_spinner_item);
		dayslistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		today.setAdapter(dayslistadapter);
		today.setSelection(StaticHelper.getDayOfWeekInt(), false); // set initially as today
		today.setOnItemSelectedListener(dayadapter);
		
		// initial week setup
		weekdialog = new WWeekButtonDialog(week, this);
		week.setText(Integer.toString(StaticHelper.getWeekOfYear()));
		week.setTextAppearance(mainactivity, android.R.layout.simple_spinner_item);
		week.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				weekdialog.show();
			}
		});
		
		// set classes list based on first time use
		if(mainactivity.uisharedpref.getBoolean(KEY_FIRSTTIMEUSE, true)) {
			classeslist.getEmptyView().findViewById(R.id.first_time_use).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// set title
    	mainactivity.getActionBar().setSubtitle(R.string.title_overview);

    	// show menu items
		setMenuVisibility(true);
		
		// show the ui
		initUI();
	}
	
	@Override
	public void onStop() {
		// hide menu items
		setMenuVisibility(false);
		
		// save prefs
		// mainactivity.uisharedpref.edit().putBoolean(KEY_DISPLAYALLCLASSES, mainactivity.dbhelperui.displayall).commit();
		
		super.onStop();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflate menu items
		inflater.inflate(R.menu.main_overview, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu){
	   // display all checked? by default the menuitem is unchecked
	   if (mainactivity.uisharedpref.getBoolean(KEY_DISPLAYALLCLASSES, false)) {
		   menu.findItem(R.id.option_displayallclasses).setChecked(true);
	   } else {
		   menu.findItem(R.id.option_displayallclasses).setChecked(false);
	   }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case R.id.action_refresh:
			this.refreshActionEvent(item);
		case R.id.option_displayallclasses:
			this.displayallclassesOptionEvent(item);
			return true;
		default: 
			Log.e(ERRTAG, "MainFragmentOverview: Reached defualt in onOptionsItemSelected! Shouldn't happen.");
			break;
		}
		return false;
	}

	void initUI() {
		// get week and day
		String sday = (String) today.getSelectedItem();
		int iweek = Integer.parseInt((String) week.getText());
		
		// fill details
		Log.i(ERRTAG, "Grabbing data with parameters { Day: " + sday + ", Week: " + iweek + " }.");
		classadapter.setDataListAndNotify(mainactivity.dbhelperui.readRelevantEntries(sday, iweek));
		
		// DEBUG: stack trace
		// Thread.dumpStack();
	}
	
	public void refreshActionEvent(MenuItem item) {
		initUI();
	}
	
	public void displayallclassesOptionEvent(MenuItem mi) {
		if(mi.isChecked()) {
			mainactivity.uisharedpref.edit().putBoolean(KEY_DISPLAYALLCLASSES, false).commit();
			initUI();
		} else {
			mainactivity.uisharedpref.edit().putBoolean(KEY_DISPLAYALLCLASSES, true).commit();
			initUI();
		}
	}
}
