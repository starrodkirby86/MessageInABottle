package com.yarmatey.messageinabottle.bottles;

/**
 * Created by Jonathan on 1/27/2016.
 */
public enum BottleAttribute {
    Location("location"),
    Message("message"),
    Type("type"),
    Author("author"),
    LastUser("user"),
    Comments("comments"),
    Ratings("ratings"),
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
    private BottleAttribute(String value) {
        // Save string value
        this.value = value;
    }
}
