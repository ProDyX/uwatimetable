package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**  uwatimetable/FUpcoming: Fragment class for the upcoming functionality.
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

public class FUpcoming extends Fragment {
    AMain mainactivity;
    ViewGroup fcontainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // extract container
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.upcoming_fragment, container, false);
        fcontainer = (ViewGroup) vg.findViewById(R.id.upcoming_container);
        return vg;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get mainactivity
        mainactivity = (AMain) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflate menu items
        inflater.inflate(R.menu.upcoming, menu);
    }

    @Override
    public void onResume() {
        super.onResume();

        // set title
        mainactivity.getActionBar().setSubtitle(R.string.title_upcoming);

        // init ui
        initUI();
    }

    public void initUI() {
        // dynamicly build ui based on number of types returned.

        // read upcoming classes into hashmap
        HashMap<String, String[]> upcoming = mainactivity.dbhelperui.readAllUpcomingClass(HStatic.getWeekOfYearInt());

        // loop through hashmap, based on type array from ClassesFields
        ViewGroup entry;
        String[] extclass;
        for (String[] typearray : ClassesFields.TYPE_CLASSES_ARRAY) {
            // get entry details by extracting from hashmap based on first type
            extclass = upcoming.get(typearray[0]);

            // continue if no match was found
            if (extclass == null) continue;

            // else make a new entry view
            entry = (ViewGroup) mainactivity.getLayoutInflater().inflate(R.layout.entry_upcoming_fragment, fcontainer, false);

            // set title and day string
            ((TextView) entry.findViewById(R.id.upcoming_day)).setText(extclass[ClassesFields.FIELD_INDEX_DAYS]);
            ((TextView) entry.findViewById(R.id.upcoming_title)).setText(typearray[0]);

            // set day and weeks to visible
            entry.findViewById(R.id.delete_day).setVisibility(View.GONE);
            entry.findViewById(R.id.day_week_divider).setVisibility(View.GONE);
            entry.findViewById(R.id.title_weeks).setVisibility(View.VISIBLE);
            entry.findViewById(R.id.weeks).setVisibility(View.VISIBLE);

            // fill view with details
            Integer layoutpos;
            for (int i = 0; i<extclass.length; i++) {
                layoutpos = ClassesFields.FIELD_VIEW_MAP_UPCOMING[i];
                if (layoutpos != null) {
                    ((TextView) entry.findViewById(layoutpos)).setText(extclass[i]);
                }
            }

            // add to container
            fcontainer.addView(entry);
        }

    }
}
