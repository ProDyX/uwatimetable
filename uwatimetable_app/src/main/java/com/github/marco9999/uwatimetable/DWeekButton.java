package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class DWeekButton extends DialogFragment {

    private FMainOverview fragment;
    private AMain context;

    public static DWeekButton newInstance(AMain mainactivity, FMainOverview frag) {
        DWeekButton dialog = new DWeekButton();
        dialog.fragment = frag;
        dialog.context = mainactivity;
        return dialog;
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
        // constants
        final int MIN_WEEKS = 1;
        final int MAX_WEEKS = 52;
        String LABEL_POS = "Set";
        String LABEL_NEG = "Cancel";

        // get required variables
        final Button week = fragment.week;

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
		picker.setValue(Integer.parseInt((String) week.getText()));
		picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // stop being able to highlight the numbers (aesthetic reasons)
		
		// set the action of the reset week button (set to current week)
		resetweek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                // check if saturday, and use next week.
                // else just return current week
                if (HStatic.nextWeek()) picker.setValue(HStatic.getWeekOfYearInt() + 1);
                else picker.setValue(HStatic.getWeekOfYearInt());
			}
		});
		
		// set title and my custom view
		builder.setTitle("Set week:").setView(rootvg);
		
		// set Cancel and Set button labels and behaviour
        builder.setPositiveButton(LABEL_POS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				week.setText(Integer.toString(picker.getValue()));
				fragment.initUI();
			}
		});
        builder.setNegativeButton(LABEL_NEG, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				fragment.initUI();
			}
		});
		
		// show the dialog
		return builder.create();
	}

}
