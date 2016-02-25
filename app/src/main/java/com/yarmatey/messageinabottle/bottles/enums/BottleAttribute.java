package com.yarmatey.messageinabottle.bottles.enums;

/**
 * Created by Jonathan on 1/27/2016.
 */
public enum BottleAttribute {
    Latitude("location"),
    Longitude("longitude"),
    Message("message"),
    Status("type"),
    Author("author"),
    LastUser("user"),
    Ratings("ratings"),
    Date("date"),
    Created("created");


    /**
     * String state of specified
     * Parse BottleAttribute (database column name).
     */
    public final String value;

    /**
     * Private constructor for Parse BottleAttribute enum.
     * @param value - Parse database column name
     */
    BottleAttribute(String value) {
        // Save string state
        this.value = value;
    }
}
