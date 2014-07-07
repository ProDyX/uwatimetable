package com.github.marco9999.uwatimetable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

class WDayAdapterView implements OnItemSelectedListener {
	
	static final String ERRTAG = "uwatimetable";

	private final MainOverviewFragment ui;
	
	WDayAdapterView(MainOverviewFragment _ui) {
		ui = _ui;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		//Log.i(ERRTAG, "initUI called from Day Spinner");
		ui.initUI();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

}
