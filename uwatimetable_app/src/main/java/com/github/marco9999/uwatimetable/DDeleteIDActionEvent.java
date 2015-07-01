package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

public class DDeleteIDActionEvent extends DialogFragment {

    private AMain mainactivity;

    public static DDeleteIDActionEvent newInstance(AMain _mainactivity) {
        DDeleteIDActionEvent dialog = new DDeleteIDActionEvent();
        dialog.mainactivity = _mainactivity;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
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
