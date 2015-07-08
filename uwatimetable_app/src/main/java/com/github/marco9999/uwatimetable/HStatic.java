package com.github.marco9999.uwatimetable;

import android.content.ContentValues;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;
// import android.content.Context;
// import android.widget.Toast;

/**  uwatimetable/HStatic: Helper class containing various system functions needed (mostly date and time).
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

    // NO LONGER NEEDED?
//    static boolean nextWeek() {
//        // function to return true if day = saturday.
//        // used for determining if the next weekview should be shown instead of current weekview (as classes finish on friday)
//        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) return true;
//        else return false;
//    }

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
