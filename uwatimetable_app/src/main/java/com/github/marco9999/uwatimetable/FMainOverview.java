package com.github.marco9999.uwatimetable;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class FMainOverview extends Fragment {

    AMain mainactivity;

    private RecyclerView classeslist;
	private Spinner dayview;
	private Button weekview;

	private AdOverviewClassesList classadapter;
	private AdWeekButton weekadapter;
    private AdDaySpinner dayadapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LogTag.APP, "%%% onAttach called %%%");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d(LogTag.APP, "%%% onCreate called %%%");
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LogTag.APP, "%%% create view called %%%");
		// inflate view to use
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.main_overview_fragment, container, false);
		
		// classes listview
		classeslist = (RecyclerView) vg.findViewById(R.id.main_classeslist);
		
		// dayview spinner
		dayview = (Spinner) vg.findViewById(R.id.today);

		// weekview button
		weekview = (Button) vg.findViewById(R.id.week);
		
		return vg;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
        Log.d(LogTag.APP, "%%% activity created called %%%");
	    
		// get root activity needed for some Android functions
		mainactivity = (AMain) this.getActivity();
		
		// classeslist recyclerview setup
        classeslist.setLayoutManager(new LinearLayoutManager(mainactivity));
		classadapter = AdOverviewClassesList.newInstance(this); // needed for initUI()
		classeslist.setAdapter(classadapter);

		// dayview spinner setup
		dayadapter = AdDaySpinner.newInstance(this, dayview, R.array.weekdays_array, android.R.layout.simple_spinner_item);
		dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dayview.setAdapter(dayadapter);
		dayview.setOnItemSelectedListener(dayadapter);

		// weekview button setup
		weekadapter = AdWeekButton.newInstance(this, weekview);
        weekview.setTextAppearance(mainactivity, android.R.style.TextAppearance_DeviceDefault_Widget_TextView_SpinnerItem);
        weekview.setOnClickListener(weekadapter);
        DWeekButton weekdialog;
        if((weekdialog = (DWeekButton) mainactivity.getFragmentManager().findFragmentByTag(Tag.DIALOG_WEEK)) != null) weekdialog.setAdapter(weekadapter); // handle config change... TODO: there must be a better way surely..

		// pre ui setup (misc)
		preinitUI(savedInstanceState);
	}

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LogTag.APP, "%%% start called %%%");
    }

	@Override
	public void onResume() {
		super.onResume();

        Log.d(LogTag.APP, "%%% resume called %%%");

		// set title
    	mainactivity.getActionBar().setSubtitle(R.string.title_overview);

        // show the ui
		initUI();
	}


	



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(LogTag.APP, "%%% on save called %%%");
        // save day and week on config change
        // need to check if its null because of fringe cases involving orientation changes and different fragments
        if (dayadapter != null && weekadapter != null) {
            outState.putIntArray(Key.SAVEINSTANCEDATA, new int[]{dayadapter.getDay(), weekadapter.getWeek()});
            Log.i(LogTag.APP, "----- SI SAVE Day and Week: " + Integer.toString(dayadapter.getDay()) + "," + Integer.toString(weekadapter.getWeek()) + " -----");
        }
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
        // setup day and week values
        dayadapter.setDay(HStatic.getDayOfWeekInt());
        weekadapter.setWeek(HStatic.getWeekOfYearInt()); // TODO: check if this is needed HStatic.nextWeek()

        // if save day and week on exit is enabled, overwrite values set above
        if (mainactivity.uisharedpref.getBoolean(Key.SAVEDAYWEEK, false)) {
            int daydata = mainactivity.uisharedpref.getInt(Key.SAVEDAYWEEKDATA_DAY, 0); // def value shouldn't be used ever except first time use
            int weekdata = mainactivity.uisharedpref.getInt(Key.SAVEDAYWEEKDATA_WEEK, 1); // def value shouldn't be used ever except first time use
            dayadapter.setDay(daydata);
            weekadapter.setWeek(weekdata);
            Log.i(LogTag.APP, "----- Loaded Day and Week: " + Integer.toString(daydata) + "," + Integer.toString(weekdata) + " -----");
        }

        // try to see if its a config change.. and override anything above
        if (savedInstanceState != null) {
            int[] sidayweekdata = savedInstanceState.getIntArray(Key.SAVEINSTANCEDATA);
            if (sidayweekdata != null) {
                dayadapter.setDay(sidayweekdata[0]);
                weekadapter.setWeek(sidayweekdata[1]);
                Log.i(LogTag.APP, "----- SI LOAD Day and Week: " + Integer.toString(sidayweekdata[0]) + "," + Integer.toString(sidayweekdata[1]) + " -----");
            }
        }
    }

	void initUI() {
		// get weekview and day
		String sday = dayadapter.getDayString();
		int iweek = weekadapter.getWeek();

		// get details
		Log.i(LogTag.APP, "Grabbing data with parameters { Day: " + sday + ", Week: " + iweek + " }.");
        ArrayList<String[]> clslist = mainactivity.dbhelperui.readRelevantEntries(sday, iweek);

        // check if empty and display ui accordingly
        if (getView() == null) {
            Log.e(LogTag.APP, "FMainOverview: getView() returned null. Shouldn't ever happen!");
        } else {
            ViewGroup vgempty = (ViewGroup) getView().findViewById(R.id.empty_classes_list);
            ViewGroup vglist = (ViewGroup) getView().findViewById(R.id.main_classeslist);
            if (clslist.size() == 0) {
                vgempty.setVisibility(View.VISIBLE);
                vglist.setVisibility(View.GONE);
                if (mainactivity.uisharedpref.getBoolean(Key.FIRSTTIMEUSE, true)) {
                    vgempty.findViewById(R.id.first_time_use).setVisibility(View.VISIBLE);
                }
            } else {
                vgempty.setVisibility(View.GONE);
                vglist.setVisibility(View.VISIBLE);
            }
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





    @Override
    public void onPause() {
        Log.d(LogTag.APP, "%%% pause called %%%");

        // save day and weekview if enabled
        if(mainactivity.uisharedpref.getBoolean(Key.SAVEDAYWEEK, false)) {
            int daydata = dayadapter.getDay();
            int weekdata = weekadapter.getWeek();
            mainactivity.uisharedpref.edit().putInt(Key.SAVEDAYWEEKDATA_DAY, daydata).apply();
            mainactivity.uisharedpref.edit().putInt(Key.SAVEDAYWEEKDATA_WEEK, weekdata).apply();
            Log.i(LogTag.APP, "----- Saved Day and Week: " + Integer.toString(daydata) + "," + Integer.toString(weekdata) + " -----");
        }

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(LogTag.APP, "%%% stop called %%%");
        super.onStop();
    }

	@Override
	public void onDestroyView() {
		Log.d(LogTag.APP, "%%% destroy view called %%%");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		Log.d(LogTag.APP, "%%% destroy called %%%");
		super.onDestroy();
	}

    @Override
    public void onDetach() {
        Log.d(LogTag.APP, "%%% detach called %%%");
        super.onDetach();
    }
}
