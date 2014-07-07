package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManualEntryFragment extends Fragment 
{
	static final String ERRTAG = "uwatimetable";
	private static final String KEY_FIRSTTIMEUSE = "FIRSTTIMEUSE";
	
	private MainActivity mainactivity;
	private ClassesDBHelperUI dbhelperui;
	private EditText eday;
    private EditText etime;
    private EditText eunit;
    private EditText etype;
    private EditText estream;
    private EditText eweeks;
    private EditText evenue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflate menu items
		inflater.inflate(R.menu.manual_entry, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
            case R.id.action_clear:
                this.clearfieldsActionEvent();
                return true;
		}
		return false;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// set callback
		mainactivity = (MainActivity) getActivity();
		dbhelperui = mainactivity.dbhelperui;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// set title
		mainactivity.getActionBar().setSubtitle(R.string.title_manual_entry);
    	
		// show menu items
		setMenuVisibility(true);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final ViewGroup rootvg = (ViewGroup) inflater.inflate(R.layout.manual_entry_fragment, container, false);
    	
    	// find and set edittext fields
    	container = (ViewGroup)rootvg.findViewById(R.id.container);
		eday = ((EditText)container.findViewById(R.id.edittext_day));
		etime = ((EditText)container.findViewById(R.id.edittext_time));
		eunit = ((EditText)container.findViewById(R.id.edittext_unit));
		etype = ((EditText)container.findViewById(R.id.edittext_type));
		estream = ((EditText)container.findViewById(R.id.edittext_stream));
		eweeks = ((EditText)container.findViewById(R.id.edittext_weeks));
		evenue = ((EditText)container.findViewById(R.id.edittext_venue));
		
    	rootvg.findViewById(R.id.addclass).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkContainerFields()) {
					addclassButtonEvent();
				}
			}
    	});
        return rootvg;
    }
	
	@Override
	public void onStop() {
		// hide menu items
		setMenuVisibility(false);
		
		super.onStop();
	}

	boolean checkContainerFields() {
		if(eday.getText().toString().isEmpty() ||
				etime.getText().toString().isEmpty() ||
				eunit.getText().toString().isEmpty() ||
				etype.getText().toString().isEmpty() ||
				estream.getText().toString().isEmpty() ||
				eweeks.getText().toString().isEmpty() ||
				evenue.getText().toString().isEmpty()) {
			// display toast
			String toastmsg = "Error processing - please check input.";
			Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	void addclassButtonEvent() {
		// get data
		String day = eday.getText().toString();
		int time = Integer.parseInt(etime.getText().toString());
		String unit = eunit.getText().toString();
		String type = etype.getText().toString();
		int stream = Integer.parseInt(estream.getText().toString());
		String weeks = eweeks.getText().toString();
		String venue = evenue.getText().toString();
		final String[] data = new String[] {day, Integer.toString(time), unit, type, Integer.toString(stream), weeks, venue};
		
		// setup view to use
		@SuppressLint("InflateParams") // parsing null is fine here as its inflated into an alertdialog
        ViewGroup vg = (ViewGroup) mainactivity.getLayoutInflater().inflate(R.layout.verify_class_entry, null);
		((TextView)vg.findViewById(R.id.day)).setText(day);
		((TextView)vg.findViewById(R.id.weeks)).setText(weeks);
		((TextView)vg.findViewById(R.id.time)).setText(Integer.toString(time));
		((TextView)vg.findViewById(R.id.unit)).setText(unit);
		((TextView)vg.findViewById(R.id.type)).setText(type);
		((TextView)vg.findViewById(R.id.stream)).setText(Integer.toString(stream));
		((TextView)vg.findViewById(R.id.venue)).setText(venue);
		
		// build alert to verify
		final String TITLE = "Please Verify Before Adding:";
		final String POS_BUTTON = "Add";
		final String NEG_BUTTON = "Cancel";
		AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
		builder.setTitle(TITLE);
		builder.setView(vg);
		builder.setPositiveButton(POS_BUTTON, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// add class to db
				if(dbhelperui.writeClassToDB(new ContentValues[] {dbhelperui.createClassesCV(data)})) {
					// set first time use to false
			        mainactivity.uisharedpref.edit().putBoolean(KEY_FIRSTTIMEUSE, false).commit();
					
					// display toast notifying success.
					String toastmsg = "Added class.";
					Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
				} else {
					// display toast notifying success.
					String toastmsg = "Failed to add class.";
					Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton(NEG_BUTTON, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// display toast notifying success.
				String toastmsg = "Canceled.";
				Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
			}
		});
		builder.show();
	}
	
	void clearfieldsActionEvent() {
		eday.getText().clear();
		etime.getText().clear();
		eunit.getText().clear();
		etype.getText().clear();
		estream.getText().clear();
		eweeks.getText().clear();
		evenue.getText().clear();
	}
}
