package com.github.marco9999.uwatimetable;

import com.github.marco9999.fileparserolcr.EClassesFileAsyncTask;

/**
 * Created by Marco on 27/06/2015.
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
