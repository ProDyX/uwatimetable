package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class DWeekButton extends DialogFragment {

    private AdWeekButton adapter;

    public static DWeekButton newInstance(AdWeekButton _adapter) {
        DWeekButton dialog = new DWeekButton();
        dialog.adapter = _adapter;
        return dialog;
    }

    public void setAdapter(AdWeekButton _adapter) {
        adapter = _adapter;
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LogTag.APP, "%%% on create dialog called %%%");

        // setup
		super.onCreateDialog(savedInstanceState);
		AMain context = (AMain) getActivity();

        // constants
        final int MIN_WEEKS = 1;
        final int MAX_WEEKS = 52;
        String LABEL_POS = "Set";
        String LABEL_NEG = "Cancel";

		// setup
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		@SuppressLint("InflateParams") // parsing null is fine here as its inflated into an alertdialog
        ViewGroup rootvg = (ViewGroup) context.getLayoutInflater().inflate(R.layout.dialog_week_picker, null);
        final TextView noeffect = (TextView) rootvg.findViewById(R.id.week_noeffect);
		final NumberPicker picker = (NumberPicker) rootvg.findViewById(R.id.picker);
		final Button resetweek = (Button) rootvg.findViewById(R.id.resetweek);

        // set if no effect message is visible
        if(context.uisharedpref.getBoolean(Key.DISPLAYALLCLASSES, false)) {
            noeffect.setVisibility(View.VISIBLE);
        }
		
		// set min, max and current values of the number picker
        picker.setMinValue(MIN_WEEKS);
        picker.setMaxValue(MAX_WEEKS);
		picker.setValue(adapter.getWeek());
		picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // stop being able to highlight the numbers (aesthetic reasons)
		
		// set the action of the reset weekview button (set to current weekview)
		resetweek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                picker.setValue(HStatic.getWeekOfYearInt());
			}
		});
		
		// set title and my custom view
		builder.setTitle("Set weekview:").setView(rootvg);
		
		// set Cancel and Set button labels and behaviour
        builder.setPositiveButton(LABEL_POS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adapter.setWeekAndDisplay(picker.getValue());
			}
		});
        builder.setNegativeButton(LABEL_NEG, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adapter.setWeekAndDisplay(adapter.getWeek()); // refresh UI, just in case, but with same value.
			}
		});
		
		// show the dialog
		return builder.create();
	}

}
