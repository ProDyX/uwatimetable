package com.github.marco9999.uwatimetable;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteEntryFragment extends Fragment 
{
	static final String ERRTAG = "uwatimetable";
	
	MainActivity mainactivity;
	ClassesDBHelperUI dbhelperui;

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
		}
		return false;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// set callback
		mainactivity = (MainActivity) getActivity();
		dbhelperui = mainactivity.dbhelperui;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// set title
		mainactivity.getActionBar().setSubtitle(R.string.title_delete_entry);
    	
		// show menu items
		setMenuVisibility(true);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final ViewGroup rootvg = (ViewGroup) inflater.inflate(R.layout.delete_entry_fragment, container, false);
        return rootvg;
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
	
}
