package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class DHttpsOLCRStatus extends DialogFragment implements View.OnClickListener {

    private AMain mainactivity;
    private String username;
    private String password;
    Button b_ok;
    TextView console;
    ScrollView scroll;
    private boolean hasrun = false;

    final static String KEY_HASRUN = "hasrun";

    public static DHttpsOLCRStatus newInstance(String[] data) {
        DHttpsOLCRStatus dialog = new DHttpsOLCRStatus();
        dialog.username = data[0];
        dialog.password = data[1];
        return dialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HDataFragment)getFragmentManager().findFragmentByTag(Tag.H_FRAGMENT_DATA)).olcr_https_status = this;
        if (savedInstanceState != null) hasrun = savedInstanceState.getBoolean(KEY_HASRUN);
    }

    @Override
    public void onResume() {
        super.onResume();
        HDataFragment data = (HDataFragment)getFragmentManager().findFragmentByTag(Tag.H_FRAGMENT_DATA);
        if (data.olcr_https_client == null) {
            if (!hasrun) {
                data.olcr_https_client = new HClassesHttpsOlcrImporter(mainactivity.dbhelperui, getFragmentManager());
                data.olcr_https_client.execute(username, password); // varargs array... not sure I like this way.
                hasrun = true;
            }
        } else {
            // god what a clusterfuck of code this is... race conditions...
            // if a config changed happened, need to make sure something is displayed on the ui for the user, if all update methods fail from the ASyncTask.
            if (data.olcr_https_client.isfinished) {
                Log.d(LogTag.APP, "consoletext: " + data.olcr_https_client.consoletext);
                console.setText(Html.fromHtml(data.olcr_https_client.consoletext));
                b_ok.setClickable(true);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }

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
        scroll = (ScrollView) vg.findViewById(R.id.status_olcr_scoll);
        b_ok.setOnClickListener(this);

        // build alert to verify
        final String TITLE = "Getting Data";
        AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
        builder.setTitle(TITLE);
        builder.setView(vg);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        // reset all once they exit
        HDataFragment data = (HDataFragment) getFragmentManager().findFragmentByTag(Tag.H_FRAGMENT_DATA);
        data.olcr_https_status = null;
        if (!getActivity().isChangingConfigurations()) {
            if (data.olcr_https_client != null) {
                if (data.olcr_https_client.isfinished) {
                    data.olcr_https_client = null;
                }
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        if (viewid == R.id.status_olcr_ok) {
            dismiss();
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_HASRUN, hasrun);
    }

}
