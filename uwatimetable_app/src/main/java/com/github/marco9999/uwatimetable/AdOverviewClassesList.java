package com.github.marco9999.uwatimetable;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**  uwatimetable/AdOverviewClassesList:  Adapter class for the RecyclerView displayed in the overview fragment.
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

public class AdOverviewClassesList extends RecyclerView.Adapter<WrClassViewHolder> {

    private FMainOverview callback;
    private ArrayList<String[]> classeslist;

    public AdOverviewClassesList() {
        classeslist = new ArrayList<String[]>();
    }

    public static AdOverviewClassesList newInstance(FMainOverview _callback) {
        AdOverviewClassesList adapter = new AdOverviewClassesList();
        adapter.callback = _callback;
        return adapter;
    }

    public void setClasseslistAndNotify(ArrayList<String[]> clist) {
        classeslist = clist;
        this.notifyDataSetChanged();
    }

    @Override
    public WrClassViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_entry, parent, false);
        return new WrClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WrClassViewHolder holder, int position) {
        // fill view with details
        final String[] singleclass = classeslist.get(position);
        final ViewGroup classview = holder.rootview;

        Integer layoutpos;
        for (int i = 0; i<singleclass.length; i++) {
            layoutpos = ClassesFields.FIELD_VIEW_MAP_OVERVIEW[i];
            if (layoutpos != null) {
                ((TextView) classview.findViewById(layoutpos)).setText(singleclass[i]);
            }
        }

        // show weeks if selected
        SharedPreferences preferences = ((AMain) callback.getActivity()).uisharedpref;
        if(preferences.getBoolean(Key.DISPLAYALLCLASSES, false)) {
            classview.findViewById(R.id.title_weeks).setVisibility(View.VISIBLE);
            classview.findViewById(R.id.weeks).setVisibility(View.VISIBLE);
        } else {
            classview.findViewById(R.id.title_weeks).setVisibility(View.GONE);
            classview.findViewById(R.id.weeks).setVisibility(View.GONE);
        }

        // show id if selected
        if(preferences.getBoolean(Key.DISPLAYID, false)) {
            classview.findViewById(R.id.title_id).setVisibility(View.VISIBLE);
            classview.findViewById(R.id.id_number).setVisibility(View.VISIBLE);
        } else {
            classview.findViewById(R.id.title_id).setVisibility(View.GONE);
            classview.findViewById(R.id.id_number).setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return classeslist.size();
    }
}

