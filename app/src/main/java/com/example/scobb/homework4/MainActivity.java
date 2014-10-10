package com.example.scobb.homework4;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;


public class MainActivity extends FragmentActivity {
    String key = "AIzaSyCYusI1U7tW7Na7EHz0yPskpUDToVyV844";
    String browserKey = "AIzaSyCR-xadd76lvcXLfPpN1vZxOARdQBr5CvE";
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

        EditText locEntry = (EditText)findViewById(R.id.locationInput);
        String location = locEntry.getText().toString();
        URI queryURI = null;
        try {
            queryURI = new URI("https","maps.googleapis.com",
                    "/maps/api/geocode/json","address=" + URLEncoder.encode(location, "UTF-8") +
                    "&key=" +  browserKey, null);
        } catch (Exception exc) {
            Log.e("Homework4", "URI encoding: ", exc);
        }
        //Toast.makeText(this, queryURI.toString(), Toast.LENGTH_LONG).show();
        JSONObject respJSON = null;
        try {
            respJSON = new BackgroundHttpRequest().execute(queryURI.toURL()).get();
        } catch (Exception exc) {
            Log.e("Homework4", "AsyncTask: ", exc);
        }
        Double lat = null;
        Double lon = null;
        if (respJSON != null) {
            try {
                lat = (Double) respJSON.getJSONArray("results").getJSONObject(0).
                        getJSONObject("geometry").getJSONObject("location").get("lat");
                lon = (Double) respJSON.getJSONArray("results").getJSONObject(0).
                        getJSONObject("geometry").getJSONObject("location").get("lng");
            }
            catch (Exception exc) {
                Log.e("Homework4", "JSON fail: ", exc);

            }

        }
        LatLng myLocation = new LatLng(lat, lon);
        myMap.clear();
        myMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title(location));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, (float)5.0));



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
