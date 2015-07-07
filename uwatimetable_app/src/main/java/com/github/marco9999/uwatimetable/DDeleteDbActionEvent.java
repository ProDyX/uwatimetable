package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**  uwatimetable/DDeleteDbActionEvent: DialogFragment class for when a user is about to perform a database wipe.
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

public class DDeleteDbActionEvent extends DialogFragment {

    private AMain mainactivity;

    public static DDeleteDbActionEvent newInstance() {
        DDeleteDbActionEvent dialog = new DDeleteDbActionEvent();
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mainactivity = (AMain) getActivity();
        // constants
        final String title = "Wipe Database?";
        final String msg = "Are you sure you want to delete the database? You can not recover the data after this has been done!";
        final String BUTTON_POS = "Wipe";
        final String BUTTON_NEG = "Cancel";

        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
        builder.setTitle(title).setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(msg);
        builder.setPositiveButton(BUTTON_POS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // recreate db
                mainactivity.dbhelperui.recreateClassesDB();

                // display toast notifying success.
                String toastmsg = "Deleted old database.";
                Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();

                // log this
                Log.i(LogTag.APP, "Deleted DB");
            }
        });
        builder.setNegativeButton(BUTTON_NEG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // display toast notifying success.
                String toastmsg = "Canceled.";
                Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
            }
        });
        return builder.create();
    }
}
