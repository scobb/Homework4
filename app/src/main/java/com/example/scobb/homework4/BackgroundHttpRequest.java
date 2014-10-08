package com.example.scobb.homework4;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by scobb on 10/8/14.
 */
public class BackgroundHttpRequest extends AsyncTask<URL, Void, JSONObject> {
    protected JSONObject doInBackground(URL... urls) {
        JSONObject result = null;
        int count = urls.length;
        if (count < 1) {
            // didn't get a URL - return null pointer
            return result;
        }
        URL queryURL = urls[0];

        BufferedReader br = null;
        HttpURLConnection conn;
        String line;
        String resultString = "";
        JSONObject respJSON;

        try {
            // send http request
            conn = (HttpURLConnection) queryURL.openConnection();
            conn.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                // read and concatenate lines of response
                resultString += line;
            }
            // build JSON from response
            result = new JSONObject(resultString);
        }
        catch (Exception exc){
            Log.e("Homework4", "HTTP connection Stack trace:", exc);
        }
        finally {
            if (br != null) {
                try {
                    // clean up buffered reader
                    br.close();
                }
                catch (Exception exc) {
                    Log.e("Homework4", "Closing BR:", exc);
                }

            }
        }
        return result;

    }

}
