package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FUpcoming extends Fragment {
    AMain mainactivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upcoming_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mainactivity = (AMain) getActivity();

        mainactivity.dbhelperui.readAllUpcomingClass(HStatic.getWeekOfYearInt(), HStatic.getDayOfWeekString(), HStatic.getHourOfDayInt());
    }
}
