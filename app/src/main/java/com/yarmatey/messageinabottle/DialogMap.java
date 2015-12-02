package com.yarmatey.messageinabottle;

import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.yarmatey.messageinabottle.bottles.AvailableBottle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 11/28/2015.
 */
public class DialogMap extends DialogFragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation;
    private CameraUpdate cameraUpdate;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String message;
    private boolean mapsVal;


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //FIRST: We check to see if we even can cast this bottle!
        LocationManager lm = (LocationManager)this.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        final View view;

        if(!gps_enabled) {
            view = inflater.inflate(R.layout.dialog_cant_drop_bottle, container);

            //Set it to be UNABLE to drop!
            Button cast = (Button) view.findViewById(R.id.cast_bottle);
            cast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        else {
            view = inflater.inflate(R.layout.dialog_drop_bottle, container);

            //Set it to be able to drop!
            Button cast = (Button) view.findViewById(R.id.cast_bottle);
            cast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentLocation!=null) {
                        ParseGeoPoint point = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()); //currentLocation.getLatitude(), currentLocation.getLongitude());
                        AvailableBottle newBottle = new AvailableBottle();
                        ArrayList<Integer> ratings = new ArrayList<>();
                        for (int i = 0; i < 4; i++)
                            ratings.add(0);
                        newBottle.setAll(point, message, 0, ParseUser.getCurrentUser(), ParseUser.getCurrentUser(), new ArrayList<String>(), ratings);
                        newBottle.saveInBackground();
                        getDialog().dismiss();
                    }
                    else
                    {
                        Snackbar.make(view, "You're lost at sea matey, find yer location first!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

            //Retrieve the preferences from this fragment's context.
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            //Pull the map_switch and return false if this value does not exist (the default value)
            mapsVal = preferences.getBoolean("map_switch", false);


            Bundle args = getArguments();
            message = args.getString("message");
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            Button dismiss = (Button) view.findViewById(R.id.dismiss);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            showMapDialog(view, savedInstanceState);
            return view;
    }



    private String TAG = this.getClass().getSimpleName();

    /** LocationListener OVERRIDES **/
    //@Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log
            currentLocation = location;
            if(!mapsVal)
                return;

            if (map.getCameraPosition().zoom > 15)
                cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            else {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
            }
            map.animateCamera(cameraUpdate);
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
                connectionResult.startResolutionForResult(getActivity(), 0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 0).show();
        }

    }

    public void showMapDialog(View v, Bundle savedInstanceState) {

        //Retrieve the preferences from this fragment's context.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        //Pull the map_switch and return false if this value does not exist (the default value)
        mapsVal = preferences.getBoolean("map_switch", false);

        if(!mapsVal){
            //Snackbar.make(v.getRootView(), "Maps Disabled!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //Find mapView in layout and create the view
        //see onCreate below
        mapView = (MapView) v.findViewById(R.id.map_view_dialog);
        mapView.onCreate(savedInstanceState);

        //Allow GoogleMap to grab the MapView and initialize
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);


        //Get LocationListener to change location on change (see class...)

        //Used to add permission for API 23 --TODO ADD INTEGRATION FOR API 23
        List<String> permissions = new ArrayList<>();
        //Add access too fine Location
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        //TODO UNCOMMENT THIS TO INTEGRATE FOR API 23
//        if (getContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(permissions.toArray(new String[permissions.size()]), PERMISSION_LOCATION);
        // TODO: make checkSelfPermission for API 23 available for use if API 23, so permission can be accessed at run time
//        }
        //Create a GoogleApiClient instance

        //Update location when changes
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0 , this); //currently an error for API 23, runs okay for now. Will fix.


        //Initialize the map after GoogleMap is set up
        MapsInitializer.initialize(this.getActivity());


//        Updates location and zoom.
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(36.815512, -119.750583), 15);
        map.animateCamera(cameraUpdate);
    }






    /** MapView OVERRIDES **/
    //For MapView to resume when parent view resumes
    @Override
    public void onResume() {
        if(mapsVal)
        {mapView.onResume();}
        super.onResume();
    }
    //For MapView to destroy when parent view is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapsVal)
        {mapView.onDestroy();}
    }
    //For MapView to follow parent view on LowMemory
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapsVal)
        {mapView.onLowMemory();}
    }
}
