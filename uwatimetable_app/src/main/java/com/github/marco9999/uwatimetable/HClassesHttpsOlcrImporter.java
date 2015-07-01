package com.github.marco9999.uwatimetable;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.github.marco9999.htmlparserolcr.EOlcrHtmlParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class HClassesHttpsOlcrImporter extends AsyncTask<String, String, Void> {

	private final HClassesDbUI db;
    private final DHttpsOLCRStatus callback;

	HClassesHttpsOlcrImporter(HClassesDbUI _db, DHttpsOLCRStatus _callback) {
		db = _db;
        callback = _callback;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Void doInBackground(String... params) {
        // String array: [0] = username, [1] = password
        // uses server1
        String baseserver = "server1.olcr.uwa.edu.au";
        String urlstring = "https://" + baseserver +"/olcrstudent/index.jsp";
        String postdata = "v_studentid=" + params[0] +"&v_studentpin=" + params[1] + "&b3=Login";

        try {
            // setup initial connection for login, get cookie id
            URL olcrindex = new URL(urlstring);
            HttpsURLConnection initconnection = (HttpsURLConnection) olcrindex.openConnection();
            initconnection.setUseCaches(false);
            initconnection.setDoOutput(true);
            initconnection.setInstanceFollowRedirects(false);
            initconnection.setRequestMethod("POST");
            initconnection.setRequestProperty("User-Agent", "curl/7.38.0");
            initconnection.setRequestProperty("Host", baseserver);
            initconnection.setRequestProperty("Accept", "*/*");
            initconnection.setRequestProperty("Content-Length", Integer.toString(postdata.length()));
            initconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // write post data (user and pass)
            OutputStream out = initconnection.getOutputStream();
            out.write(postdata.getBytes("UTF-8"));
            out.flush();
            out.close();

            // send data and get response code
            int code = initconnection.getResponseCode();
            publishProgress("Initial Connection Code: " + Integer.toString(code));

            // get response headers
            Map<String, List<String>> initheader = initconnection.getHeaderFields();

            // get cookie id
            List<String> getcookie = initheader.get("Set-Cookie");
            String cookiestr = getcookie.get(0);
            String[] cookiearray = cookiestr.split(";");
            String jsessionid = cookiearray[0];
            publishProgress("Got Session ID!");

            // above should return 302 temp redirect -> get location header
            List<String> getlocation = initheader.get("Location");
            String templocation = getlocation.get(0);

            // sleep for a bit, too fast for the remote server? (not sure but seems to fix)
            publishProgress("Wait for a bit...");
            Thread.sleep(100);

            // get new url
            publishProgress("Get units page...");
            URL units = new URL(templocation);
            HttpsURLConnection unitsconnection = (HttpsURLConnection) units.openConnection();
            unitsconnection.setInstanceFollowRedirects(false);
            unitsconnection.setRequestMethod("GET");
            unitsconnection.setRequestProperty("User-Agent", "curl/7.38.0");
            unitsconnection.setRequestProperty("Host", baseserver);
            unitsconnection.setRequestProperty("Accept", "*/*");
            unitsconnection.setRequestProperty("Cookie", jsessionid);

            // send
            int code1 = unitsconnection.getResponseCode();
            publishProgress("Units Connection Code: " + Integer.toString(code1));

            // sleep for a bit, too fast for the remote server? (not sure but seems to fix)
            publishProgress("Wait for a bit...");
            Thread.sleep(100);

            // get allocs url
            publishProgress("Get allocs page...");
            String allocsurl = "https://" + baseserver + "/olcrstudent/studenttimetable.jsp?v_deptid=null&v_unitid=null&v_studentid=" + params[0] + "&v_groupid=none&v_fontsize=10&v_action=2";
            URL allocs = new URL(allocsurl);
            HttpsURLConnection allocsconnection = (HttpsURLConnection) allocs.openConnection();
            allocsconnection.setInstanceFollowRedirects(false);
            allocsconnection.setRequestMethod("GET");
            allocsconnection.setRequestProperty("User-Agent", "curl/7.38.0");
            allocsconnection.setRequestProperty("Host", baseserver);
            allocsconnection.setRequestProperty("Accept", "*/*");
            allocsconnection.setRequestProperty("Cookie", jsessionid);

            // send
            int code2 = allocsconnection.getResponseCode();
            publishProgress("Allocs Connection Code: " + Integer.toString(code2));

            // write allocs page to string
            StringBuilder allocshtml = new StringBuilder();
            InputStream in = allocsconnection.getInputStream();
            byte[] tmp = new byte[8192]; // 8 KB buffer
            int len;
            while ((len = in.read(tmp)) != -1) {
                allocshtml.append(new String(tmp, 0, len));
            }
            in.close();

            publishProgress("Successfully got data! Processing now.");

            // Get list
            ArrayList<String> classlist = new EOlcrHtmlParser().getClassList(allocshtml.toString());

            // put into contentvalues array
            ArrayList<ContentValues> arraycv = new ArrayList<>();
            Log.i(LogTag.OLCR, "-------------------Start Classes Listing-------------------");
            for(String str : classlist) {
                if(str == null) {
                    Log.i(LogTag.OLCR, "(null)");
                } else {
                    Log.i(LogTag.OLCR, str);
                }
                arraycv.add(db.createClassesCV(db.readEntryFromLine(str)));
            }
            Log.i(LogTag.OLCR, "-------------------End Classes Listing-------------------");

            // write and display db
            if(db.writeClassToDB(arraycv.toArray(new ContentValues[arraycv.size()]))) {
                // display message notifying success.
                publishProgress("Got classes from OLCR successfully (" + arraycv.size() + " entries).");
            } else {
                // failed
                publishProgress("Failed to add classes. Please notify developer!");
            }
            return null;
        } catch (Exception ex) {
            publishProgress("\nSomething went wrong, check Username and Password. UWA may also be experiencing issues. Tell the developer this message:");
            publishProgress(ex.getMessage());
            return null;
        }
	}

	@Override
	protected void onPostExecute(Void result) {
        callback.b_ok.setClickable(true);
	}

	@Override
	protected void onProgressUpdate(String... result) {
        String oldtext = callback.console.getText().toString();
        String newtext = oldtext + System.getProperty("line.separator") + result[0];
        callback.console.setText(newtext);
	}
}