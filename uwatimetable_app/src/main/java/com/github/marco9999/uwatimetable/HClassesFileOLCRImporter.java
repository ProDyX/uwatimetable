package com.github.marco9999.uwatimetable;

import com.github.marco9999.fileparserolcr.EClassesFileAsyncTask;

/**  uwatimetable/HClassesFileOLCRImporter: Helper class aliasing the engine behind classes.txt file importing.
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

public class HClassesFileOLCRImporter {
    public final AMain mainactivity;
    public final HClassesDbUI dbhelperui;

    HClassesFileOLCRImporter(AMain _mainactivity, HClassesDbUI _dbhelperui) {
        mainactivity = _mainactivity;
        dbhelperui = _dbhelperui;
    }

    void read() {
        new EClassesFileAsyncTask(this).execute(new Void[] {null});
    }
}
