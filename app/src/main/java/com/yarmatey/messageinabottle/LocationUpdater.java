package com.yarmatey.messageinabottle;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Jason on 10/10/2015.
 */
public class LocationUpdater {

    /**
     * CLASS CONSTANTS
     **/
    //Constant for range of pickup
    public static final double RANGE = .01;

    //Constant for checking minimum accuracy to check for bottles
    public static final double MIN_ACCURACY = 20;

    private static ParseObject foundBottle;

    public static ParseObject checkForBottle(final Context context, Location location) {
        if (location != null && location.getAccuracy() < MIN_ACCURACY && location.getAccuracy() != 0) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log
            //Create a point that Parse knows what the location is.
            ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            //Replicating the below code:
            //ParseGeoPoint userLocation = (ParseGeoPoint) foundBottle.get("location");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("bottle");
            query.whereNear("location", point);
            //Retrieve 1 Bottle. Do not proceed unto 2.
            query.setLimit(1);
            query.whereWithinKilometers("location", point, RANGE);
            //Now to run the query:
            if (foundBottle == null)
                foundBottle = new ParseObject("ghost");
            foundBottle.put("message", "null");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> localBottle, ParseException e) {
                    if (e == null && !localBottle.isEmpty()) {
                        Log.d("location", "Retrieved Lat: " + localBottle.get(0).getParseGeoPoint("location").getLatitude() + ", Lon: " + localBottle.get(0).getParseGeoPoint("location").getLongitude());
                        ParseGeoPoint bottleLoc = localBottle.get(0).getParseGeoPoint("location");
                        foundBottle.put("location", bottleLoc);
                        String message = localBottle.get(0).getString("message");
                        foundBottle.put("message", message);
                        int type = localBottle.get(0).getInt("type");
                        foundBottle.put("type", type);
                        foundBottle.saveInBackground();
                        localBottle.get(0).deleteInBackground();
                        //message.setText(foundBottle.getString("message"));
                        Toast.makeText(context, foundBottle.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (e == null) {
                            Log.d("location", "No Bottles!");
                        } else {
                            Log.d("location", "Error: " + e.getMessage());
                        }
                    }
                }
            });
            if (foundBottle.get("message") != "null")
                return foundBottle;
        }
        return null;
    }
}