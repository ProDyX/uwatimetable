package com.github.marco9999.uwatimetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**  uwatimetable/HClassesDbSQL: Helper class for the database dealing with SQL operations.
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

class HClassesDbSQL extends SQLiteOpenHelper {

    private static final String DBNAME = "uwatimetable.db";
    private static final int DBVERSION = 2;

    public HClassesDbSQL(Context context) {
    	super(context, DBNAME, null, DBVERSION);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    // create the table
    static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
	    + ClassesFields.TABLE_NAME + " (" + ClassesFields._ID
	    + " INTEGER PRIMARY KEY" + COMMA_SEP
	    + ClassesFields.COLUMN_DAY + TEXT_TYPE + COMMA_SEP
	    + ClassesFields.COLUMN_TIME + INT_TYPE + COMMA_SEP
	    + ClassesFields.COLUMN_UNIT + TEXT_TYPE + COMMA_SEP
	    + ClassesFields.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP
	    + ClassesFields.COLUMN_STREAM + INT_TYPE + COMMA_SEP
	    + ClassesFields.COLUMN_WEEKS + TEXT_TYPE + COMMA_SEP
	    + ClassesFields.COLUMN_VENUE + TEXT_TYPE + ")";

    static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
	    + ClassesFields.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
		// Create the db
		db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// If someone downgrades, we just do the same as upgrading (delete db
		// and start over)
		onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Delete db and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
    }

}
