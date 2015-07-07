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

/**  uwatimetable/DWeekButton: DialogFragment class for when a specific week is to be selected for display, initiated from the user.
 *    Copyright (C) 2015 Marco Satti
 *    Contact: marcosatti@gmail.com
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
