package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DHttpsOLCRStatus extends DialogFragment implements View.OnClickListener {
    final private String KEY_ISCLIENTRUNNING = "isClientRunning";

    private AMain mainactivity;
    private String username;
    private String password;
    private boolean isClientRunning = false;
    Button b_ok;
    TextView console;

    public static DHttpsOLCRStatus newInstance(String[] data) {
        DHttpsOLCRStatus dialog = new DHttpsOLCRStatus();
        dialog.username = data[0];
        dialog.password = data[1];
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first

        if(savedInstanceState != null) {
            isClientRunning = savedInstanceState.getBoolean(KEY_ISCLIENTRUNNING);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isClientRunning) {
            openURLConnection();
            isClientRunning = true; // only want to run it once anyway.
        }
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        if (viewid == R.id.status_olcr_ok) {
            dismiss();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mainactivity = (AMain) getActivity();

        // setup view to use initially
        @SuppressLint("InflateParams") // parsing null is fine here as its inflated into an alertdialog
        ViewGroup vg = (ViewGroup) mainactivity.getLayoutInflater().inflate(R.layout.status_olcr_dialog, null);
        b_ok = (Button) vg.findViewById(R.id.status_olcr_ok);
        console = (TextView) vg.findViewById(R.id.status_olcr_text);
        b_ok.setOnClickListener(this);

        // build alert to verify
        final String TITLE = "Getting Data";
        AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
        builder.setTitle(TITLE);
        builder.setView(vg);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_ISCLIENTRUNNING, isClientRunning);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    void openURLConnection() {
        new HClassesHttpsOlcrImporter(mainactivity.dbhelperui, this).execute(username, password); // varargs array... not sure I like this way.
    }


}
