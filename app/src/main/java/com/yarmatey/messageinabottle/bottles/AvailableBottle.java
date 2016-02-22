package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Jason on 11/25/2015.
 */

@ParseClassName("AvailableBottle")
public class AvailableBottle extends Bottle {

    public AvailableBottle() {
        // Initialize superclass
        // Needed for Parse
        super();
    }

//    public static ParseQuery<AvailableBottle> getQuery(ParseGeoPoint point, double range) {
//        ParseQuery<AvailableBottle> query = ParseQuery.getQuery(AvailableBottle.class);
//        query.whereNear(BottleAttribute.Location.value, point);
//        //Retrieve 1 PickedUpBottle. Do not proceed unto 2.
//        query.setLimit(1);
//        query.whereWithinKilometers(BottleAttribute.Location.value, point, range);
//        query.whereNotEqualTo(BottleAttribute.LastUser.value, ParseUser.getCurrentUser());
//        return (query);
//    }

    public void setAll(ParseGeoPoint point, String message, int type, ParseUser author, ParseUser user, List<String> comments, List<Integer> ratings) {
        // Overriding this method here since
        // Bottle class savings ratings after
        // setting all values, so...
        // Repeat everything here, but exclude
        // saving ratings at the end.

        //super.setPoint(point);
        super.setMessage(message);
        super.setBottleStatus(type);
        //super.setAuthor(author);
        //super.setLastUser(user);
        //super.setComments(comments);
        super.setRatings(ratings);
    }
}
