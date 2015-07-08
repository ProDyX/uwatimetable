package com.github.marco9999.uwatimetable;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.HashMap;

/**  uwatimetable/WrDeleteClassViewHolder: Wrapper class for holding a view used within various RecyleViews (delete classes fragment).
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

class WrDeleteClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ViewGroup rootview;
    String classid;
    HashMap<String, Boolean> checkboxstate;

    WrDeleteClassViewHolder(View itemview, HashMap<String, Boolean> _checkboxstate) {
        super(itemview);
        rootview = (ViewGroup) itemview;
        rootview.setOnClickListener(this);
        checkboxstate = _checkboxstate;
    }

    @Override
    public void onClick(View v) {
        CheckBox cb = (CheckBox) v.findViewById(R.id.delete_entry_checkbox);
        if (cb.isChecked()) {
            checkboxstate.put(classid, false);
            cb.setChecked(false);
        } else {
            checkboxstate.put(classid, true);
            cb.setChecked(true);
        }
    }

    public void setCBState(HashMap<String, Boolean> _checkboxstate) {
        checkboxstate = _checkboxstate;
    }
}


