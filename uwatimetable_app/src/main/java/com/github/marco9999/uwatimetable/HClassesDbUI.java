package com.github.marco9999.uwatimetable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HClassesDbUI {

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
				// what the hell did we run into? return true to be safe.
				Log.i(LogTag.APP, "Unknown class weekview encounted, returning true: " + weektocheck);
				return true;
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

	public boolean writeClassToDB(ContentValues[] values) {
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
		String sqlselection = ClassesFields.COLUMN_DAY + "='" + day + "'";

		// Get the cursor results
		Cursor dbcursor = db.query(ClassesFields.TABLE_NAME, null, sqlselection, null, null, null, ClassesFields.COLUMN_TIME);

		if (!dbcursor.moveToFirst()) {
			Log.i(LogTag.APP, "Cursor from DB query is empty. Returning empty ArrayList.");
            dbcursor.close();
			return returnedlist;
		}

		// filter cursor out based on weeks, while copying to new arraylist
		do {
			if (isRelevantToWeek(dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_WEEKS)), week)) {
                tempstr = createClassStrArrayFromCursor(dbcursor);
				returnedlist.add(tempstr);
				Log.i(LogTag.APP, "Class ID returned: " + tempstr[0]);
			}
		} while (dbcursor.moveToNext());

        dbcursor.close();
		return returnedlist;
	}

    ArrayList<String[]> readAllEntries() {
        // similar to readRelevantEntries, except the search string is null (to return all)
        ArrayList<String[]> returnedlist = new ArrayList<String[]>();

        Cursor dbcursor = db.query(ClassesFields.TABLE_NAME, null, null, null, null, null, ClassesFields._ID);
        if (!dbcursor.moveToFirst()) {
            Log.i(LogTag.APP, "Cursor from DB query is empty. Returning empty ArrayList.");
            dbcursor.close();
            return returnedlist;
        }

        do {
            returnedlist.add(createClassStrArrayFromCursor(dbcursor));
        } while (dbcursor.moveToNext());

        dbcursor.close();
        return returnedlist;
    }

    HashMap<String, String[]> readAllUpcomingClass(int currentweek) {
        // Convenience method. Uses the array defined in ClassesFields to return all types.
        // Init hash map with capacity, assuming at least one of each class type
        HashMap<String, String[]> returnedmap = new HashMap<String, String[]>(ClassesFields.NUM_TYPE_CLASSES);

        for (String[] typearray : ClassesFields.TYPE_CLASSES_ARRAY) {
            // Put result into hashmap. Always set the hashmap key to the first entry in the array.
            returnedmap.put(typearray[0], readUpcomingClass(typearray, currentweek));
        }

        return returnedmap;
    }

    String[] readUpcomingClass(String[] typearray, int currentweek) {
        // TODO: revise function.
        final int ARB_HIGH_WEEK = 60; // arbitrary high weekview (see below)
        final int ARB_HIGH_DAY = 10;
        final int ARB_HIGH_TIME = 30;

        // sql selection builder sub routine
        StringBuilder sqlselbuild = new StringBuilder();
        for (String currenttype : typearray) {
            sqlselbuild.append(ClassesFields.COLUMN_TYPE);
            sqlselbuild.append(" LIKE '");
            sqlselbuild.append(currenttype);
            sqlselbuild.append("%' OR ");
        }
        sqlselbuild.delete(sqlselbuild.length() - 4, sqlselbuild.length());
        sqlselbuild.append(" COLLATE NOCASE");
        String sqlselection = sqlselbuild.toString();

        // Get the cursor results
        Cursor dbcursor = db.query(ClassesFields.TABLE_NAME, null, sqlselection, null, null, null, null);

        // check if empty
        if (!dbcursor.moveToFirst()) {
            // no matches were found, return null
            return null;
        }

        // if not then add earliest result after current weekview, day and time to hashmap.
        // need to iterate to find the earliest... probably very expensive performance wise.
        int earlistclassidx = -1;

        String weekstmp, daytmp;
        int timetmp;
        boolean alreadypast;

        int earlyweekold = ARB_HIGH_WEEK, earlyweeknew;
        int earlydayold = ARB_HIGH_DAY, earlydaynew;
        int earlytimeold = ARB_HIGH_TIME, earlytimenew;

        for (int i = 0; i < dbcursor.getCount(); i++) {
            // cache values needed to check class
            weekstmp = dbcursor.getString(dbcursor.getColumnIndex(ClassesFields.COLUMN_WEEKS));
            daytmp = dbcursor.getString(dbcursor.getColumnIndex((ClassesFields.COLUMN_DAY)));
            timetmp = dbcursor.getInt(dbcursor.getColumnIndex(ClassesFields.COLUMN_TIME));

            // start by checking two classes based on weeks (more efficient to do weeks -> day -> time? or day -> weeks -> time? etc)
            alreadypast = HStatic.hasClassAlreadyPast(daytmp, timetmp);
            earlyweeknew = checkAndGetEarliestWeek(weekstmp, currentweek, alreadypast);
            if((earlyweeknew != -1) && (earlyweeknew < earlyweekold)) {
                earlyweekold = earlyweeknew;
                earlydayold = HStatic.getIntFromStringDayAll(daytmp);
                earlytimeold = timetmp;
                earlistclassidx = i;
            } else if (earlyweeknew == earlyweekold) {
                // implement based on day
                earlydaynew = HStatic.getIntFromStringDayAll(daytmp);
                if(earlydaynew < earlydayold) {
                    earlyweekold = earlyweeknew;
                    earlydayold = earlydaynew;
                    earlytimeold = timetmp;
                    earlistclassidx = i;
                } else if (earlydaynew == earlydayold) {
                    // implement based on time
                    earlytimenew = timetmp;
                    if(earlytimenew < earlytimeold) {
                        earlyweekold = earlyweeknew;
                        earlydayold = earlydaynew;
                        earlytimeold = earlytimenew;
                        earlistclassidx = i;
                    } else if (earlytimenew == earlytimeold) {
                        // classes have a clash... report the newest one for now
                        // TODO: change function to return both.
                        earlistclassidx = i;
                    }
                }
            }
            Log.d(LogTag.APP, "[readupcomingclass loop] " + "class: " + weekstmp + " - " + daytmp + " - " + timetmp + ", alreadypast: " + alreadypast + ", earlyweeknew: " + earlyweeknew + ", earliestclassidx: " + earlistclassidx);
            Log.d(LogTag.APP, "-----");

            // no match, continue looping until one is found.
            dbcursor.moveToNext();
        }

        // check if no classes matched
        if (earlistclassidx == -1) return null;

        // else move back to earlist position index, and create string array from it
        dbcursor.moveToPosition(earlistclassidx);
        String[] classstr = createClassStrArrayFromCursor(dbcursor);
        Log.i(LogTag.APP, "Returned earliest class for " + typearray[0] + ": " + Arrays.deepToString(classstr));

        return classstr;
    }

    @SuppressLint("DefaultLocale") // we can assume it will be always english
    int checkAndGetEarliestWeek(String weektocheck, int currentweek, boolean ispassedforweek) {
        // Note: Returns -1 if not upcoming.
        // Constants
        // TODO: hardcoded values... need to see if we can pull from website (probably too much code to bother with)
        final int START_SEM1 = 9;
        final int END_SEM1 = 22;
        final int START_SEM2 = 31;
        final int END_SEM2 = 44;

        final int NOT_UPCOMING = -1;
        final int ARB_HIGH_WEEK = 60; // arbitrary high weekview (see below)

        int EXT_WEEK = 0;
        if (ispassedforweek) EXT_WEEK = 1;

        String weektochecklc = weektocheck.toLowerCase();
        if (weektochecklc.contains("sem")) {
            // which semester? if something contains 'sem' it can't contain any ranges or single weeks.
            String semester = weektochecklc.substring(3, 4);
            if (semester.equals("1")) {
                if (currentweek <= END_SEM1) {
                    //noinspection SimplifiableIfStatement
                    if (weektochecklc.contains("w2")) {
                        if (currentweek <= START_SEM1) return (START_SEM1 + 1);
                    }
                    return ((currentweek + EXT_WEEK) > END_SEM1 ? NOT_UPCOMING : (currentweek + EXT_WEEK));
                } else {
                    return NOT_UPCOMING;
                }
            } else if (semester.equals("2")) {
                if (currentweek <= END_SEM2) {
                    //noinspection SimplifiableIfStatement
                    if (weektochecklc.contains("w2")) {
                        if (currentweek <= START_SEM2) return (currentweek + EXT_WEEK);
                    }
                    return ((currentweek + EXT_WEEK) > END_SEM2 ? NOT_UPCOMING : (currentweek + EXT_WEEK));
                } else {
                    return NOT_UPCOMING;
                }
            } else {
                // what the hell did we run into? return current weekview to be safe.
                Log.i(LogTag.APP, "Unknown class weekview encounted, returning upcoming weekview (" + (currentweek + EXT_WEEK) + "): " + weektocheck);
                return (currentweek + EXT_WEEK);
            }
        } else {
            // Check for ranges or individual weeks: ie: 42-45,47-48 or 10,42 or 11 etc..
            String[] ranges = weektochecklc.split(",");
            int lowestlimit = ARB_HIGH_WEEK; // start at arbitrary high weekview to make sure every class will be lower than it initially.
            for (String indranges : ranges) {
                if (indranges.contains("-")) {
                    String[] indweeks = indranges.split("-");
                    int limit1 = Integer.parseInt(indweeks[0]);
                    int limit2 = Integer.parseInt(indweeks[1]);
                    if ((currentweek + EXT_WEEK) <= limit2) {
                        if (currentweek < limit1) {
                            if (limit1 < lowestlimit) lowestlimit = limit1;
                        } else {
                            if (limit2 < lowestlimit) lowestlimit = currentweek + EXT_WEEK;
                        }
                    }
                } else {
                    int limit = Integer.parseInt(indranges);
                    if ((currentweek + EXT_WEEK) <= limit) {
                        if (limit < lowestlimit) lowestlimit = limit;
                    }
                }
            }

            // end of checker
            if (lowestlimit != ARB_HIGH_WEEK) {
                return lowestlimit;
            } else {
                // no matches
                return NOT_UPCOMING;
            }
        }
    }

    public ContentValues createClassesCV(String[] svalues) {
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

	String[] createClassStrArrayFromCursor(Cursor dbcursor) {
        // convenience method to create a string array from a db cursor
        // must already have selected entry beforehand, otherwise will (most likely) throw an error
        String[] tempstr = new String[ClassesFields.NUM_INFO_COLS];
        tempstr[0] = Integer.toString(dbcursor.getInt(dbcursor.getColumnIndexOrThrow(ClassesFields._ID)));
        tempstr[1] = dbcursor.getString(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_DAY));
        tempstr[2] = Integer.toString(dbcursor.getInt(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_TIME))) + ":00";
        tempstr[3] = dbcursor.getString(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_UNIT));
        tempstr[4] = dbcursor.getString(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_TYPE));
        tempstr[5] = "(0" + Integer.toString(dbcursor.getInt(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_STREAM))) + ") - ";
        tempstr[6] = dbcursor.getString(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_WEEKS));
        tempstr[7] = dbcursor.getString(dbcursor.getColumnIndexOrThrow(ClassesFields.COLUMN_VENUE));
        return tempstr;
    }

    public String[] readEntryFromLine(String line1) {
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
