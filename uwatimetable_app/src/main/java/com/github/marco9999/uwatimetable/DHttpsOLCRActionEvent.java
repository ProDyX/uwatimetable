package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DHttpsOLCRActionEvent extends DialogFragment implements View.OnClickListener {

    private AMain mainactivity;
    private Dialog dialog;
    private String username;
    private String password;
    private TextView console;

    public static DHttpsOLCRActionEvent newInstance(AMain _mainactivity) {
        DHttpsOLCRActionEvent dialog = new DHttpsOLCRActionEvent();
        dialog.mainactivity = _mainactivity;
        return dialog;
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        if (viewid == R.id.login_olcr_cancel) {
            dismiss();
        } else if (viewid == R.id.login_olcr_ok) {
            username = ((EditText) dialog.findViewById(R.id.login_olcr_user)).getText().toString();
            password = ((EditText) dialog.findViewById(R.id.login_olcr_pass)).getText().toString();
            changeDialogView();
            openURLConnection();
        } else if (viewid == R.id.status_olcr_ok) {
            dismiss();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // setup view to use initially
        @SuppressLint("InflateParams") // parsing null is fine here as its inflated into an alertdialog
        ViewGroup vg = (ViewGroup) mainactivity.getLayoutInflater().inflate(R.layout.login_olcr_dialog, null);
        Button b_cancel = (Button) vg.findViewById(R.id.login_olcr_cancel);
        Button b_ok = (Button) vg.findViewById(R.id.login_olcr_ok);
        b_cancel.setOnClickListener(this);
        b_ok.setOnClickListener(this);

        // build alert to verify
        final String TITLE = "Login to OLCR";
        AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
        builder.setTitle(TITLE);
        builder.setView(vg);
        dialog = builder.create();
        return dialog;
    }

    void changeDialogView() {
        ViewGroup vg = (ViewGroup) mainactivity.getLayoutInflater().inflate(R.layout.status_olcr_dialog, null);
        dialog.setContentView(vg);
        console = (TextView) vg.findViewById(R.id.status_olcr_text);
        vg.findViewById(R.id.status_olcr_ok).setOnClickListener(this);
        vg.findViewById(R.id.status_olcr_ok).setClickable(false);
    }

    void openURLConnection() {
        new HClassesHttpsOlcrImporter(mainactivity, console).execute(username, password); // varargs array... not sure I like this way.
    }
}
