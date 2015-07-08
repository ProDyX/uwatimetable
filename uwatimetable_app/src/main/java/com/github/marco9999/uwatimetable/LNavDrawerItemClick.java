package com.github.marco9999.uwatimetable;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**  uwatimetable/LNavDrawerItemClick: Listener class for the navigation drawer.
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

public class LNavDrawerItemClick implements ListView.OnItemClickListener {

    private AMain mainactivity;

    LNavDrawerItemClick(AMain mainactivity) {
        this.mainactivity = mainactivity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get nav fragment selected
        String fragmentstr = (String) parent.getAdapter().getItem(position);

        // manage fragments
        // see if fragment is already on the stack by using findByTag, if not then we proceed to replace the old one with it.
        if(mainactivity.getFragmentManager().findFragmentByTag(fragmentstr) == null) {
            if(fragmentstr.equals(Tag.FRAGMENT_OVERVIEW)) {
                mainactivity.getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_holder, new FMainOverview(), Tag.FRAGMENT_OVERVIEW)
                        .commit();
            } else if (fragmentstr.equals(Tag.FRAGMENT_UPCOMING)) {
                mainactivity.getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_holder, new FUpcoming(), Tag.FRAGMENT_UPCOMING)
                        .commit();
            }
        }

        // set checked item
        mainactivity.drawerlist.setItemChecked(position, true);

        // close the drawer
        mainactivity.drawerlayout.closeDrawer(mainactivity.drawerlist);
    }
}
