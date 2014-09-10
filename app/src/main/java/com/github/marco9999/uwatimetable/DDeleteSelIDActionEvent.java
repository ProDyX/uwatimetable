package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class DDeleteSelIDActionEvent extends DialogFragment {

    FDeleteEntry context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get variables needed
        final String[] data = getArguments().getStringArray(Key.DATA);
        final AMain mainactivity = (AMain) getActivity();

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
                context.initList();
            }
        });
        builder.setNegativeButton(LABEL_NEG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // display toast notifying success.
                String toastmsg = "Canceled.";
                Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                context.initList();
            }
        });
        return builder.create();
    }

    void setCallback(FDeleteEntry context) {
        this.context = context;
    }
}