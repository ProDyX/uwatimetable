package com.github.marco9999.uwatimetable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

class WDayAdapterView implements OnItemSelectedListener {

	private final FMainOverview ui;
	
	WDayAdapterView(FMainOverview _ui) {
		ui = _ui;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		ui.initUI();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

}
