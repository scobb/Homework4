package com.example.scobb.homework4;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    String key = "AIzaSyCYusI1U7tW7Na7EHz0yPskpUDToVyV844";
    String browserKey = "AIzaSyCR-xadd76lvcXLfPpN1vZxOARdQBr5CvE";
    GoogleMap myMap = null;
    LocationClient myLocationClient;
    Location myCurrentLocation;
    ArrayList<Marker> myMarkers = new ArrayList<Marker>();
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLocationClient = new LocationClient(this, this, this);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        myLocationClient.connect();
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        myLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */

            Toast.makeText(this, "Connection Error: " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();

            }
        }


    public void onClick(View view) {
        /** gets desired location name from user, creates JSON request to send to google maps **/
        myCurrentLocation = myLocationClient.getLastLocation();
        MapFragment myMapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        myMap = myMapFragment.getMap();

        EditText locEntry = (EditText)findViewById(R.id.locationInput);
        String location = locEntry.getText().toString();
        if ("".equals(location)){
            Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            return;
        }
        URI queryURI = null;
        try {
            queryURI = new URI("https","maps.googleapis.com",
                    "/maps/api/geocode/json","address=" + URLEncoder.encode(location, "UTF-8") +
                    "&key=" +  browserKey, null);
        } catch (Exception exc) {
            Log.e("Homework4", "URI encoding: ", exc);
        }
        JSONObject respJSON = null;
        try {
            respJSON = new BackgroundHttpRequest().execute(queryURI.toURL()).get();
        } catch (Exception exc) {
            Log.e("Homework4", "AsyncTask: ", exc);
        }
        Double lat = null;
        Double lon = null;
        String address = null;
        if (respJSON != null) {
            try {
                if (respJSON.get("status").equals("OK")) {
                    lat = respJSON.getJSONArray("results").getJSONObject(0).
                            getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    lon = respJSON.getJSONArray("results").getJSONObject(0).
                            getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    address = respJSON.getJSONArray("results").getJSONObject(0).
                            getString("formatted_address");
                } else {
                    Toast.makeText(this, "Please enter a valid location.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            catch (Exception exc) {
                Log.e("Homework4", "JSON fail: ", exc);
            }

        } else {
            Toast.makeText(this, "Please enter a valid location.", Toast.LENGTH_SHORT).show();
        }
        myMap.clear();
        myMarkers.clear();
        if (myCurrentLocation != null) {
            LatLng currentLatLng = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
            myMarkers.add(myMap.addMarker(new MarkerOptions()
                        .position(currentLatLng)
                        .title("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));

            float[] distResult = new float[1];
            Location.distanceBetween(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude(),
                    lat, lon, distResult);

            findViewById(R.id.distTitle).setVisibility(View.VISIBLE);
            TextView distValue = (TextView)findViewById(R.id.distValue);
            distValue.setText(String.format("%.2f", distResult[0]/1609.34f) + " mi");
            findViewById(R.id.distValue).setVisibility(View.VISIBLE);
        } else {
            Log.e("Homework4", "Current location is null.");
        }
        LatLng requestedLatLng = new LatLng(lat, lon);
        myMarkers.add(myMap.addMarker(new MarkerOptions()
                      .position(requestedLatLng)
                      .title(address)));

        //Calculate the markers to get their position
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (Marker m : myMarkers) {
            b.include(m.getPosition());
        }
        LatLngBounds bounds = b.build();
        myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 75));


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
