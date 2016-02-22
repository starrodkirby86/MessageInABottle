package com.yarmatey.messageinabottle.bottles;

/**
 * Created by Jonathan on 1/27/2016.
 */
public enum BottleAttribute {
    Location("location"),
    Message("message"),
    Status("type"),
    Author("author"),
    LastUser("user"),
    Ratings("ratings"),
    Date("date"),
    Created("created")
    ;


    /**
     * String value of specified
     * Parse BottleAttribute (database column name).
     */
    public final String value;

    /**
     * Private constructor for Parse BottleAttribute enum.
     * @param value - Parse database column name
     */
    BottleAttribute(String value) {
        // Save string value
        this.value = value;
    }
}
