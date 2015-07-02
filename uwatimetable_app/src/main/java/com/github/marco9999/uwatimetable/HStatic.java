package com.github.marco9999.uwatimetable;

import android.content.ContentValues;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;
// import android.content.Context;
// import android.widget.Toast;

class HStatic {

	static int getWeekOfYearInt() {
        // get week of the year from calendar

        // this is used for determining if the next week should be shown instead of current week (as classes finish on friday)
        int add1 = 0;
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) add1 = 1;

        return (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + add1);
	}

	static int getDayOfWeekInt() {
        // note: Special case for saturday and sunday -> default to monday.
        // Sunday = 1 -> Saturday = 7 from calendar, but this function goes from Monday = 0 -> Friday = 4.
		// This array is based on weekdays_array from strings.xml
		final int[] days = new int[] {0,0,1,2,3,4,0};
		
		// Return int from array
		return days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
	}

    static int getDayOfWeekIntAll() {
        // needed for upcoming fragment (workaround)
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    static boolean nextWeek() {
        // function to return true if day = saturday.
        // used for determining if the next weekview should be shown instead of current weekview (as classes finish on friday)
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) return true;
        else return false;
    }

    static int getHourOfDayInt() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    static boolean hasClassAlreadyPast(String dayidxstring, int timeidx) {
        // needed for upcoming fragment (workaround)
        int dayidx = getIntFromStringDayAll(dayidxstring);
        int day = getDayOfWeekIntAll();
        int time = getHourOfDayInt();
        Log.d(LogTag.APP, "[hasclassalreadypassed called] " + "dayidxstring: " + dayidxstring + ", dayidx: " + dayidx + ", day: " + day + ", timeidx: " + timeidx + ", time: " + time);
        if (day > dayidx) return true;
        else if ((day == dayidx) && (time > timeidx)) return true;
        return false;
    }

    static int getIntFromStringDayAll(String daytocheck) {
        // needed for upcoming fragment (workaround)
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return (Arrays.asList(days).indexOf(daytocheck) + 1);
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
