package com.github.marco9999.uwatimetable;

import android.provider.BaseColumns;

/**  uwatimetable/ClassesFields: Static values used in the program are stored here.
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

final class ClassesFields implements BaseColumns {

    // DB column names
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
	
	// AN ENTRY IN CLASSES.TXT WILL BE AS FOLLOWS:
	// DAY:TIME:UNIT:TYPE:STREAM:WEEKS:VENUE(:[PREF i], not currently used/activated)
	
	// Used to map returned arraylist or string array with classes data for the day -> views for displaying
    // (ID:)DAY:TIME:UNIT:TYPE:STREAM:WEEKS:VENUE(:[PREF i], not currently used)
	final static Integer[] FIELD_VIEW_MAP_OVERVIEW = {R.id.id_number, null, R.id.time, R.id.unit, R.id.type, R.id.stream, R.id.weeks, R.id.venue};
    final static Integer[] FIELD_VIEW_MAP_DELENT = {R.id.delete_id, R.id.delete_day, R.id.time, R.id.unit, R.id.type, R.id.stream, R.id.weeks, R.id.venue};
    final static Integer[] FIELD_VIEW_MAP_UPCOMING = {null, null, R.id.time, R.id.unit, R.id.type, R.id.stream, R.id.weeks, R.id.venue};
    final static Integer[] FIELD_VIEW_MAP_ADDCLASS = {R.id.day, R.id.time, R.id.unit, R.id.type, R.id.stream, R.id.weeks, R.id.venue}; // id field omitted as it is not used when adding a class manually
    final static int FIELD_INDEX_DAYS = 1; // used to map day in upcoming fragment
	final static int FIELD_INDEX_ID = 0;

    // Within the types of classes, we have: Lectures, Labs, Tutorials, Practicals, Seminars (According to timetable.uwa.edu.au) + Workshops
    // Add another arbitrary type, called 'Other' which contains any classes which do not fit into the categories above.
    // In total, there are 7 types of classes.
    // Included are the long hand names and some common abbreviations. The key with _1 in it will be used as the primary key when returning in a hashmap.
    final static int NUM_TYPE_CLASSES = 7;

    final static String TYPE_LECTURE_1 = "Lecture";
    final static String TYPE_LECTURE_2 = "Lec";
    final static String[] TYPE_LECTURES = {TYPE_LECTURE_1, TYPE_LECTURE_2};

    final static String TYPE_LABORATORY_1 = "Laboratory";
    final static String TYPE_LABORATORY_2 = "Lab";
    final static String[] TYPE_LABORATORYS = {TYPE_LABORATORY_1, TYPE_LABORATORY_2};

    final static String TYPE_TUTORIAL_1 = "Tutorial";
    final static String TYPE_TUTORIAL_2 = "Tut";
    final static String[] TYPE_TUTORIALS = {TYPE_TUTORIAL_1, TYPE_TUTORIAL_2};

    final static String TYPE_PRACTICAL_1 = "Practical";
    final static String TYPE_PRACTICAL_2 = "Prac";
    final static String[] TYPE_PRACTICALS = {TYPE_PRACTICAL_1, TYPE_PRACTICAL_2};

    final static String TYPE_SEMINAR_1 = "Seminar";
    final static String[] TYPE_SEMINARS = {TYPE_SEMINAR_1};

    final static String TYPE_WORKSHOP_1 = "Workshop";
    final static String[] TYPE_WORKSHOPS = {TYPE_WORKSHOP_1};

    final static String TYPE_OTHER_1 = "Other";
    final static String[] TYPE_OTHERS = {TYPE_OTHER_1};

    final static String[][] TYPE_CLASSES_ARRAY = {TYPE_LECTURES, TYPE_LABORATORYS, TYPE_TUTORIALS, TYPE_PRACTICALS, TYPE_SEMINARS, TYPE_WORKSHOPS, TYPE_OTHERS};

}
