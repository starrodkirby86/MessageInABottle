package com.gmail.jhernandez5922.messageinabottle;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by Jason on 10/10/2015.
 */
public class LocationComparator implements Comparator<Location> {

    @Override
    public int compare(Location lhs, Location rhs) {
        double longDiff = lhs.getLongitude() - rhs.getLongitude();
        double latDiff = lhs.getLatitude() - rhs.getLatitude();
        int lat = latDiff > 0 ? 1 : -1; //Set difference as 1 is greater, -1 is less
        int lon = longDiff > 0 ? 1 : -1;
        if (longDiff == 0) { //if longitude is the same, return latitude
            return latDiff == 0 ? 0 : lat; //if lat not same, use lat difference as indicator
        }
        else
            return lon; //if not return longitude
    }
}
