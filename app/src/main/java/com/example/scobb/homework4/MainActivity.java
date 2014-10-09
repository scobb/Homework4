package com.example.scobb.homework4;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends FragmentActivity {
    String key = "AIzaSyCYusI1U7tW7Na7EHz0yPskpUDToVyV844";
    GoogleMap myMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        /** gets desired location name from user, creates JSON request to send to google maps **/
        Log.d("Homework4", "Bob says hi");
        MapFragment myMapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        myMap = myMapFragment.getMap();
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

        JSONObject respJSON = null;
//        try {
//            respJSON = new BackgroundHttpRequest().execute(queryURL).get();
//        }
//        catch (Exception exc) {
//            Log.e("Homework4", "Get Async Result Stacktrace:", exc);
//        }
//        try {
//            if (!respJSON.get("status").equals("OK")) {
//                // TODO: handle this case
//
//            }
//            Log.d("Homework4", "resp status is " + respJSON.get("status"));
//            else {
//                JSONObject location = respJSON.getJSONObject("location");
//                Double lat = (Double)location.get("lat");
//                Double lon = (Double)location.get("lon");
//                Toast.makeText(this, "Query string:\n" + queryString + "\nlat: " + lat + "\nlon: " + lon, Toast.LENGTH_LONG).show();
//
//
//            }
//        }
//        catch (Exception exc) {
//            Log.e("Homework4", "JSON Excpetion: ", exc);
//        }
        // get map
        Log.d("Homework4", "Steve's cool debug msg");
        SupportMapFragment mapFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        GoogleMap myMap = mapFrag.getMap();
//        if (myMap == null) {
//            // TODO: handle this case
//        }
//
//        // add pin to map


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
