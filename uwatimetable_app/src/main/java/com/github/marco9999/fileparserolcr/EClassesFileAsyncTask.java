package com.github.marco9999.fileparserolcr;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.marco9999.uwatimetable.HClassesFileOLCRImporter;

import java.util.ArrayList;

/**  fileparserolcr/EClassesFileAsyncTask: Engine class for reading in class entries through a HClassesFileReader object (stores them in the database).
 *    Copyright (C) 2015 Marco Satti
 *    Contact: marcosatti@gmail.com
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
