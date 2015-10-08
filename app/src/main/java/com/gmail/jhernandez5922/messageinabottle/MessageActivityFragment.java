package com.gmail.jhernandez5922.messageinabottle;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment {


    final int PERMISSION_LOCATION = 1;
    MapView mapView;
    GoogleMap map;
    LocationManager locationManager;
    CameraUpdate cameraUpdate;
    public MessageActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);


        //Find mapView in layout and create the view
        //see onCreate below
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        //Allow GoogleMap to grab the MapView and initialize
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        //Initialize the map after GoogleMap is set up
        MapsInitializer.initialize(this.getActivity());

        //Updates location and zoom.
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(36.815512, -119.750583), 15);
        map.animateCamera(cameraUpdate);

        //Grab TextView to update Coordinates --DEBUG ONLY
        final TextView locationText = (TextView) v.findViewById(R.id.currentLocation);
        //Get LocationManager to update location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Get LocationListener to change location on change (see class...)
        LocationListener locationListener = new currentLocationListener(locationText);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); //currently an error for API 23, runs okay for now. Will fix.
        return v;
    }

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

    //When Permission is requested, this will determine what to do on the result
    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

            }
        }
    }

    //TODO INTEGRATE IN
    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocation(TextView locationText) {
        LocationListener locationListener = new currentLocationListener(locationText);
        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (getContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), PERMISSION_LOCATION);
//            // TODO: Consider calling
//            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            //return TODO;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //Location Listener to update location upon change
    private class currentLocationListener implements LocationListener {
        /*
            This class implements a location listener to grab the android device's
            current location.

            This class overrides four functions inherited from LocationListener
            --onLocationChange(Location location)
            --onProviderDisabled(String provider)
            --onProviderEnabled(String provider)
            --onStatusChanged(String provider, int status, Bundle extras)
         */
        TextView currentLocation;
        currentLocationListener(TextView l) {
            currentLocation = l;
        }
        @Override
        public void onLocationChanged(Location location) {
            if(location != null) {
                Log.d("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude());
                currentLocation.setText(location.getLatitude() + ", " + location.getLongitude());
                cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                map.animateCamera(cameraUpdate);

            }

        }
        //For LocationListener, doesn't need to be implemented further
        @Override
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
