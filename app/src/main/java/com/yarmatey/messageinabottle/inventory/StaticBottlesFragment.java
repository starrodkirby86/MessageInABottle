package com.yarmatey.messageinabottle.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.bottles.AvailableBottle;
import com.yarmatey.messageinabottle.bottles.MapBottleAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaticBottlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaticBottlesFragment extends Fragment
                                    implements SharedPreferences.OnSharedPreferenceChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    public MapBottleAdapter mAdapter;
    private MapView mapView;
    private GoogleMap map;
    private CameraUpdate cameraUpdate;
    private GoogleApiClient mGooglePlayClient;
    private boolean mapsVal;

    public static StaticBottlesFragment newInstance(GoogleApiClient gpc) {
        StaticBottlesFragment fragment = new StaticBottlesFragment();
        return fragment;
    }

    public StaticBottlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve the preferences from this fragment's context.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        preferences.registerOnSharedPreferenceChangeListener(this);
        mapsVal = preferences.getBoolean("map_switch", false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_static_bottles, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.static_bottles);
        mRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        Inventory activity = (Inventory) getActivity();
        Location currentLocation = activity.getCurrentLocation();
        ParseGeoPoint geoPoint = new ParseGeoPoint();
        if (currentLocation == null) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        geoPoint.setLatitude(currentLocation.getLatitude());
        geoPoint.setLongitude(currentLocation.getLongitude());
        mAdapter = new MapBottleAdapter(getContext(), container, geoPoint, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getItemAnimator().setRemoveDuration(250);
        mRecyclerView.getItemAnimator().setAddDuration(500);


            ImageButton home = (ImageButton) v.findViewById(R.id.my_location);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Inventory activity = (Inventory) getActivity();
                    Location currentLocation = activity.getCurrentLocation();
                    if (currentLocation == null) {
                        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 20);
                    map.animateCamera(cameraUpdate);
                }
            });

        if(!mapsVal)
        {
            home.setVisibility(View.GONE);
            v.findViewById(R.id.mapview).setVisibility(View.GONE);
        }
        else {
            showMap(v,savedInstanceState);
        }

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

        return v;
    }

    public void clearMarkers() {
        if (map != null)
            map.clear();
    }

    public void addMarker(AvailableBottle bottle) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(bottle.getMessage());

        try {
            markerOptions.snippet(bottle.getLastUser().fetchIfNeeded().getUsername());
        } catch (ParseException e){
            markerOptions.snippet("pirate");
            e.printStackTrace();
        }

        markerOptions.position(new LatLng(bottle.getPoint().getLatitude(), bottle.getPoint().getLongitude()));
        map.addMarker(markerOptions);
    }

    /** MapView OVERRIDES **/
    //For MapView to resume when parent view resumes
    @Override
    public void onResume() {

        if(mapsVal && mapView != null)
        {mapView.onResume();}
        else if(!mapsVal)
        {
            getView().findViewById(R.id.my_location).setVisibility(View.GONE);
            getView().findViewById(R.id.mapview).setVisibility(View.GONE);
            mapView = null;
        }
        else {
            showMap(getView(), null);
            getView().findViewById(R.id.my_location).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.mapview).setVisibility(View.VISIBLE);
        }
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

    private void showMap (View v, Bundle savedInstanceState){

        //        if(mapsVal){
        //            Snackbar.make(v, "Maps Works!", Snackbar.LENGTH_SHORT).show();
        //        }
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
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("map_switch")) 
            mapsVal = sharedPreferences.getBoolean("map_switch", false);
//
//            if(mapsVal&&mapView!=null)
//            {}
//
//            else if(mapsVal && mapView ==null)
//            {showMap(getView(), null);}
//
//            else//no MapsVal
//            {//findViewById(R.id.mapview).setVisibility(View.GONE);}
//            }
        //}
    }
}