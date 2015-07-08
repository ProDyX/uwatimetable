package com.github.marco9999.uwatimetable;

import android.content.ContentValues;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.github.marco9999.htmlparserolcr.EOlcrHtmlParser;

import java.util.ArrayList;

/**  uwatimetable/HClassesWebViewOlcrImporter: Helper class which contains the javascript interface hack for processing WebView OLCR results.
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