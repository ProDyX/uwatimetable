package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

class WWeekButtonDialog {

    private final Button week;
	private final MainOverviewFragment fragment;
	private final MainActivity context;
	
	WWeekButtonDialog(Button _week, MainOverviewFragment _fragment) {
		week = _week;
		fragment = _fragment;
		context = fragment.mainactivity;
	}
	 
	void show() {
		// setup
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		@SuppressLint("InflateParams") // parsing null is fine here as its inflated into an alertdialog
        ViewGroup rootvg = (ViewGroup) context.getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
		final NumberPicker picker = (NumberPicker) rootvg.findViewById(R.id.picker);
		final Button resetweek = (Button) rootvg.findViewById(R.id.resetweek);
		
		// set min, max and current values of the number picker
        int MIN_WEEKS = 1;
        picker.setMinValue(MIN_WEEKS);
        int MAX_WEEKS = 52;
        picker.setMaxValue(MAX_WEEKS);
		picker.setValue(Integer.parseInt((String) week.getText()));
		picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // stop being able to highlight the numbers (aesthetic reasons)
		
		// set the action of the reset week button (set to current week)
		resetweek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picker.setValue(StaticHelper.getWeekOfYear());
			}
		});
		
		// set title and my custom view
		builder.setTitle("Set week:").setView(rootvg);
		
		// set Cancel and Set button labels and behaviour
        String LABEL_POS = "Set";
        builder.setPositiveButton(LABEL_POS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				week.setText(Integer.toString(picker.getValue()));
				fragment.initUI();
			}
		});
        String LABEL_NEG = "Cancel";
        builder.setNegativeButton(LABEL_NEG, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				fragment.initUI();
			}
		});
		
		// show the dialog
		builder.create().show();
	}

}
