package com.yarmatey.messageinabottle.bottles;

/**
 * Created by jason on 2/21/16.
 */
public enum BottleStatus {

    Available("available"),
    PickedUp("picked_up"),
    Map("map");

    public final String value;


    BottleStatus(String value) {
        // Save string value
        this.value = value;
    }

}
