package com.yarmatey.messageinabottle.inventory;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.bottles.MapBottleAdapter;
import com.yarmatey.messageinabottle.bottles.PirateMast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaticBottlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaticBottlesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    public MapBottleAdapter mAdapter;
    private MapView mapView;
    private GoogleMap map;
    private CameraUpdate cameraUpdate;
    private HashMap<String, Marker> pirateMasts;

    public static StaticBottlesFragment newInstance() {
        StaticBottlesFragment fragment = new StaticBottlesFragment();
        return fragment;
    }

    public StaticBottlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pirateMasts = new HashMap<>();
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
        showMap(v, savedInstanceState);

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

    public void goToMarker(PirateMast mast) {
        Marker marker = pirateMasts.get(mast.getObjectId());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18);
        marker.showInfoWindow();
        map.animateCamera(cameraUpdate);
    }

    public Marker addMarkerIfUnique(PirateMast mast) {
        if(pirateMasts.get(mast.getObjectId()) == null)
            return addMarker(mast);
        else
            return null;
    }

    public Marker addMarker(PirateMast mast) {
        MarkerOptions markerOptions = new MarkerOptions();
        String title="A message from ";
        try {
            if(mast.getLastUser().fetchIfNeeded().getUsername().length()<=15)
                title = title + mast.getLastUser().fetchIfNeeded().getUsername();
            else
                title=title+"a pirate";
        } catch (ParseException e) {
            title = title+"a pirate";
            e.printStackTrace();
        }
        markerOptions.title(title);
        markerOptions.snippet(mast.getMessage());
        markerOptions.position(new LatLng(mast.getPoint().getLatitude(), mast.getPoint().getLongitude()));
        Marker marker = map.addMarker(markerOptions);
        pirateMasts.put(mast.getObjectId(), marker);
        return marker;
    }

    public void newMast() {
        mAdapter.itemInserted();
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

    private void showMap (View v, Bundle savedInstanceState){

        //Find mapView in layout and create the view
        //see onCreate below
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        //Allow GoogleMap to grab the MapView and initialize
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        //Initialize the map after GoogleMap is set up
        //MapsInitializer.initialize(this.getActivity());

        //Updates location and zoom.
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(36.815512, -119.750583), 15);
        map.animateCamera(cameraUpdate);
    }

}