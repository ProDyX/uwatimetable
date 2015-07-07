package com.github.marco9999.fileparserolcr;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**  fileparserolcr/HClassesFileReader: Helper class responsible for opening and handling file related functions.
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

class HClassesFileReader {
	
	private final static String filename = "classes.txt";

	private File classesdbfile = null;
	private BufferedReader reader = null;
	private String line = null;

	boolean openClassesDBFile() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			classesdbfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + filename);
			if(classesdbfile.exists()) {
				Log.i(LogTag.fileparserolcr, "Opened file: " + classesdbfile.getPath());
				return true;
			}
		} else {
		    // Oops... what went wrong here?
		    Log.e(LogTag.fileparserolcr, "Error: Check read from external storage permission!");
		    return false;
		}
		Log.i(LogTag.fileparserolcr, "File doesn't exist.");
		return false; // file doesn't exist, so don't bother trying to read it.
	}
	
	void openBufferedReaderFile() {
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(classesdbfile)));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	boolean moveToNextLine() {
		try {
			line = reader.readLine();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

        return !(line == null || line.isEmpty());
    }
	
	void closeFile() {
		try {
			reader.close();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	String getLine() {
		return line;
	}
}
