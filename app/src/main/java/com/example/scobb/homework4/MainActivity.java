package com.example.scobb.homework4;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    String key = "AIzaSyAen783dHbzKXmNo6qPRgudy3kdLlGRRKs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        /** gets desired location name from user, creates JSON request to send to google maps **/
        EditText loc = (EditText)findViewById(R.id.locationInput);
        String locText = loc.getText().toString().replace(" ", "+");
        URL queryURL = null;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("maps.googleapis.com").appendPath("maps").appendPath("api").appendPath("geocode").appendPath("json").appendQueryParameter("address",locText).appendQueryParameter("key", key);
        String queryString = builder.build().toString();
        try {
            queryURL = new URL(queryString);
        }
        catch (java.net.MalformedURLException exc) {
            // handle this case
            Log.e("Homework4", "URL Stack trace:", exc);
            return;
        }

        Toast.makeText(this, "Query string:\n" + queryString, Toast.LENGTH_LONG).show();

        try {
            JSONObject respJSON = new BackgroundHttpRequest().execute(queryURL).get();
        }
        catch (Exception exc) {
            Log.e("Homework4", "Get Async Result Stacktrace:", exc);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
