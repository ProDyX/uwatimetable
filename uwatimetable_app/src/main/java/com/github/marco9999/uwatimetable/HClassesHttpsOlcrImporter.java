package com.github.marco9999.uwatimetable;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.text.Html;
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
    private final FragmentManager fmanager;
    String consoletext = "";
    final Object lock = new Object();
    boolean isfinished = false;

	HClassesHttpsOlcrImporter(HClassesDbUI _db, FragmentManager _fmanager) {
		db = _db;
        fmanager = _fmanager;
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
            if (code == 302) {
                publishProgress("Initial connection code: <font color=\"#00FF00\">" + Integer.toString(code) + "</font>"); // expect 302 - moved
            } else {
                publishProgress("Initial connection code: <font color=\"#FF0000\">" + Integer.toString(code) + "</font>");
                throw new Exception("Connection code returned was: <font color=\"#FF0000\">" + Integer.toString(code) + "</font><br>This is probably caused by UWA.");
            }

            // get response headers
            Map<String, List<String>> initheader = initconnection.getHeaderFields();

            // get cookie id
            List<String> getcookie = initheader.get("Set-Cookie");
            String cookiestr = getcookie.get(0);
            String[] cookiearray = cookiestr.split(";");
            String jsessionid = cookiearray[0];
            publishProgress("Got session ID!");

            // above should return 302 temp redirect -> get location header
            List<String> getlocation = initheader.get("Location");
            String templocation = getlocation.get(0);

            // sleep for a bit, too fast for the remote server? (not sure but seems to fix)
            publishProgress("Wait for a bit...");
            Thread.sleep(100);

            // get new url
            publishProgress("Getting units page.");
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
            if (code1 == 200) {
                publishProgress("Units connection code: <font color=\"#00FF00\">" + Integer.toString(code1) + "</font>"); // expect 200 - ok
            } else {
                publishProgress("Units connection code: <font color=\"#FF0000\">" + Integer.toString(code1) + "</font>");
                throw new Exception("Connection code returned was: <font color=\"#FF0000\">" + Integer.toString(code1) + "</font><br>This is probably caused by UWA.");
            }

            // sleep for a bit, too fast for the remote server? (not sure but seems to fix)
            publishProgress("Wait for a bit...");
            Thread.sleep(100);

            // get allocs url
            publishProgress("Getting allocations page.");
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
            if (code2 == 200) {
                publishProgress("Allocations connection code: <font color=\"#00FF00\">" + Integer.toString(code2) + "</font>"); // expect 200 - ok
            } else {
                publishProgress("Allocations connection code: <font color=\"#FF0000\">" + Integer.toString(code2) + "</font>");
                throw new Exception("Connection code returned was: <font color=\"#FF0000\">" + Integer.toString(code2) + "</font><br>This is probably caused by UWA.");
            }

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
            isfinished = true;
            return null;
        } catch (Exception ex) {
            publishProgress("");
            publishProgress("Something went wrong, please check Student ID and Password. UWA may also be experiencing issues. Tell the developer this message:");
            publishProgress(ex.getMessage());
            isfinished = true;
            return null;
        }
	}

	@Override
	protected void onPostExecute(Void result) {
        try {
            DHttpsOLCRStatus status = ((HDataFragment)fmanager.findFragmentByTag(Tag.H_FRAGMENT_DATA)).olcr_https_status;
            status.b_ok.setClickable(true);
            status.console.setText(Html.fromHtml(consoletext));
        } catch (NullPointerException ex) {
            Log.d(LogTag.OLCR_HTTPS, "Tried to access status fragment members but was null!");
        }
	}

	@Override
	protected void onProgressUpdate(String... result) {
        // due to race condition with fringe case involving configuration change, need to check to make sure nothing is null first of all. (timing/order is undefined according to docs for onProgUpd)
        // in case there is a null reference, we will not print the message, but store it locally and try again next time there is a progress update call.
        // finally, an update will be invoked from the original dialog, in case all else fails.
        consoletext = consoletext + result[0] + "<br>";
        Log.d(LogTag.OLCR_HTTPS, "consoletext: " + consoletext);

        try {
            DHttpsOLCRStatus status = ((HDataFragment)fmanager.findFragmentByTag(Tag.H_FRAGMENT_DATA)).olcr_https_status;
            status.console.setText(Html.fromHtml(consoletext));
        } catch (NullPointerException ex) {
            Log.d(LogTag.OLCR_HTTPS, "Tried to print but console var was null! Try again later.");
        }
	}
}