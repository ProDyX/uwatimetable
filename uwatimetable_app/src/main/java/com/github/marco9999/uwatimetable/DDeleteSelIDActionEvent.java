package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**  uwatimetable/DDeleteSelIDActionEvent: DialogFragment class for when a user is about to erase selected entries from the database.
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

public class DDeleteSelIDActionEvent extends DialogFragment {

    private FDeleteEntry callback;
    private String[] data;
    private AMain mainactivity;

    final static String KEY_DATA = "data";

    public static DDeleteSelIDActionEvent newInstance(String[] _data, FDeleteEntry _callback) {
        DDeleteSelIDActionEvent dialog = new DDeleteSelIDActionEvent();
        dialog.data = _data;
        dialog.callback = _callback;
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

        // constants
        final String TITLE = "Please Verify Before Deleting:";
        final String LABEL_POS = "Delete";
        final String LABEL_NEG = "Cancel";

        // build view
        final TextView tv = new TextView(mainactivity);
        final StringBuilder sb = new StringBuilder();
        sb.append("Delete ID(s): ");
        for(int i = 0; i<data.length; i++) {
            sb.append(data[i]).append(", ");
        }
        sb.delete(sb.length()-2, sb.length()).append("?");
        tv.setText(sb.toString());
        tv.setGravity(Gravity.CENTER);

        // build alert to verify
        AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
        builder.setTitle(TITLE);
        builder.setView(tv);
        builder.setPositiveButton(LABEL_POS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete class
                boolean retstate = true;
                for(int i = 0; i<data.length; i++) {
                    if(!mainactivity.dbhelperui.deleteClassFromDB(data[i])) {
                        retstate = false; // something didnt delete
                    }
                }
                if(!retstate) {
                    // display toast notifying success.
                    String toastmsg = "Failed to remove some classes.";
                    Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                } else {
                    // display toast notifying success.
                    String toastmsg = "Deleted classes.";
                    Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                }
                callback.initUI();
            }
        });
        builder.setNegativeButton(LABEL_NEG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // display toast notifying success.
                String toastmsg = "Canceled.";
                Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                callback.initUI();
            }
        });
        return builder.create();
    }

    void setCallback(FDeleteEntry _callback) {
        callback = _callback;
    }
}
