package com.github.marco9999.uwatimetable;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClassesFileHelper {
	
	private final static String filename = "classes.txt";
	private final static String ERRTAG = "uwatimetable";
	
	File classesdbfile = null;
	BufferedReader reader = null;
	String line = null;

	boolean openClassesDBFile() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			classesdbfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + filename);
			if(classesdbfile.exists()) {
				Log.i(ERRTAG, classesdbfile.getPath());
				return true;
			}
		} else {
		    // Oops... what went wrong here?
		    Log.e(ERRTAG, "Error: Check read from external storage permission!");
		    return false;
		}
		Log.i(ERRTAG, "File doesn't exist.");
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
