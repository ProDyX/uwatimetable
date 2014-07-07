package com.github.marco9999.uwatimetable;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

class ClassesFileAsyncTask extends AsyncTask<Void, Integer, Void>
{
	private static final String ERRTAG = "uwatimetable";
	
	private final MainActivity mainactivity;
	private final ClassesDBHelperUI dbhelperui;
	
	public ClassesFileAsyncTask(MainActivity _mainactivity) {
		super();
		mainactivity = _mainactivity;
		dbhelperui = mainactivity.dbhelperui;
	}
	
	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		ArrayList<ContentValues> arraycv = new ArrayList<ContentValues>();
		
		// open file helper, check for error (also done in class)
		ClassesFileHelper filehelper = new ClassesFileHelper();
		if(filehelper.openClassesDBFile()) {
			filehelper.openBufferedReaderFile();
		} else {
			return null;
		}
		
		int count = 0;
		while(filehelper.moveToNextLine()) {
			arraycv.add(dbhelperui.createClassesCV(dbhelperui.readEntryFromLine(filehelper.getLine())));
			count++;
		}
		filehelper.closeFile();
		
		// Write to db
		dbhelperui.writeClassToDB(arraycv.toArray(new ContentValues[arraycv.size()]));
		
		// progress update
		publishProgress(count);
		

		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.i(ERRTAG, "Read from file complete");
	}
	
	@Override
	protected void onProgressUpdate(Integer... result) {
		// only called one time at end of file read
		// display toast notifying success.
		String toastmsg = "Read in " + result[0] + " class entries from file.";
		Toast.makeText(mainactivity, toastmsg, Toast.LENGTH_LONG).show();
	}
	
}
