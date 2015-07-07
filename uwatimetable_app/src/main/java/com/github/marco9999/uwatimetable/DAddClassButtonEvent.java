package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**  uwatimetable/DAddClassesButtonEvent: DialogFragment class for when a new class is about to be added. Asks user for conformation.
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

public class DAddClassButtonEvent extends DialogFragment {

    private String[] data;
    private AMain mainactivity;

    final static String KEY_DATA = "data";

    public static DAddClassButtonEvent newInstance(String[] classdata) {
        DAddClassButtonEvent dialog = new DAddClassButtonEvent();
        dialog.data = classdata;
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(KEY_DATA, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle config change
        if (savedInstanceState != null) data = savedInstanceState.getStringArray(KEY_DATA);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mainactivity = (AMain) getActivity();
        // setup view to use
        @SuppressLint("InflateParams") // parsing null is fine here as its inflated into an alertdialog
        ViewGroup vg = (ViewGroup) mainactivity.getLayoutInflater().inflate(R.layout.verify_class_entry, null);
        // see FManualEntry for array details {day, time, unit, type, stream, weeks, venue}
        Integer layoutpos;
        for (int i = 0; i<data.length; i++) {
            layoutpos = ClassesFields.FIELD_VIEW_MAP_ADDCLASS[i];
            if (layoutpos != null) {
                ((TextView) vg.findViewById(layoutpos)).setText(data[i]);
            }
        }

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
                if(mainactivity.dbhelperui.writeClassToDB(new ContentValues[] {mainactivity.dbhelperui.createClassesCV(data)})) {
                    // set first time use to false
                    mainactivity.uisharedpref.edit().putBoolean(Key.FIRSTTIMEUSE, false).commit();

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
        return builder.create();
    }
}
