package com.yarmatey.messageinabottle.bottles;

/**
 * Created by jason on 2/21/16.
 */
public enum BottleStatus {

    Available(0), //Keep lowest state if need to change
    PickedUp(1),
    Map(2); //Keep highest state, since independent of bottle types

    public final int state;

    BottleStatus(int value) {
        // Save string state
        this.state = value;
    }

    public static boolean valid (int value) {
        return !(value > Map.state || value < Available.state);
    }

}
