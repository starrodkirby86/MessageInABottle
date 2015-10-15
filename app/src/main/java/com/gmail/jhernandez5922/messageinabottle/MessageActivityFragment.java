package com.gmail.jhernandez5922.messageinabottle;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment
    implements LocationListener {

    /** MEMBER VARIABLES **/
    private NavigableMap<Location, String> savedMessages;
    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation;
    private CameraUpdate cameraUpdate;
    private TextView message;

    /** CLASS CONSTANTS **/
    //Constant for range of pickup
    public static final double WITHIN_RANGE = 3.25;

    //Constant for checking minimum accuracy to check for bottles
    public static final double MIN_ACCURACY = 15;

    //private final int PERMISSION_LOCATION = 1;

    /** CONSTRUCTOR **/
    public MessageActivityFragment() {
        savedMessages = new TreeMap<Location, String>(new LocationComparator()) {};
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

        //Grab TextView to update Coordinates --DEBUG ONLY
        final TextView locationText = (TextView) v.findViewById(R.id.message_title);
        //Get LocationManager to update location
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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

        //Update location when changes
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); //currently an error for API 23, runs okay for now. Will fix.


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
                    //if location's message does not exist...
                    //then save the message
                    if (!savedMessages.containsValue(textView.getText().toString())) {
                        savedMessages.put(currentLocation, textView.getText().toString());

                        //PARSE.COM:: SAVE DATA TO SERVER
                        Map<String, String> dimensions = new HashMap<String, String>();
                        // What type of news is this?
                        dimensions.put("category", "politics");
                        // Is it a weekday or the weekend?
                        dimensions.put("dayType", "weekday");
                        // Send the dimensions to Parse along with the 'read' event
                        ParseAnalytics.trackEventInBackground("read", dimensions);
                        //END PARSE.COM
                    }
                    //else if Location occupied by another message, remove than add message
                    //NOTE: TODO: REMOVE
                    else if (savedMessages.containsValue(textView.getText().toString())) {
                        savedMessages.values().removeAll(Collections.singleton(textView.getText().toString()));
                        savedMessages.put(currentLocation, textView.getText().toString());
                    }
                }
                else //No message inserted
                    Toast.makeText(getContext(), "Enter a Message!", Toast.LENGTH_SHORT).show();
            }
        });
        return v; //return inflated view
    }

    /** MapView OVERRIDES **/
    //For MapView to resume when parent view resumes - Jason
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
            Log.d("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log

            //Move map to new location [IS THIS NECESSARY], might be taxing on battery
            cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            map.animateCamera(cameraUpdate);

            //update location
            currentLocation = location;

            //Grab textView to print message --DEBUG ONLY TODO ADD VIEW MESSAGE FRAGMENT
            if (message == null)
                message = (TextView) getActivity().findViewById(R.id.message_title);

            //Check if location is accurate enough and there are messages to find
            if (location.getAccuracy() < MIN_ACCURACY && location.getAccuracy() != 0 && !savedMessages.isEmpty()) {
                //get next highest value
                Location greaterLoc = savedMessages.ceilingKey(location);
                //get next lowest value
                Location lesserLoc = savedMessages.floorKey(location);
                //check if value entry
                double ceilDist = greaterLoc != null ? location.distanceTo(greaterLoc) : Double.MAX_VALUE;
                double floorDist = lesserLoc != null ? location.distanceTo(lesserLoc) : Double.MAX_VALUE;
                //if both are not valid exit --IS THIS NECESSARY, MIGHT BE REDUNDANT
                if (ceilDist == floorDist && ceilDist == Double.MAX_VALUE)
                    return;
                //Pick the closer value
                double lesser = ceilDist < floorDist ? ceilDist : floorDist;
                //If within 3.25 meters
                if (lesser < WITHIN_RANGE) {
                    //change text to message --DEBUG ONLY TODO Implement View Message Fragment and write to it here
                    message.setText(savedMessages.get(lesser == floorDist ? lesserLoc : greaterLoc));
                }
                else {
                    //Revert message
                    message.setText("Create A Message");
                }
            }
            else
                //Revert message [Possibly redundant]
                message.setText("Create A Message");


        }

    }
    //For LocationListener, doesn't need to be implemented further
    //MAYBES
    @Override
    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}


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
