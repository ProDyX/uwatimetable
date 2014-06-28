package com.github.marco9999.uwatimetable;

import java.util.ArrayList;

import com.github.marco9999.htmlparserolcr.HtmlParserOlcr;

import android.content.ContentValues;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

class ProcessHtmlOlcr {
	
	static final String ERRTAG = "uwatimetable";
	final String timetablepage = "studenttimetable.jsp";
	final String timetableaction = "v_action=2";
	
	MainActivity context;
	ClassesDBHelperUI dbhelper;

	// Dummy class for javascript html source hack
	ProcessHtmlOlcr(MainActivity _context) {
		context = _context;
		dbhelper = context.dbhelperui;
	}

	@JavascriptInterface
    public void processHTML(String URL, String html) {
		if(URL.contains(timetablepage) && URL.contains(timetableaction)) {
			// Get list
			HtmlParserOlcr parser = new HtmlParserOlcr(html);
			ArrayList<String> classlist = parser.getClassList();
			
			// put into contentvalues array
			ArrayList<ContentValues> arraycv = new ArrayList<ContentValues>();
			Log.i("OLCR", "-------------------Start Classes Listing-------------------");
			for(String str : classlist) {
				if(str == null) {
					Log.i("OLCR", "(null)");
				} else {
					Log.i("OLCR", str);
				}
				arraycv.add(dbhelper.createClassesCV(dbhelper.readEntryFromLine(str)));
			}
			Log.i("OLCR", "-------------------End Classes Listing-------------------");
			
			// write and display db
			if(dbhelper.writeClassToDB(arraycv.toArray(new ContentValues[arraycv.size()]))) {
				// display toast notifying success.
		    	String toastmsg = "Got classes from OLCR successfully (" + arraycv.size() + " entries).";
				Toast.makeText(context, toastmsg, Toast.LENGTH_LONG).show();
			} else {
				// failed
				String toastmsg = "Failed to add classes. Please notify developer!";
				Toast.makeText(context, toastmsg, Toast.LENGTH_LONG).show();
			}
		}
    }
	
}