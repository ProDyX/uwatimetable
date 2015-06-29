package com.github.marco9999.uwatimetable;

import android.content.ContentValues;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.github.marco9999.htmlparserolcr.EOlcrHtmlParser;

import java.util.ArrayList;

class HClassesWebViewOlcrImporter {

    private final AMain context;
	private final HClassesDbUI dbhelper;

	// Dummy class for javascript html source hack
	HClassesWebViewOlcrImporter(AMain _context) {
		context = _context;
		dbhelper = context.dbhelperui;
	}

	@JavascriptInterface
    public void processHTML(String URL, String html) {
        String timetablepage = "studenttimetable.jsp";
        String timetableaction = "v_action=2";
        if(URL.contains(timetablepage) && URL.contains(timetableaction)) {
			// Get list
			ArrayList<String> classlist = new EOlcrHtmlParser().getClassList(html);
			
			// put into contentvalues array
			ArrayList<ContentValues> arraycv = new ArrayList<ContentValues>();
			Log.i(LogTag.OLCR, "-------------------Start Classes Listing-------------------");
			for(String str : classlist) {
				if(str == null) {
					Log.i(LogTag.OLCR, "(null)");
				} else {
					Log.i(LogTag.OLCR, str);
				}
				arraycv.add(dbhelper.createClassesCV(dbhelper.readEntryFromLine(str)));
			}
			Log.i(LogTag.OLCR, "-------------------End Classes Listing-------------------");
			
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