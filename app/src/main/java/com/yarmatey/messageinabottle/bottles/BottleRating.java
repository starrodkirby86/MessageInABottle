package com.yarmatey.messageinabottle.bottles;

/**
 * Created by Jason on 2/22/2016.
 */
public enum BottleRating {

    NoRating(0),
    Worst(1),
    Bad(2),
    Good(3),
    Best(4);

    public final int value;


    BottleRating(int value) {
        this.value = value;
    }
}
