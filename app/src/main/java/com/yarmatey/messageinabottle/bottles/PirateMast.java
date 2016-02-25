package com.yarmatey.messageinabottle.bottles;
/**
 * Created by Jason on 11/27/2015.
 */
public class PirateMast extends PirateMessage {

    public PirateMast() {
        // Initialize superclass
        // Needed for Parse
        super();
    }

    public String getObjectId() {
        return "Hello";
    }

    @Override
    public boolean readyToInsert() {
        return false;
    }
}
