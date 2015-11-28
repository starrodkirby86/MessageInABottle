package com.yarmatey.messageinabottle.message;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.SettingsActivity;
import com.yarmatey.messageinabottle.bottles.AvailableBottle;

import java.util.ArrayList;


public class MessageActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {



    /** MEMBER VARIABLES **/
    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation;
    private CameraUpdate cameraUpdate;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    /** CLASS CONSTANTS **/
    private String TAG = this.getClass().getSimpleName();
    //private final int PERMISSION_LOCATION = 1;


    @Override
    public void onStart() {
        super.onStart();
        // Connect texi client.
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Launch the Settings Activity
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.drop_bottle) {
            //grab EditText that contains user message
            EditText textView = (EditText) findViewById(R.id.message_edit);
            if (textView.getText().toString().trim().length() > 0) { //if contains characters, and not just whitespace
                ParseGeoPoint point = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()); //currentLocation.getLatitude(), currentLocation.getLongitude());
                AvailableBottle newBottle = new AvailableBottle();
                ArrayList<Integer> ratings = new ArrayList<>();
                for(int i = 0; i < 4; i++)
                    ratings.add(0);
                newBottle.setAll(point, textView.getText().toString(), 0, ParseUser.getCurrentUser(), ParseUser.getCurrentUser(), new ArrayList<String>(), ratings);
                newBottle.saveInBackground();
            } else //No message inserted
                Toast.makeText(this, "Enter a Message!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    /** MapView OVERRIDES **/
    //For MapView to resume when parent view resumes
    @Override
    public void onResume() {
        //mapView.onResume();
        super.onResume();
    }
    //For MapView to destroy when parent view is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }
    //For MapView to follow parent view on LowMemory
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }

    /** LocationListener OVERRIDES **/
    //@Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log
            currentLocation = location;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected Status: Connected");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1500);

        //TODO REIMPLMEMENT WHEN LOCATIONLISTENER IS UPDATED
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Status: Disconnected");
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connected Status: Failed");
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }

    }

}
