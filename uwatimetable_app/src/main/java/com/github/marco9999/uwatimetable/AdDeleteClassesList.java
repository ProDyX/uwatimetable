package com.github.marco9999.uwatimetable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marco on 21/02/2015.
 */
class AdDeleteClassesList  extends RecyclerView.Adapter<WrDeleteClassViewHolder> {

    private AMain mainactivity;
    ArrayList<String[]> classeslist;
    HashMap<String, Boolean> checkboxstate;

    public AdDeleteClassesList () {
        classeslist = new ArrayList<String[]>();
    }

    public static AdDeleteClassesList newInstance(AMain _mainactivity) {
        AdDeleteClassesList adapter = new AdDeleteClassesList();
        adapter.mainactivity = _mainactivity;
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