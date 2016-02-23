package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseClassName;


/**
 * Created by root on 11/5/15.
 */
@ParseClassName("PickedUpBottle")
public class PickedUpBottle extends Bottle {

    public PickedUpBottle() {
        // Initialize superclass
        // Needed for Parse
        super();
        // Initialize rated to zero
        super.setPreviousRating(0);
    }

//    public static ParseQuery<PickedUpBottle> getQuery() {
//        return ParseQuery.getQuery(PickedUpBottle.class);
//    }
}
