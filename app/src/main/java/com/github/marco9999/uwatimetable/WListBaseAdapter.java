package com.github.marco9999.uwatimetable;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WListBaseAdapter extends BaseAdapter
{
	static final String ERRTAG = "uwatimetable";
	static final String KEY_DISPLAYALLCLASSES = "DISPLAYALLCLASSES";
	static final String KEY_DISPLAYID = "option_display_id";

	ArrayList<String[]> classeslist;
	MainActivity mainactivity;
	
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
		
		// check if parsing existing view
		ViewGroup classview = null;
		if (convertView == null) {
			// inflate a new view
			
			// TODO: unfortunately this is a fairly slow operation with many entries, and the only way to fix this is 
			// to rewrite a bit of the program -- however there would not be too many people with a lot of classes on a day,
			// so not really important to look at for now.
			//
			// currently this is inflating the same views for each entry repeatedly and not recycling the old ones (which
			// are exactly the same in contents).  
			if (singleclass != null) {
				classview = (ViewGroup) li.inflate(R.layout.class_entry, parent, false);
			}
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
