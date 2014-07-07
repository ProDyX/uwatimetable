package com.github.marco9999.uwatimetable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class WListBaseAdapter extends BaseAdapter
{
	static final String ERRTAG = "uwatimetable";
	private static final String KEY_DISPLAYALLCLASSES = "DISPLAYALLCLASSES";
	private static final String KEY_DISPLAYID = "option_display_id";

	private ArrayList<String[]> classeslist;
	private final MainActivity mainactivity;
	
	WListBaseAdapter(MainActivity _mainactivity) {
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
		for (int i = 1; i<singleclass.length; i++) {
			layoutpos = ClassesFields.FIELD_VIEW_MAP[i];
			if (layoutpos != null) {
				((TextView) classview.findViewById(layoutpos)).setText(singleclass[i]);
			}
		}
		
		// fill in weeks if selected
		if(mainactivity.uisharedpref.getBoolean(KEY_DISPLAYALLCLASSES, false)) {
			classview.findViewById(R.id.title_weeks).setVisibility(View.VISIBLE);
			classview.findViewById(R.id.weeks).setVisibility(View.VISIBLE);
			
			// weeks index from ClassesFields definition
			((TextView) classview.findViewById(R.id.weeks)).setText(singleclass[ClassesFields.FIELD_INDEX_WEEKS]);
		}
		
		// fill in id if selected
		if(mainactivity.uisharedpref.getBoolean(KEY_DISPLAYID, false)) {
			classview.findViewById(R.id.title_id).setVisibility(View.VISIBLE);
			classview.findViewById(R.id.id_number).setVisibility(View.VISIBLE);
			
			// id index from ClassesFields definition
			((TextView) classview.findViewById(R.id.id_number)).setText(singleclass[ClassesFields.FIELD_INDEX_ID]);
		}
		
		return classview;
		
	}

	void setDataListAndNotify(ArrayList<String[]> list) {
		classeslist = list;
		this.notifyDataSetChanged();
    }
}
