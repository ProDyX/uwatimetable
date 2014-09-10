package com.github.marco9999.uwatimetable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class WClassesBaseAdapter extends BaseAdapter {

	private ArrayList<String[]> classeslist;
	private final AMain mainactivity;
	
	WClassesBaseAdapter(AMain _mainactivity) {
		super();
		mainactivity = _mainactivity;
		classeslist = new ArrayList<String[]>(); // just create an empty arraylist to keep BaseAdapter methods happy
	}
	
	@Override
	public boolean isEmpty() {
		return classeslist.isEmpty();
	}
	
	@Override
	public int getCount()
	{
		return classeslist.size();
	}

	@Override
	public Object getItem(int position)
	{
		return classeslist.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// get layout inflater from parent
		LayoutInflater li = mainactivity.getLayoutInflater();
		
		// extract the single class from the list at the position specified
		String[] singleclass = classeslist.get(position);
        assert singleclass != null;
		
		// check if parsing existing view
		ViewGroup classview = null;
		if (convertView == null) {
			classview = (ViewGroup) li.inflate(R.layout.class_entry, parent, false);
		} else {
			// use existing view
			classview = (ViewGroup) convertView;
		}
		
		// fill view with details
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
		
		return classview;
		
	}

	void setDataListAndNotify(ArrayList<String[]> list) {
		classeslist = list;
		this.notifyDataSetChanged();
    }
}
