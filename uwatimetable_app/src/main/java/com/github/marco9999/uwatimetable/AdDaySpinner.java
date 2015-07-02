package com.github.marco9999.uwatimetable;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Marco on 2/07/2015.
 * Simple adapter wrapper class for the day spinner (string arrays)
 */
public class AdDaySpinner extends ArrayAdapter<String> implements AdapterView.OnItemSelectedListener {
    private int day;
    private FMainOverview callback;
    private Spinner dayspinner;
    private int arrayid;

    AdDaySpinner(Context context, int textViewResId, String[] strings) {
        super(context, textViewResId, strings);
    }

    public static AdDaySpinner newInstance(FMainOverview _callback, Spinner _dayspinner, int textArrayResId, int textViewResId) {
        Context context = _callback.getActivity();
        String[] strings = (String[]) context.getResources().getStringArray(textArrayResId);
        AdDaySpinner adapter = new AdDaySpinner(context, textViewResId, strings);
        adapter.callback = _callback;
        adapter.arrayid = textArrayResId;
        adapter.dayspinner = _dayspinner;
        return adapter;
    }

    void setDay(int _day) {
        // only used for initialising the UI
        day = _day;
        dayspinner.setSelection(day, false);
    }

    void setDayAndDisplay(int _day) {
        day = _day;
        dayspinner.setSelection(day, false);
        callback.initUI();
    }

    int getDay() {
        return day;
    }

    String getDayString() {
        Context context = callback.getActivity();
        String[] strings = (String[]) context.getResources().getStringArray(arrayid);
        return strings[day];
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setDayAndDisplay(position);
    }

    public void onNothingSelected(AdapterView<?> parent) {}
}
