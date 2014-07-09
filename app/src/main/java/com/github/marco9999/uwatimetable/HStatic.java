package com.github.marco9999.uwatimetable;

import android.content.ContentValues;

import java.util.Calendar;
// import android.content.Context;
// import android.widget.Toast;

class HStatic {
	
	static int getWeekOfYear() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}

// --Commented out by Inspection START (7/07/2014 4:54 PM):
//	static String getDayOfWeek() {
//		// note: Special case for saturday and sunday -> default to monday. Sunday = 1 -> Saturday = 7
//		final String[] days = new String[] {"Monday","Monday","Tuesday","Wednesday","Thursday","Friday","Monday"};
//
//		// Return the day using calendar class
//		return days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
//	}
// --Commented out by Inspection STOP (7/07/2014 4:54 PM)

	static int getDayOfWeekInt() {
		// This array is based on weekdays_array from strings.xml, using the note from in the string version of this function 
		final int[] days = new int[] {0,0,1,2,3,4,0};
		
		// Return int from array
		return days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
	}

	static ContentValues createTestEntry() {
		// (test) create a new map of key -> values, to be used in storing in db
		// (ContentValues class)
		ContentValues testval = new ContentValues();
		testval.put(ClassesFields.COLUMN_DAY, "Friday");
		testval.put(ClassesFields.COLUMN_TIME, 12);
		testval.put(ClassesFields.COLUMN_UNIT, "ENSC3001");
		testval.put(ClassesFields.COLUMN_TYPE, "Lecture");
		testval.put(ClassesFields.COLUMN_STREAM, 1);
		testval.put(ClassesFields.COLUMN_WEEKS, "Sem1");
		testval.put(ClassesFields.COLUMN_VENUE, "Physics: Ross Lecture Theatre");
		return testval;
	}

// --Commented out by Inspection START (7/07/2014 4:54 PM):
//	void DEBUGImplementToast(Context context) {
//		// display toast notifying success.
//		String toastmsg = "TODO: Implement";
//		Toast.makeText(context, toastmsg, Toast.LENGTH_LONG).show();
//	}
// --Commented out by Inspection STOP (7/07/2014 4:54 PM)

}
