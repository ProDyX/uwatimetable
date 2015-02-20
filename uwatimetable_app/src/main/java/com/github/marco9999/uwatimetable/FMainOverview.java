package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Spinner;

import java.util.ArrayList;

public class FMainOverview extends Fragment {

    final static String SAVEINSTANCE_DAYWEEK = "SI_DAYWEEK";

	private RecyclerView classeslist;
	private Spinner today;
	Button week;
	private AdOverviewClassesList classadapter;
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
		classeslist = (RecyclerView) vg.findViewById(R.id.main_classeslist);
		
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
		
		// classes recyclerview and adapter setup
        classeslist.setLayoutManager(new LinearLayoutManager(mainactivity));
		classadapter = AdOverviewClassesList.newInstance(mainactivity, (ViewGroup) classeslist.getRootView());
		classeslist.setAdapter(classadapter);
        // TODO: check if this applies: classeslist.setHasFixedSize(true);


		// today spinner and adapter setup
		ArrayAdapter<CharSequence> dayslistadapter = ArrayAdapter.createFromResource(mainactivity, R.array.weekdays_array, android.R.layout.simple_spinner_item);
		dayslistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		today.setAdapter(dayslistadapter);
        WrDayAdapterView dayadapter = new WrDayAdapterView(this);
		today.setOnItemSelectedListener(dayadapter);

		
		// week setup
		week.setTextAppearance(mainactivity, android.R.style.TextAppearance_DeviceDefault_Widget_TextView_SpinnerItem);
		week.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DWeekButton.newInstance(mainactivity, FMainOverview.this).show(mainactivity.getFragmentManager(), null);
			}
		});

        // pre ui setup (misc)
        preinitUI(savedInstanceState);

	}
	
	@Override
	public void onResume() {
		super.onResume();

		// set title
    	mainactivity.getActionBar().setSubtitle(R.string.title_overview);

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
			break;
		}
		return false;
	}

    void preinitUI(Bundle savedInstanceState) {
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
            if (HStatic.nextWeek()){
                week.setText(Integer.toString(HStatic.getWeekOfYearInt() + 1)); // saturday
            } else {
                week.setText(Integer.toString(HStatic.getWeekOfYearInt())); // default
            }
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

	void initUI() {
		// get week and day
		String sday = (String)today.getSelectedItem();
		int iweek = Integer.parseInt((String)week.getText());

		// get details
		Log.i(LogTag.APP, "Grabbing data with parameters { Day: " + sday + ", Week: " + iweek + " }.");
        ArrayList<String[]> clslist = mainactivity.dbhelperui.readRelevantEntries(sday, iweek);

        // check if empty and display ui accordingly
        assert getView() != null; // damn lint warnings... may happen but i cant think of a scenario.
        ViewGroup vg = (ViewGroup) getView().findViewById(R.id.empty_classes_list);
        if (clslist.size() == 0) {
            vg.setVisibility(View.VISIBLE);
            if(mainactivity.uisharedpref.getBoolean(Key.FIRSTTIMEUSE, true)) {
                vg.findViewById(R.id.first_time_use).setVisibility(View.VISIBLE);
            }
        } else {
            vg.setVisibility(View.GONE);
        }

        // fill details
		classadapter.setClasseslistAndNotify(clslist);
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
