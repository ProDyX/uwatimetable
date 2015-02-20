package com.github.marco9999.uwatimetable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marco on 19/11/2014.
 */

public class AdOverviewClassesList extends RecyclerView.Adapter<WrClassViewHolder> {

    private AMain mainactivity;
    private ViewGroup overviewcont;
    private ArrayList<String[]> classeslist;

    public AdOverviewClassesList() {
        classeslist = new ArrayList<String[]>();
    }

    public static AdOverviewClassesList newInstance(AMain _mainactivity, ViewGroup _overviewcont) {
        AdOverviewClassesList adapter = new AdOverviewClassesList();
        adapter.mainactivity = _mainactivity;
        adapter.overviewcont = _overviewcont;
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
        if(mainactivity.uisharedpref.getBoolean(Key.DISPLAYALLCLASSES, false)) {
            classview.findViewById(R.id.title_weeks).setVisibility(View.VISIBLE);
            classview.findViewById(R.id.weeks).setVisibility(View.VISIBLE);
        } else {
            classview.findViewById(R.id.title_weeks).setVisibility(View.GONE);
            classview.findViewById(R.id.weeks).setVisibility(View.GONE);
        }

        // show id if selected
        if(mainactivity.uisharedpref.getBoolean(Key.DISPLAYID, false)) {
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

class WrClassViewHolder extends RecyclerView.ViewHolder {
    ViewGroup rootview;

    WrClassViewHolder(View itemview) {
        super(itemview);
        rootview = (ViewGroup) itemview;
    }
}