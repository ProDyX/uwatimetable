package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

/**  uwatimetable/DDeleteIDActionEvent: DialogFragment class for when a user is manually deleting an entry from the database by ID number.
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

public class DDeleteIDActionEvent extends DialogFragment {

    private AMain mainactivity;

    public static DDeleteIDActionEvent newInstance() {
        DDeleteIDActionEvent dialog = new DDeleteIDActionEvent();
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mainactivity = (AMain) getActivity();
        // constants
        String LABEL_POS = "Delete";
        String LABEL_NEG = "Cancel";

        // build view
        final EditText et = new EditText(mainactivity);
        et.setHint(R.string.hint_enterid);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        // build alert
        AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
        builder.setTitle(R.string.enterid);
        builder.setView(et);

        // set pos and neg buttons
        builder.setPositiveButton(LABEL_POS, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // string id value
                String value = et.getText().toString();

                // check id exists and delete
                if(mainactivity.dbhelperui.deleteClassFromDB(value)) {
                    // display toast notifying success.
                    String toastmsg = "Deleted Class.";
                    Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                } else {
                    // failure
                    String toastmsg = "Failure - ID doesn't exist.";
                    Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(LABEL_NEG, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // failure
                String toastmsg = "Canceled.";
                Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();
    }
}
