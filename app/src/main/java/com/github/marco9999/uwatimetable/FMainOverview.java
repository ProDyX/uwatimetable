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

public class FMainOverview extends Fragment {

    final static String SAVEINSTANCE_DAYWEEK = "SI_DAYWEEK";

	private ListView classeslist;
	private Spinner today;
	Button week;
	private WClassesBaseAdapter classadapter;
	AMain mainactivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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
		mainactivity = (AMain) this.getActivity();
		
		// for displaying in the list
		classadapter = new WClassesBaseAdapter(mainactivity);
		
		// for getting a custom day
        WDayAdapterView dayadapter = new WDayAdapterView(this);
		
		// classes listview
		classeslist.setAdapter(classadapter);
		
		// today spinner and adapter setup
		ArrayAdapter<CharSequence> dayslistadapter = ArrayAdapter.createFromResource(mainactivity, R.array.weekdays_array, android.R.layout.simple_spinner_item);
		dayslistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		today.setAdapter(dayslistadapter);
		today.setOnItemSelectedListener(dayadapter);
		
		// initial week setup
		week.setTextAppearance(mainactivity, android.R.style.TextAppearance_DeviceDefault_Widget_TextView_SpinnerItem);
		week.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DWeekButton().show(mainactivity.getFragmentManager(), null);
			}
		});
		
		// set classes list based on first time use
		if(mainactivity.uisharedpref.getBoolean(Key.FIRSTTIMEUSE, true)) {
			classeslist.getEmptyView().findViewById(R.id.first_time_use).setVisibility(View.VISIBLE);
		}

        // setup day and week text
        // see if we can restore saved info from exit
        if (mainactivity.uisharedpref.getBoolean(Key.SAVEDAYWEEK, false)) {
            String[] savedayweekdata = mainactivity.uisharedpref.getString(Key.SAVEDAYWEEKDATA, "Monday,1").split(","); // def value shouldn't be used ever except first time use
            today.setSelection(((ArrayAdapter<CharSequence>) today.getAdapter()).getPosition(savedayweekdata[0]));
            week.setText(savedayweekdata[1]);
            Log.i(LogTag.APP, "----- Loaded Day and Week: " + savedayweekdata[0] + "," + savedayweekdata[1] + " -----");
        } else {
            // otherwise just default to current day and week
            today.setSelection(HStatic.getDayOfWeekInt(), false); // set initially as today. set animate parameter to false to avoid initialising ui twice unnecessarily
            week.setText(Integer.toString(HStatic.getWeekOfYearInt()));
        }
        // try to see if its a config change.. and override anything above
        if (savedInstanceState != null) {
            String[] sidayweekdata = savedInstanceState.getStringArray(SAVEINSTANCE_DAYWEEK);
            if (sidayweekdata != null) {
                today.setSelection(((ArrayAdapter<CharSequence>) today.getAdapter()).getPosition(sidayweekdata[0]));
                week.setText(sidayweekdata[1]);
                Log.i(LogTag.APP, "----- SI LOAD Day and Week: " + sidayweekdata[0] + "," + sidayweekdata[1] + " -----");
            }
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
    public void onPause() {
        super.onPause();

        // save day and week if enabled
        if(mainactivity.uisharedpref.getBoolean(Key.SAVEDAYWEEK, false)) {
            String savedayweekdata = today.getSelectedItem() + "," + week.getText();
            mainactivity.uisharedpref.edit().putString(Key.SAVEDAYWEEKDATA, savedayweekdata).apply();
            Log.i(LogTag.APP, "----- Saved Day and Week: " +  savedayweekdata + " -----");
        }
    }
	
	@Override
	public void onStop() {
		// hide menu items
		setMenuVisibility(false);

		super.onStop();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save day and week on config change
        outState.putStringArray(SAVEINSTANCE_DAYWEEK, new String[] {(String)today.getSelectedItem(), (String)week.getText()});
        Log.i(LogTag.APP, "----- SI SAVE Day and Week: " + (String)today.getSelectedItem() + "," + (String)week.getText() + " -----");
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflate menu items
		inflater.inflate(R.menu.main_overview, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu){
	   // display all checked? by default the menuitem is unchecked
	   if (mainactivity.uisharedpref.getBoolean(Key.DISPLAYALLCLASSES, false)) {
		   menu.findItem(R.id.option_displayallclasses).setChecked(true);
	   } else {
		   menu.findItem(R.id.option_displayallclasses).setChecked(false);
	   }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_refresh:
			this.refreshActionEvent();
            return true;
		case R.id.option_displayallclasses:
			this.displayallclassesOptionEvent(item);
			return true;
		default: 
			Log.e(LogTag.APP, "MainFragmentOverview: Reached defualt in onOptionsItemSelected! Shouldn't happen.");
			break;
		}
		return false;
	}

	void initUI() {
		// get week and day
		String sday = (String)today.getSelectedItem();
		int iweek = Integer.parseInt((String)week.getText());
		
		// fill details
		Log.i(LogTag.APP, "Grabbing data with parameters { Day: " + sday + ", Week: " + iweek + " }.");
		classadapter.setDataListAndNotify(mainactivity.dbhelperui.readRelevantEntries(sday, iweek));
	}
	
	void refreshActionEvent() {
		initUI();
	}
	
	void displayallclassesOptionEvent(MenuItem mi) {
		if(mi.isChecked()) {
			mainactivity.uisharedpref.edit().putBoolean(Key.DISPLAYALLCLASSES, false).apply();
			initUI();
		} else {
			mainactivity.uisharedpref.edit().putBoolean(Key.DISPLAYALLCLASSES, true).apply();
			initUI();
		}
	}
}
