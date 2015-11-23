package com.yarmatey.messageinabottle;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment {

    /**
     * MEMBER VARIABLES
     **/
    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation;
    private CameraUpdate cameraUpdate;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    /**
     * CLASS CONSTANTS
     **/
    //Constant for range of pickup
    public static final double RANGE = .001;

    //Constant for checking minimum accuracy to check for bottles
    public static final double MIN_ACCURACY = 20;

    private String TAG = this.getClass().getSimpleName();
    //private final int PERMISSION_LOCATION = 1;


    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        //mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        //mGoogleApiClient.disconnect();
        super.onStop();
    }


//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.i(TAG, "Connected Status: Connected");
//
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1500);
//
//        //TODO REIMPLMEMENT WHEN LOCATIONLISTENER IS UPDATED
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(TAG, "Connection Status: Disconnected");
//    }
//
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.i(TAG, "Connected Status: Failed");
//        if (connectionResult.hasResolution()) {
//            try {
//                connectionResult.startResolutionForResult(getActivity(), 0);
//            } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
//        } else {
//            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 0).show();
//        }
//
//    }


    /**
     * FRAGMENT OVERRIDES
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_message, container, false);
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
        //final Button dropBottle = (Button) v.findViewById(R.id.button_fake);
//        dropBottle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //grab EditText that contains user message
//                EditText textView = (EditText) getActivity().findViewById(R.id.message_edit);
//                if (textView.getText().toString().trim().length() > 0) { //if contains characters, and not just whitespace
//                    ParseObject bottle = new ParseObject("bottle");
//                    ParseGeoPoint point = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()); //currentLocation.getLatitude(), currentLocation.getLongitude());
//                    bottle.put("location", point);
//                    bottle.put("message", textView.getText().toString());
//                    bottle.put("type", 0);
//                    bottle.saveInBackground();
//                } else //No message inserted
//                    Toast.makeText(getContext(), "Enter a Message!", Toast.LENGTH_SHORT).show();
//            }
//        });


        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drop_bottle:
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mapDialog(View v, Bundle savedInstanceState) {

        //Find mapView in layout and create the view
        //see onCreate below
        //mapView = (MapView) v.findViewById(R.id.mapview);
        //mapView.onCreate(savedInstanceState);

        //Allow GoogleMap to grab the MapView and initialize
        //map = mapView.getMap();
        //map.getUiSettings().setMyLocationButtonEnabled(false);
        //map.setMyLocationEnabled(true);


        //Get LocationListener to change location on change (see class...)

        //Used to add permission for API 23 --TODO ADD INTEGRATION FOR API 23
        //List<String> permissions = new ArrayList<>();
        //Add access too fine Location
        //permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        //TODO UNCOMMENT THIS TO INTEGRATE FOR API 23
//        if (getContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(permissions.toArray(new String[permissions.size()]), PERMISSION_LOCATION);
        // TODO: make checkSelfPermission for API 23 available for use if API 23, so permission can be accessed at run time
//        }
        //Create a GoogleApiClient instance

        //Update location when changes
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0 , this); //currently an error for API 23, runs okay for now. Will fix.


        //Initialize the map after GoogleMap is set up
        //MapsInitializer.initialize(this.getActivity());


        //Updates location and zoom.
        //cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(36.815512, -119.750583), 15);
        //map.animateCamera(cameraUpdate);
    }
}
