package com.yarmatey.messageinabottle.bottles.enums;

/**
 * Created by Jason on 2/22/2016.
 */
public enum MessageRatings {

    NoRating(-1, ""),
    Worst(0, "Scurvy!"),
    Bad(1, "Nay"),
    Good(2, "Aye"),
    Best(3, "Yar har!");

    public final int index;
    public final String value;

    MessageRatings(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public static String getRating(int index) {
        switch (index){
            case 0:
                return Worst.value;
            case 1:
                return Bad.value;
            case 2:
                return Good.value;
            case 3:
                return Best.value;
            default:
                return NoRating.value;
        }
    }
}
