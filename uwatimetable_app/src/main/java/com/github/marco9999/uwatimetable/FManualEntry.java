package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class FManualEntry extends Fragment {

    private AMain mainactivity;
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
		mainactivity = (AMain) getActivity();
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
		String time = String.valueOf(Integer.parseInt(etime.getText().toString())); // use parse int and string to clean input before processing
		String unit = eunit.getText().toString();
		String type = etype.getText().toString();
		String stream = String.valueOf(Integer.parseInt(estream.getText().toString())); // use parse int and string to clean input before processing
		String weeks = eweeks.getText().toString();
		String venue = evenue.getText().toString();
		String[] data = new String[] {day, time, unit, type, stream, weeks, venue};
		
        // create fragment
        DAddClassButtonEvent dialog = DAddClassButtonEvent.newInstance(mainactivity, data);
        dialog.show(mainactivity.getFragmentManager(), null);
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
