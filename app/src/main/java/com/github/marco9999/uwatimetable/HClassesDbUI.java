package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class HClassesDbUI {

	private final static String token = ":";

	private final SQLiteDatabase db;
	private final AMain mainactivity;

	HClassesDbUI(SQLiteDatabase _db, AMain _mainactivity) {
		db = _db; // Classes Db
		mainactivity = _mainactivity;
	}

	@SuppressLint("DefaultLocale") // we can assume it will be always english
	boolean isRelevantToWeek(String weektocheck, int currentweek) {
		// first check if display all is turned on
		if (mainactivity.uisharedpref.getBoolean(Key.DISPLAYALLCLASSES, false)) {
			return true;
		}
		
		// Constants
		// TODO: hardcoded values... need to see if we can pull from website (probably too much code to bother with)
		final int START_SEM1 = 9;
		final int END_SEM1 = 22;
		final int START_SEM2 = 31;
		final int END_SEM2 = 44;
		
		String weektochecklc = weektocheck.toLowerCase(); 
		if (weektochecklc.contains("sem")) {
			// which semester?
			String semester = weektochecklc.substring(3, 4);
			if (semester.equals("1")) {
				if (currentweek >= START_SEM1 && currentweek <= END_SEM1) {
                    //noinspection SimplifiableIfStatement
                    if (weektochecklc.contains("w2")) {
                        return !(currentweek == START_SEM1);
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else if (semester.equals("2")) {
				if (currentweek >= START_SEM2 && currentweek <= END_SEM2) {
                    //noinspection SimplifiableIfStatement
                    if (weektochecklc.contains("w2")) {
						return !(currentweek == START_SEM2);
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				// what the hell did we run into?
				Log.i(LogTag.APP, "Unknown class week encounted, returning false: " + weektocheck);
				return false;
			}
		} else {
			// Check for ranges or individual weeks: ie: 42-45,47-48 or 10,42 or 11 etc..
			String[] ranges = weektochecklc.split(",");
			for (String indranges : ranges) {
				if (indranges.contains("-")) {
					String[] indweeks = indranges.split("-");
					int limit1 = Integer.parseInt(indweeks[0]);
					int limit2 = Integer.parseInt(indweeks[1]);
					if (currentweek >= limit1 && currentweek <= limit2) {
						return true;
					}
				} else {
					int limit = Integer.parseInt(indranges);
					if (currentweek == limit) {
						return true;
					}
				}
			}
			// no matches
			return false;
		}
	}

	boolean writeClassToDB(ContentValues[] values) {
		// Insert the new rows, db must be writable (assumed)
		long retcode;
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				retcode = db.insert(ClassesFields.TABLE_NAME, ClassesFields.COLUMN_NULLABLE, values[i]);
				if (retcode == -1) {
					Log.e(LogTag.APP, "Insert into DB failure. Check input. (" + i + ")");
					return false;
				}
			}
		}
		return true;
	}
	
	boolean deleteClassFromDB(String id) {
		// delete specified row from id
		return (db.delete(ClassesFields.TABLE_NAME, ClassesFields._ID + "=" + id, null) > 0);
	}
	
	void recreateClassesDB() {
		db.execSQL(HClassesDbSQL.SQL_CREATE_ENTRIES); // Just create it to make sure we debug it right ;)
		db.execSQL(HClassesDbSQL.SQL_DELETE_ENTRIES);
		db.execSQL(HClassesDbSQL.SQL_CREATE_ENTRIES);
	}

	ArrayList<String[]> readRelevantEntries(String day, int week) {
		// DB assumed to be at least readable
		// Return a list of all the entries found, matching the day queried, then sort based on weeks
		ArrayList<String[]> returnedlist = new ArrayList<String[]>();
		String[] tempstr;

		// Create the selection sql string for the specified day
		String sqlselection = "day='" + day + "'";

		// Get the cursor results
		Cursor dbcursor = db.query(ClassesFields.TABLE_NAME, null, sqlselection, null, null, null, ClassesFields.COLUMN_TIME);

		if (!dbcursor.moveToFirst()) {
			Log.i(LogTag.APP, "Cursor from DB query is empty. Returning empty ArrayList.");
			return returnedlist;
		}

		// filter cursor out based on weeks, while copying to new arraylist
		for (int i = 0; i < dbcursor.getCount(); i++) {
			if (isRelevantToWeek(
					dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_WEEKS)),
					week)) {
				tempstr = new String[ClassesFields.NUM_INFO_COLS];
				tempstr[0] = Integer.toString(dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields._ID)));
				tempstr[1] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_DAY));
				tempstr[2] = Integer.toString(dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields.COLUMN_TIME))) + ":00";
				tempstr[3] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_UNIT));
				tempstr[4] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_TYPE));
				tempstr[5] = "(0" + Integer.toString(dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields.COLUMN_STREAM))) + ") - ";
				tempstr[6] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_WEEKS));
				tempstr[7] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_VENUE));
				returnedlist.add(tempstr);
				
				Log.i(LogTag.APP, "Class ID returned: " + tempstr[0]);
			}
			dbcursor.moveToNext();
		}

		return returnedlist;
	}

    HashMap<String, String[]> readUpcomingEntries(int currentweek, String currentday, int currenthour, int rangeweek) {
        // init hash map with capactity, assuming at least one of each class type
        HashMap<String, String[]> returnedmap = new HashMap<String, String[]>(ClassesFields.NUM_TYPE_CLASSES);

        return returnedmap;
    }

    ArrayList<String[]> readAllEntries() {
        ArrayList<String[]> returnedlist = new ArrayList<String[]>();

        Cursor dbcursor = db.query(ClassesFields.TABLE_NAME, null, null, null, null, null, ClassesFields._ID);
        if (!dbcursor.moveToFirst()) {
            Log.i(LogTag.APP, "Cursor from DB query is empty. Returning empty ArrayList.");
            return returnedlist;
        }

        String[] tempstr;
        for (int i = 0; i < dbcursor.getCount(); i++) {
            tempstr = new String[ClassesFields.NUM_INFO_COLS];
            tempstr[0] = Integer.toString(dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields._ID)));
            tempstr[1] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_DAY));
            tempstr[2] = Integer.toString(dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields.COLUMN_TIME))) + ":00";
            tempstr[3] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_UNIT));
            tempstr[4] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_TYPE));
            tempstr[5] = "(0" + Integer.toString(dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields.COLUMN_STREAM))) + ") - ";
            tempstr[6] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_WEEKS));
            tempstr[7] = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_VENUE));
            returnedlist.add(tempstr);
            dbcursor.moveToNext();
        }
        return returnedlist;
    }

	ContentValues createClassesCV(String[] svalues) {
		// values is an array containing the fields listed in ClassesFields
		ContentValues cvalues = new ContentValues();
		cvalues.put(ClassesFields.COLUMN_DAY, svalues[0]);
		cvalues.put(ClassesFields.COLUMN_TIME, Integer.parseInt(svalues[1]));
		cvalues.put(ClassesFields.COLUMN_UNIT, svalues[2]);
		cvalues.put(ClassesFields.COLUMN_TYPE, svalues[3]);
		cvalues.put(ClassesFields.COLUMN_STREAM, Integer.parseInt(svalues[4]));
		cvalues.put(ClassesFields.COLUMN_WEEKS, svalues[5]);
		cvalues.put(ClassesFields.COLUMN_VENUE, svalues[6]);
		return cvalues;
	}

	String[] readEntryFromLine(String line1) {
		// if we didnt have to mutate the string, we would have a very small function here.
		// instead, we need to remove the preference tag, AND join up the venue string as it 
		// may contain a colon(s) in it which will get split by the function below. there may be
		// a better way to do this, but it works for the UWA entries.
		
		// BUG: If there is no [pref] part AND there is a colon in the venue, then this function fails to return properly.
		//      Possible solution 1: check (? maybe we don't have to) for (no) [pref] and add a colon to the end of the string. <- accepted
		//               solution 2: check end of string for [pref] and remove it that way (replace).
		
		// SEE ClassesFields.java FOR WHAT THIS FUNCTION EXPECTS AS INPUT
		
		// fix for bug mentioned above (solution 1)
		String line;
		if(!line1.contains("[Pref")) {
			line = line1 + ": "; 
		} else {
			line = line1;
		}
		String[] splitline = line.split(token); // Java makes this part very easy :) 
		
		// since there will always be a total of 7 elements in the class entry 
		// (check ClassesDBFields for the listings), we can trim the end of the string array
		// and join up the elements >7 (<length-1) into the 7th element.
		String[] returnline = Arrays.copyOf(splitline, ClassesFields.NUM_INFO_COLS_NOID);
		if (splitline.length > (ClassesFields.NUM_INFO_COLS_NOID+1)) { // check if we have more than 8 elements (8 elements including the preferences part, 7 without) and do correction
			for(int i = ClassesFields.NUM_INFO_COLS_NOID; i<(splitline.length - 1); i++) { // the first part of the venue string should always be copied over, so we can start from 7 (zero indexed here)
				returnline[ClassesFields.NUM_INFO_COLS_NOID-1] = returnline[ClassesFields.NUM_INFO_COLS_NOID-1].concat(": " + splitline[i]);
			}
		}
		
		// trim whitespaces
		for(int i = 0; i < returnline.length; i++) {
			returnline[i] = returnline[i].trim();
		}
		
		return returnline;
	}
	
}
