package com.github.marco9999.uwatimetable;

import android.provider.BaseColumns;

final class ClassesFields implements BaseColumns {
	final static String TABLE_NAME = "classes"; // text
	final static String COLUMN_NULLABLE = "null"; // text
	
	final static String COLUMN_DAY = "day"; // text
	final static String COLUMN_TIME = "time"; // int
	final static String COLUMN_UNIT = "unit"; // text
	final static String COLUMN_TYPE = "type"; // text
	final static String COLUMN_STREAM = "stream"; // int
	final static String COLUMN_WEEKS = "weeks"; // text
	final static String COLUMN_VENUE = "venue"; // text
	
	final static int NUM_INFO_COLS = 8; // 1 for id + 7 info cols
    final static int NUM_INFO_COLS_NOID = 7; // 7 info cols
	
	// A ENTRY IN CLASSES.TXT WILL BE AS FOLLOWS:
	// DAY:TIME:UNIT:TYPE:STREAM:WEEKS:VENUE(:[PREF i], not currently used)
	
	// Used to map returned arraylist or string array with classes data for the day -> views for displaying
	final static Integer[] FIELD_VIEW_MAP_OVERVIEW = {null, null, R.id.time, R.id.unit, R.id.type, R.id.stream, null, R.id.venue};
    final static Integer[] FIELD_VIEW_MAP_DELENT = {R.id.delete_id, R.id.delete_day, R.id.time, R.id.unit, R.id.type, R.id.stream, R.id.weeks, R.id.venue};
    final static Integer[] FIELD_VIEW_MAP_ADDCLASS = {R.id.day, R.id.time, R.id.unit, R.id.type, R.id.stream, R.id.weeks, R.id.venue};
	final static int FIELD_INDEX_WEEKS = 6; // used to map week to textview in WClassesBaseAdapter
	final static int FIELD_INDEX_ID = 0;

    // Within the types of classes, we have: Lectures, Labs, Tutorials, Practicals, Seminars (According to timetable.uwa.edu.au)
    // Add another arbitrary type, called 'Other' which contains any classes which do not fit into the categories above.
    // In total, there are 6 types of classes
    final static int NUM_TYPE_CLASSES = 6;

}
