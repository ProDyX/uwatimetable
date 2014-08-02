package com.github.marco9999.uwatimetable;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LNavDrawerItemClick implements ListView.OnItemClickListener {

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
            if(fragmentstr.equals(Tag.FRAGMENT_OVERVIEW)) {
                mainactivity.getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .replace(R.id.fragment_holder, new FMainOverview(), Tag.FRAGMENT_OVERVIEW)
                        .commit();
            } else if (fragmentstr.equals(Tag.FRAGMENT_UPCOMING)) {
                // debug
                Toast.makeText(mainactivity, "TODO: Implement Upcoming Fragment", Toast.LENGTH_LONG).show();
                mainactivity.drawerlist.setItemChecked(0, true);
                mainactivity.drawerlayout.closeDrawer(mainactivity.drawerlist);
                return;
            }
        }

        // set checked item
        mainactivity.drawerlist.setItemChecked(position, true);

        // close the drawer
        mainactivity.drawerlayout.closeDrawer(mainactivity.drawerlist);
    }
}
