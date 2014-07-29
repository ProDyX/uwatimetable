package com.github.marco9999.uwatimetable;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LNavDrawerItemClick implements ListView.OnItemClickListener {

    private static final String TAG_FRAGMENT_OVERVIEW = "Overview";
    private static final String TAG_FRAGMENT_UPCOMING = "Upcoming";

    private AMain mainactivity;

    LNavDrawerItemClick(AMain mainactivity) {
        this.mainactivity = mainactivity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get nav fragment selected
        TextView item = (TextView) view;
        String fragmentstr = (String) item.getText();

        // check if its already on the stack
        if(mainactivity.getFragmentManager().findFragmentByTag(fragmentstr) == null) {
            if(fragmentstr.equals(TAG_FRAGMENT_OVERVIEW)) {
                mainactivity.getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .replace(R.id.fragment_holder, new FMainOverview(), TAG_FRAGMENT_OVERVIEW)
                        .commit();
            } else if (fragmentstr.equals(TAG_FRAGMENT_UPCOMING)) {
                Toast.makeText(mainactivity, "Implement Upcoming Fragment", Toast.LENGTH_LONG).show();
            }
        }
    }
}
