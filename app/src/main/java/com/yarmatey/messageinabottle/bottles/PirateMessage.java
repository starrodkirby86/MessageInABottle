package com.yarmatey.messageinabottle.bottles;

import android.content.ContentValues;
import android.location.Location;

import com.yarmatey.messageinabottle.bottles.enums.BottleAttribute;
import com.yarmatey.messageinabottle.bottles.enums.MessageStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * *  Message: The message in the bottle
 *  Author: The person who wrote the message
 *  Location: The location at which the message was last dropped
 *  Status: The current status of the bottle, see MessageStatus for more
 *  Created: The date the bottle was created
 *  Ratings: The current ratings of the message
 *
 * Helper Values:
 *
 *  previousRating: value that was previously rated, See BottleRatings for more
 *  contentValues: Formats values to insert into database
 * Created by jason on 2/23/16.
 */
public abstract class PirateMessage {
    protected ContentValues contentValues;
    private int previousRating;

    public static final SimpleDateFormat DATE_STRING_FORMAT
            = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy", Locale.US);

    public ContentValues getContentValues() {
        return contentValues;
    }

    // ------------ Message Getter and Setters ------------ \\
    public String getMessage() {
        // Get state from Parse
        return contentValues.getAsString(BottleAttribute.Message.value);
    }

    public void setMessage(String message){
        // Save state to Db
        if (message == null)
            return;
        contentValues.put(BottleAttribute.Message.value, message);
    }

    // ------------ Status Getter and Setters ------------ \\

    public int getStatus() {
        // Get state from Parse
        return contentValues.getAsInteger(BottleAttribute.Status.value);
    }

    public void setStatus(int status){
        if (!MessageStatus.valid(status))
            return;
        contentValues.put(BottleAttribute.Status.value, status);
    }

    // ------------ Author Getter and Setters ------------ \\

    public String getAuthor() {
        return contentValues.getAsString(BottleAttribute.Author.value);
    }

    public void setAuthor(String author) {
        if (author == null)
            return;
        contentValues.put(BottleAttribute.Author.value, author);
    }

    // ------------ Ratings Getter and Setters ------------ \\


    public List<Integer> getRatings() {
        String values = contentValues.getAsString(BottleAttribute.Ratings.value);
        ArrayList<Integer> ratings = new ArrayList<>(4);
        if (values == null || values.length() == 0) {
            values = "0 0 0 0";
        }
        String [] splitValues = values.split(" ");
        for (int i = 0; i < 4; i++) {
            ratings.add(Integer.valueOf(splitValues[i]));
        }
        return ratings;
    }

    public void setRatings(String ratings) {
        contentValues.put(BottleAttribute.Ratings.value, ratings);
    }
    public void setRatings(List<Integer> ratings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ratings.size(); i++) {
            builder.append(ratings.get(i));
            if (i + 1 < ratings.size())
                builder.append(" ");
        }
        contentValues.put(BottleAttribute.Ratings.value, builder.toString());
    }

    // ------------ Rated Getter and Setters ------------ \\

    public int getPreviousRating() {
        return (this.previousRating);
    }

    public void setPreviousRating(int previousRating) {
        this.previousRating = previousRating;
    }

    // ------------ Location Getter and Setters ------------ \\

    public void setLocation(Location location) {
        contentValues.put(BottleAttribute.Latitude.value, location.getLatitude());
        contentValues.put(BottleAttribute.Longitude.value, location.getLongitude());
    }

    public Location getLocation() {
        Location location = new Location("");
        double lat = contentValues.getAsDouble(BottleAttribute.Latitude.value);
        double lon = contentValues.getAsDouble(BottleAttribute.Longitude.value);
//        if (lat.isNaN())
//            lat = 0.0;
//        if (lon.isNaN())
//            lon = 0.0;
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }


    // ------------ Created Date Getter and Setters ------------ \\

    public void setCreated(Calendar created) {
        contentValues.put(BottleAttribute.Created.value, DATE_STRING_FORMAT.format(created.getTime()));
    }

    public void setCreated(String timeCreated) {
        if (timeCreated == null)
            return;
        contentValues.put(BottleAttribute.Created.value, timeCreated);
    }

    public Calendar getCreated() {
        String timeStr = contentValues.getAsString(BottleAttribute.Created.value);
        Calendar time = Calendar.getInstance();
        try {
            time.setTime(DATE_STRING_FORMAT.parse(timeStr));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return time;
    }

    public abstract boolean readyToInsert();
}
