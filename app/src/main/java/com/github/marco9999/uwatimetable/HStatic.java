package com.github.marco9999.uwatimetable;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
// import android.content.Context;
// import android.widget.Toast;

class HStatic {

	static int getWeekOfYearInt() {
        // special case for if its saturday, as we have set the program to return monday (of next week) if its the weekend...
        // but whilst sunday is already the start of next week, saturday is still considered as part of the current week.
        // thus for saturday, just return the current week + 1.
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + 1;
        }
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}

	static int getDayOfWeekInt() {
        // note: Special case for saturday and sunday -> default to monday. Sunday = 1 -> Saturday = 7
		// This array is based on weekdays_array from strings.xml
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

}
