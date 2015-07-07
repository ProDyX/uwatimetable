package com.github.marco9999.uwatimetable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**  uwatimetable/AdDeleteClassesList: Adapter class for the RecyclerView displayed in the delete classes menu.
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

class AdDeleteClassesList  extends RecyclerView.Adapter<WrDeleteClassViewHolder> {

    ArrayList<String[]> classeslist;
    HashMap<String, Boolean> checkboxstate;

    public AdDeleteClassesList () {
        classeslist = new ArrayList<String[]>();
    }

    public static AdDeleteClassesList newInstance() {
        AdDeleteClassesList adapter = new AdDeleteClassesList();
        return adapter;
    }

    @Override
    public int getItemCount() {
        return classeslist.size();
    }

    @Override
    public WrDeleteClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_class_entry, parent, false);
        return new WrDeleteClassViewHolder(view, checkboxstate);
    }

    @Override
    public void onBindViewHolder(WrDeleteClassViewHolder holder, int position) {
        // extract the single class from the list at the position specified
        String[] singleclass = classeslist.get(position);

        // get the classview from holder and set class id
        ViewGroup classview = holder.rootview;
        holder.classid = singleclass[ClassesFields.FIELD_INDEX_ID];

        // set day and weeks to visible
        classview.findViewById(R.id.delete_day).setVisibility(View.VISIBLE);
        classview.findViewById(R.id.day_week_divider).setVisibility(View.VISIBLE);
        classview.findViewById(R.id.title_weeks).setVisibility(View.VISIBLE);
        classview.findViewById(R.id.weeks).setVisibility(View.VISIBLE);

        // fill view with details
        Integer layoutpos;
        for (int i = 0; i<singleclass.length; i++) {
            layoutpos = ClassesFields.FIELD_VIEW_MAP_DELENT[i];
            if (layoutpos != null) {
                ((TextView) classview.findViewById(layoutpos)).setText(singleclass[i]);
            }
        }

        // set checkbox state for recycled views
        holder.setCBState(checkboxstate);
        Boolean cb = checkboxstate.get(singleclass[ClassesFields.FIELD_INDEX_ID]);
        if (cb == null) cb = false;
        ((CheckBox) classview.findViewById(R.id.delete_entry_checkbox)).setChecked(cb);

    }

    public void setClasseslistAndNotify(ArrayList<String[]> clist) {
        classeslist = clist;
        this.notifyDataSetChanged();
    }

    public void setCBState(HashMap<String, Boolean> _checkboxstate) {
        checkboxstate = _checkboxstate;
    }
}