package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Jason on 11/27/2015.
 */
@ParseClassName("PirateMast")
public class PirateMast extends ParseObject {

    public PirateMast() {
        // Initialize superclass
        // Needed for Parse
        super();
    }

    public static ParseQuery<PirateMast> getQuery(ParseGeoPoint point, double range, int maxPosts) {
        ParseQuery<PirateMast> query = ParseQuery.getQuery(PirateMast.class);
        //query.whereWithinKilometers(BottleAttribute.Location.state, point, range);
        query.setLimit(maxPosts);
        return (query);
    }

    public static ParseQuery<PirateMast> getUserQuery(ParseUser user) {
        ParseQuery<PirateMast> query = ParseQuery.getQuery(PirateMast.class);
        query.whereEqualTo(BottleAttribute.Author.value, user);
        return (query);
    }
}
