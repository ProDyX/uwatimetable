package com.github.marco9999.uwatimetable;

import android.view.View;
import android.widget.Button;

/**
 * Created by Marco on 2/07/2015.
 * A simple pseudo-adapter class in order to store current weekview data, and set it properly.
 */

public class AdWeekButton implements View.OnClickListener {
    private FMainOverview callback;
    private int week;
    private Button weekbutton;

    public static AdWeekButton newInstance(FMainOverview _callback, Button _weekbutton) {
        AdWeekButton adapter = new AdWeekButton();
        adapter.callback = _callback;
        adapter.weekbutton = _weekbutton;
        return adapter;
    }

    void setWeek(int _week) {
        // only used for initialising the UI
        week = _week;
        weekbutton.setText(Integer.toString(week));
    }

    void setWeekAndDisplay(int _week) {
        week = _week;
        weekbutton.setText(Integer.toString(week));
        callback.initUI();
    }

    int getWeek() {
        return week;
    }

    @Override
    public void onClick(View v) {
        DWeekButton.newInstance(this).show(callback.getActivity().getFragmentManager(), null);
    }
}
