package com.github.marco9999.uwatimetable;

/**  uwatimetable/Tag: Tag class which contains fragment tags, used in FragmentManager.
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

public class Tag {
    static final String H_FRAGMENT_DATA = "H_Fragment_data";
    static final String FRAGMENT_MANUAL_ENTRY = "Manual_entry";
    static final String FRAGMENT_DELETE_ENTRY = "Delete_entry";
    static final String FRAGMENT_WEBVIEW_OLCR = "Webview_olcr";
    static final String FRAGMENT_OVERVIEW = "Overview"; // Linked to string array nav_drawer_items - do not change without propagating changes
    static final String FRAGMENT_SETTINGS = "Settings";
    static final String FRAGMENT_HELP = "Help";
    static final String FRAGMENT_UPCOMING = "Upcoming";// Linked to string array nav_drawer_items - do not change without propagating changes

    static final String DIALOG_WEEK = "Dialog_week";
    static final String DIALOG_DELSELID = "Dialog_delselid";
    static final String DIALOG_HTTPSSTATUS = "Dialog_httpsstatus";
}
