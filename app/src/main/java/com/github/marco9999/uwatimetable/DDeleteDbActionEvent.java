package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DDeleteDbActionEvent extends DialogFragment {

    private static final String ERRTAG = "uwatimetable";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get variables needed
        final AMain mainactivity = (AMain) getActivity();

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
                Log.i(ERRTAG, "Deleted DB");
            }
        });
        builder.setNegativeButton(BUTTON_NEG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                // display toast notifying success.
                String toastmsg = "Canceled.";
                Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
            }
        });
        return builder.create();
    }
}
