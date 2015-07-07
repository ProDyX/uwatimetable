package com.github.marco9999.uwatimetable;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**  uwatimetable/AdDaySpinner: (Pseudo) Adapter wrapper class for the day spinner. Provides extra functionality over the ArrayAdapter used previously.
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
