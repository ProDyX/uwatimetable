package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeleteEntryFragment extends ListFragment
{
	static final String ERRTAG = "uwatimetable";
	
	MainActivity mainactivity;
	ClassesDBHelperUI dbhelperui;
    HashMap<String,Boolean> checkboxstate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflate menu items
		inflater.inflate(R.menu.delete_entry, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
            case R.id.action_deletebyid:
                this.deleteidActionEvent();
                return true;
            case R.id.action_deleteselectedids:
                this.deleteselectedidsActionEvent();
                return true;
		}
		return false;
	}



    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// set callback
		mainactivity = (MainActivity) getActivity();
		dbhelperui = mainactivity.dbhelperui;

        // set adapter
        setListAdapter(new WDeleteBaseAdapter(mainactivity, new ArrayList<String[]>(), this));
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delete_entry_fragment, container, false);
    }

    @Override
	public void onResume() {
		super.onResume();

        ListView root = getListView();

        // set visibility of "set display all" textview to false
        root.getEmptyView().findViewById(R.id.noclasses_displayall).setVisibility(View.GONE);
		
		// set title
		mainactivity.getActionBar().setSubtitle(R.string.title_delete_entry);
    	
		// show menu items
		setMenuVisibility(true);

        // init list
        initList();
	}

    void initList() {
        // reinit ui
        WDeleteBaseAdapter adapter = (WDeleteBaseAdapter) getListAdapter();
        ArrayList<String[]> array = dbhelperui.readAllEntries();
        checkboxstate = new HashMap<String, Boolean>(array.size());
        for(String[] strarray : array) {
            checkboxstate.put(strarray[ClassesFields.FIELD_INDEX_ID], false);
        }
        adapter.classeslist = array;
        adapter.notifyDataSetChanged();
    }
	
	@Override
	public void onStop() {
		// hide menu items
		setMenuVisibility(false);
		
		super.onStop();
	}

	void deleteidActionEvent() {
		// build view
		final EditText et = new EditText(mainactivity);
		et.setHint(R.string.hint_enterid);
		et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		
		// build alert
		AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
		builder.setTitle(R.string.enterid);
		builder.setView(et);
		
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// string id value
				String value = et.getText().toString();
				
				if(dbhelperui.deleteClassFromDB(value)) {
					// display toast notifying success.
					String toastmsg = "Deleted Class.";
                    Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                } else {
                    // failure
                    String toastmsg = "Failure - ID doesn't exist.";
                    Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
                }
            }
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // failure
			String toastmsg = "Canceled.";
			Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
		  }
		});

		builder.show();
	}

    void deleteselectedidsActionEvent() {
        for(Map.Entry<String, Boolean> entry : checkboxstate.entrySet()) {
            if(entry.getValue()) {
                mainactivity.dbhelperui.deleteClassFromDB(entry.getKey());
            }
        }

        initList();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CheckBox cb = (CheckBox) v.findViewById(R.id.delete_entry_checkbox);
        String classid = ((String[])getListView().getItemAtPosition(position))[ClassesFields.FIELD_INDEX_ID];
        if (cb.isChecked()) {
            checkboxstate.put(classid, false);
            cb.setChecked(false);
        } else {
            checkboxstate.put(classid, true);
            cb.setChecked(true);
        }
    }
}

class WDeleteBaseAdapter extends BaseAdapter {

    MainActivity mainactivity;
    ArrayList<String[]> classeslist;
    DeleteEntryFragment deleteentryfrag;

    WDeleteBaseAdapter(MainActivity ma, ArrayList<String[]> list, DeleteEntryFragment def) {
        mainactivity = ma;
        classeslist = list;
        deleteentryfrag = def;
    }

    @Override
    public int getCount() {
        return classeslist.size();
    }

    @Override
    public Object getItem(int position) {
        return classeslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get layout inflater from parent
        LayoutInflater li = mainactivity.getLayoutInflater();

        // extract the single class from the list at the position specified
        String[] singleclass = classeslist.get(position);

        // check if parsing existing view
        ViewGroup classview = null;
        if (convertView == null) {
            // use new view
            if (singleclass != null) {
                classview = (ViewGroup) li.inflate(R.layout.delete_class_entry, parent, false);
            }
        } else {
            // use existing view
            classview = (ViewGroup) convertView;
        }

        // fill view with details
        ((TextView) classview.findViewById(R.id.delete_id)).setText(singleclass[ClassesFields.FIELD_INDEX_ID]);
        Integer layoutpos;
        for (int i = 1; i<singleclass.length; i++) {
            layoutpos = ClassesFields.FIELD_VIEW_MAP[i];
            if (layoutpos != null) {
                ((TextView) classview.findViewById(layoutpos)).setText(singleclass[i]);
            }
        }

        // set checkbox state for recycled views
        ((CheckBox) classview.findViewById(R.id.delete_entry_checkbox)).setChecked(deleteentryfrag.checkboxstate.get(singleclass[ClassesFields.FIELD_INDEX_ID]));

        return classview;
    }
}