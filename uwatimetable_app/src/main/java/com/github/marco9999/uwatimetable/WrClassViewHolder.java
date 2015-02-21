package com.github.marco9999.uwatimetable;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Marco on 21/02/2015.
 */
class WrClassViewHolder extends RecyclerView.ViewHolder {
    ViewGroup rootview;

    WrClassViewHolder(View itemview) {
        super(itemview);
        rootview = (ViewGroup) itemview;
    }
}
