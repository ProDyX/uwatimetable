package com.github.marco9999.fileparserolcr;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.marco9999.uwatimetable.HClassesFileOLCRImporter;

import java.util.ArrayList;

public class EClassesFileAsyncTask extends AsyncTask<Void, Integer, Void>
{

    private final HClassesFileOLCRImporter callback;
	
	public EClassesFileAsyncTask(HClassesFileOLCRImporter _callback) {
		super();
		callback = _callback;
	}
	
	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		ArrayList<ContentValues> arraycv = new ArrayList<ContentValues>();
		
		// open file helper, check for error (also done in class)
		HClassesFileReader filehelper = new HClassesFileReader();
		if(filehelper.openClassesDBFile()) {
			filehelper.openBufferedReaderFile();
		} else {
			return null;
		}
		
		int count = 0;
		while(filehelper.moveToNextLine()) {
			arraycv.add(callback.dbhelperui.createClassesCV(callback.dbhelperui.readEntryFromLine(filehelper.getLine())));
			count++;
		}
		filehelper.closeFile();
		
		// Write to db
		callback.dbhelperui.writeClassToDB(arraycv.toArray(new ContentValues[arraycv.size()]));
		
		// progress update
		publishProgress(count);

		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.i(LogTag.fileparserolcr, "Read from file complete");
	}
	
	@Override
	protected void onProgressUpdate(Integer... result) {
		// only called one time at end of file read
		// display toast notifying success.
		String toastmsg = "Read in " + result[0] + " class entries from file.";
		Toast.makeText(callback.mainactivity, toastmsg, Toast.LENGTH_LONG).show();
	}
	
}
