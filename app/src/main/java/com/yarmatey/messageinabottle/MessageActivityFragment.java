package com.yarmatey.messageinabottle;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /** MEMBER VARIABLES **/
    private NavigableMap<Location, String> savedMessages;
    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation;
    private CameraUpdate cameraUpdate;
    private TextView message;
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    /** CLASS CONSTANTS **/
    //Constant for range of pickup
    public static final double RANGE = .001;

    //Constant for checking minimum accuracy to check for bottles
    public static final double MIN_ACCURACY = 20;

    private String TAG = this.getClass().getSimpleName();
    //private final int PERMISSION_LOCATION = 1;

    /** CONSTRUCTOR **/
    public MessageActivityFragment() {
        savedMessages = new TreeMap<Location, String>(new LocationComparator()) {};
    }

    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected Status: Connected");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1500);

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


    /** FRAGMENT OVERRIDES **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        //Find mapView in layout and create the view
        //see onCreate below
        mapView = (MapView) v.findViewById(R.id.mapview);
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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //Update location when changes
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0 , this); //currently an error for API 23, runs okay for now. Will fix.


        //Initialize the map after GoogleMap is set up
        MapsInitializer.initialize(this.getActivity());


        //Updates location and zoom.
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(36.815512, -119.750583), 15);
        map.animateCamera(cameraUpdate);
        final Button dropBottle = (Button) v.findViewById(R.id.drop_button);
        dropBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab EditText that contains user message
                EditText textView = (EditText)getActivity().findViewById(R.id.message_edit);
                if (textView.getText().toString().trim().length() > 0) { //if contains characters, and not just whitespace

                    ParseObject bottle = new ParseObject("bottle");
                    ParseGeoPoint point = new ParseGeoPoint(currentLocation.getLatitude(),currentLocation.getLongitude()); //currentLocation.getLatitude(), currentLocation.getLongitude());
                    bottle.put("location", point);
                    bottle.put("message", textView.getText().toString());
                    bottle.put("type", 0);
                    //bottle.put("past", null);
                    //bottle.put("content", null);

                    bottle.saveInBackground();
//                    //if location does not contain message at location
//                    if (!savedMessages.containsValue(textView.getText().toString())) {
//                        savedMessages.put(currentLocation, textView.getText().toString());
//                    }
//                    //Location occupied, remove than add message
//                    else if (savedMessages.containsValue(textView.getText().toString())) {
//                        savedMessages.values().removeAll(Collections.singleton(textView.getText().toString()));
//                        savedMessages.put(currentLocation, textView.getText().toString());
//                    }
                }
                else //No message inserted
                    Toast.makeText(getContext(), "Enter a Message!", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    /** MapView OVERRIDES **/
    //For MapView to resume when parent view resumes
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    //For MapView to destroy when parent view is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    //For MapView to follow parent view on LowMemory
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /** LocationListener OVERRIDES **/
    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log

            //Move map to new location [IS THIS NECESSARY], might be taxing on battery
            cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            map.animateCamera(cameraUpdate);

            //update location
            currentLocation = location;

            //Create a point that Parse knows what the location is.
            ParseGeoPoint point = new ParseGeoPoint(currentLocation.getLatitude(),currentLocation.getLongitude());
            final ParseObject foundBottle = new ParseObject("bottle");
            foundBottle.put("message","Create a Message!");

            //Replicating the below code:
            //ParseGeoPoint userLocation = (ParseGeoPoint) foundBottle.get("location");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("bottle");
            query.whereNear("location", point);
            //Retrieve 1 Bottle. Do not proceed unto 2.
            query.setLimit(1);
            query.whereWithinKilometers("location", point, RANGE);
            //Now to run the query:
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> localBottle, ParseException e) {
                    if (e == null && !localBottle.isEmpty()) {
                        Log.d("location", "Retrieved Lat: " + localBottle.get(0).getParseGeoPoint("location").getLatitude() + ", Lon: " + localBottle.get(0).getParseGeoPoint("location").getLongitude());
                        foundBottle.put("location", localBottle.get(0).getParseGeoPoint("location"));
                        foundBottle.put("message", localBottle.get(0).getString("message"));
                        foundBottle.put("type", localBottle.get(0).getInt("type"));
                        //message.setText(foundBottle.getString("message"));
                        Toast.makeText(getContext(), foundBottle.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (e == null) {
                            Log.d("location", "No Bottles!");
                        } else {
                            Log.d("location", "Error: " + e.getMessage());
                        }
                    }
                }
            });
            //message.setText(foundBottle.getString("message"));
//
//            //Grab textView to print message --DEBUG ONLY TODO ADD VIEW MESSAGE FRAGMENT
//            if (message == null)ParseObject bottle = new ParseObject("bottle");
//                message = (TextView) getActivity().findViewById(R.id.message_title);
//
//            //Check if location is accurate enough and there are messages to find
//            if (location.getAccuracy() < MIN_ACCURACY && location.getAccuracy() != 0 && !savedMessages.isEmpty()) {
//                //get next highest value
//                Location greaterLoc = savedMessages.ceilingKey(location);
//                //get next lowest value
//                Location lesserLoc = savedMessages.floorKey(location);
//                //check if value entry
//                double ceilDist = greaterLoc != null ? location.distanceTo(greaterLoc) : Double.MAX_VALUE;
//                double floorDist = lesserLoc != null ? location.distanceTo(lesserLoc) : Double.MAX_VALUE;
//                //if both are not valid exit --IS THIS NECESSARY, MIGHT BE REDUNDANT
//                if (ceilDist == floorDist && ceilDist == Double.MAX_VALUE)
//                    return;
//                //Pick the closer value
//                double lesser = ceilDist < floorDist ? ceilDist : floorDist;
//                //If within 3.25 meters
//                if (lesser < RANGE) {
//                    //change text to message --DEBUG ONLY TODO Implement View Message Fragment and write to it here
//                    message.setText(savedMessages.get(lesser == floorDist ? lesserLoc : greaterLoc));
//                }
//                else {
//                    //Revert message
//                    message.setText("Create A Message");
//                }
//            }
//            else
//                //Revert message [Possibly redundant]
//                message.setText("Create A Message");
//
//
        }

    }


//TODO Implement Runtime Permissions
    //When Permission is requested, this will determine what to do on the result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_LOCATION:
////                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
////
////            }
//        }
//    }


}
