package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FDeleteEntry extends Fragment
{
	private AMain mainactivity;
	private HClassesDbUI dbhelperui;
    HashMap<String,Boolean> checkboxstate;
    private RecyclerView classeslist;
    private AdDeleteClassesList classadapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.delete_entry_fragment, container, false);

        // get classes listview;
        classeslist = (RecyclerView) vg.findViewById(R.id.delete_classeslist);

        return vg;
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// set callback
		mainactivity = (AMain) getActivity();
		dbhelperui = mainactivity.dbhelperui;

        // set classeslist display manager and adapter
        classeslist.setLayoutManager(new LinearLayoutManager(mainactivity));
        classadapter = AdDeleteClassesList.newInstance(mainactivity);
        classeslist.setAdapter(classadapter);

        // set callback in dialog for configuration change
        DDeleteSelIDActionEvent dialog;
        if((dialog = (DDeleteSelIDActionEvent) mainactivity.getFragmentManager().findFragmentByTag(Tag.DIALOG_DELSELID)) != null) dialog.setCallback(this); // handle config change... TODO: there must be a better way surely..
	}

    @Override
	public void onResume() {
		super.onResume();

        // set visibility of "set display all" textview to false
        classeslist.getRootView().findViewById(R.id.noclasses_displayall).setVisibility(View.GONE);
		
		// set title
        mainactivity.getActionBar().setSubtitle(R.string.title_delete_entry);

        // show menu items
		setMenuVisibility(true);

        // init list
        initUI();
	}

    void initUI() {
        // reinit ui
        ArrayList<String[]> array = dbhelperui.readAllEntries();
        checkboxstate = new HashMap<String, Boolean>(array.size());
        classadapter.setCBState(checkboxstate);

        // check for no class entries
        assert getView() != null;
        ViewGroup vg = (ViewGroup) getView().findViewById(R.id.empty_classes_list);
        if (array.isEmpty()) {
            vg.setVisibility(View.VISIBLE);
            vg.findViewById(R.id.noclasses_displayall).setVisibility(View.GONE);
        } else {
            vg.setVisibility(View.GONE);
            for(String[] strarray : array) {
                checkboxstate.put(strarray[ClassesFields.FIELD_INDEX_ID], false);
            }
        }
        classadapter.setClasseslistAndNotify(array);
    }
	
	@Override
	public void onStop() {
		// hide menu items
		setMenuVisibility(false);
		
		super.onStop();
	}

	void deleteidActionEvent() {
        DDeleteIDActionEvent.newInstance().show(mainactivity.getFragmentManager(), null);
	}

    void deleteselectedidsActionEvent() {
        // put id's into array
        ArrayList<String> ids = new ArrayList<String>();
        for(Map.Entry<String, Boolean> entry : checkboxstate.entrySet()) {
            if(entry.getValue()) {
               ids.add(entry.getKey());
            }
        }
        String[] idarray = ids.toArray(new String[ids.size()]);

        // create dialog fragment and show
        DDeleteSelIDActionEvent.newInstance(idarray,this).show(mainactivity.getFragmentManager(), Tag.DIALOG_DELSELID);

    }

}

