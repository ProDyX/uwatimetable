package com.github.marco9999.uwatimetable;

import android.view.View;
import android.widget.Button;

/**  uwatimetable/AdWeekButton: (Pseudo) Adapter class in order to store current weekview data, and set it properly.
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
        DWeekButton.newInstance(this).show(callback.getActivity().getFragmentManager(), Tag.DIALOG_WEEK);
    }
}
