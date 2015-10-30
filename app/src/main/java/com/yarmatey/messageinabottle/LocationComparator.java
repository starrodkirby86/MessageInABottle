package com.yarmatey.messageinabottle;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Jason on 10/10/2015.
 */
public class LocationComparator implements LocationListener {

    /** CLASS CONSTANTS **/
    //Constant for range of pickup
    public static final double RANGE = 7;

    //Constant for checking minimum accuracy to check for bottles
    public static final double MIN_ACCURACY = 20;

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log

            //TODO ADD PARSE PICKUP HERE
        }
    }
}
