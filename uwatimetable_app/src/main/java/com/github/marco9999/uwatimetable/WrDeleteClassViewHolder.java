package com.github.marco9999.uwatimetable;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.HashMap;

/**
 * Created by Marco on 21/02/2015.
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


