package com.github.marco9999.htmlparserolcr;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.Collections;

/**  htmlparserolcr/EOlcrHtmlParser: Engine class responsible for parsing given html into readable strings for the database functions.
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

public class EOlcrHtmlParser {
	
	// Uses the Jsoup library.
	
	private String dirtyhtml;

	public ArrayList<String> getClassList(String contents) {
        dirtyhtml = contents;

        // parse html and put into ArrayList
		return makeListFromTable(parseHtmlString());
	}
	
	private String[][] parseHtmlString() {
		// Note: \u00A0 refers to &nbsp in the source html. It is still seen as a character and NOT as a
		//       white space. That is why trim() doesn't work and it has to be manually replaced with
		//       no character.
		//
		// Clean the html string using Jsoup
		String html = Jsoup.clean(dirtyhtml, Whitelist.relaxed());
		
		// TODO: Too hard (read: lazy) to workout dimension dynamically so just allocate more than enough rows
		// In theory max rows should be 1 (header) + 24 (hours) = 25 rows, but not urgent.
		String[][] finaltable = new String[64][]; 		
		
		// Prepare the document to be used
		Document doc = Jsoup.parse(html);
		for (Element table : doc.select("table")) {
			
			// parse the header (days)
			String[] headerrow = null;
            for (Element thead : table.select("thead")) {
                for (Element tr : thead.select("tr")) {
                	headerrow = new String[tr.select("th").size()];
                	int i = 0;
                	for (Element th : tr.select("th")) {
                		headerrow[i] = th.text().trim().replace("\u00A0", "");
                		i++;
                	}
	                	
	            }
            }
            // always going to be 0th element in array (header)
            finaltable[0] = headerrow; 
            
            // body (time and classes)
			String[] bodyrow;
            for (Element tbody : table.select("tbody")) {
            	int i = 1;
                for (Element tr : tbody.select("tr")) {
                	bodyrow = new String[tr.select("td").size()];
                	int j = 0;
                	for (Element td : tr.select("td")) {
                		bodyrow[j] = td.text().trim().replace("\u00A0", "");
                		j++;
                	}   
                	finaltable[i] = bodyrow;
                    i++;
	            }
            }
            
        }
		
		return finaltable;
	}

	private ArrayList<String> makeListFromTable(String[][] finaltable) {
		// Turn double array into ArrayList for easier processing
		// Also append the day and time to the string for linear processing later
		ArrayList<String> finallist = new ArrayList<String>((finaltable.length * finaltable[0].length)*2);
		String[] tmparray;
		for(int i = 1; i<finaltable.length; i++) {
			if(finaltable[i] != null) {
				for(int j = 1; j<finaltable[i].length; j++) {
					if(!finaltable[i][j].isEmpty()) {
						// clean string before adding
						tmparray = makeCleanClassStrings(finaltable[0][j], finaltable[i][0], finaltable[i][j]);
                        Collections.addAll(finallist, tmparray);
					}
				}
			}
		}
		
		return finallist;
	}
	
	private String[] makeCleanClassStrings(String day, String time, String dirtycell1) {
        // A cell may contain 1 or more classes, therefore we need to clean up the text and split the cell into the seperate classes.
        // Most of the time, there will only be one or two classes per cell.

		// get only the hour
		String newtime = time.split(":")[0];
		
		// remove any rogue characters
		String dirtycell = dirtycell1.replace("* ", "");
		
		// Split classes
		final String classregex = "(?<=\\[Pref\\s\\d{1,2}\\])(?!\\s\\(cont\\))|(?<=\\s\\(cont\\))";
		String[] seperateclasses = dirtycell.trim().split(classregex);
		Log.i(LogTag.htmlparserolcr, "------");
		Log.i(LogTag.htmlparserolcr, "RAW: " + day + "/" + time + ": " + dirtycell);
		for(String s : seperateclasses) {
			Log.i(LogTag.htmlparserolcr, "LISTING: " + s);
		}
		Log.i(LogTag.htmlparserolcr, "------");
		
		// Trim invidual fields and add date and time
		String[] singleclassfields;
		StringBuilder stringb;
		
		for(int i = 0; i<seperateclasses.length; i++) {
			// Build a string, start with day and time
			stringb = new StringBuilder();
			stringb.append(day).append(":").append(newtime).append(":");
			
			// Split using ":" character
			singleclassfields = seperateclasses[i].split(":");
            for (String singleclassfield : singleclassfields) {
                stringb.append(singleclassfield.trim()).append(":");
            }
			
			// Delete last ":" character
			stringb.deleteCharAt(stringb.length()-1);

			// Put back into array
			seperateclasses[i] = stringb.toString();
		}
		
		return seperateclasses;
	}
	

}
