package com.yarmatey.messageinabottle.bottles;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.yarmatey.messageinabottle.sql.BottleContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jonathan on 1/27/2016.
 */
public class Bottle {

    /**
     * Generic Bottle class,
     * built on top of Parse layer.
     *
     * Values of the bottle class:
     *
     *  Message: The message in the bottle
     *  Author: The person who wrote the message
     *  Location: The location at which the message was last dropped
     *  Status: The current status of the bottle, see BottleStatus for more
     *  LastUser: The last user to have seen the bottle
     *  Created: The date the bottle was created
     *  LastUpdated: The date the bottle was last dropped
     *  Ratings: The current ratings of the message
     *
     * Helper Values:
     *
     *  previousRating: value that was previously rated, See BottleRatings for more
     *  contentValues: Formats values to insert into database
     */
    private List<Integer> ratings;
    private String author;
    private String lastUser;
    private String message;
    private int status;
    private Location location;
    private Calendar created;
    private Calendar lastUpdated;
    private ContentValues contentValues;
    private int previousRating;

    public static final SimpleDateFormat DATE_STRING_FORMAT
            = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy", Locale.US);

    public Bottle() {
        // Needed for Parse
        contentValues = new ContentValues();
        setMessage("Test message");
        setAuthor("Pirate");
        setStatus(BottleStatus.Available.state);
        setLastUser("Another Pirate");
        setPreviousRating(0);
        setLastUpdated(Calendar.getInstance());
        setCreated(Calendar.getInstance());
        Location location = new Location("");
        location.setLongitude(35);
        location.setLatitude(-120);
        setLocation(location);
        List<Integer> array = new ArrayList<>(0);
        for (int i = 0; i < 4; i++)
            array.add(i, 0);
        setRatings(array);

    }

    public Bottle(Cursor cursor) {

        contentValues = new ContentValues();

        //set Message
        String temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.Message.value));
        setMessage(temp);
        //Set Author
        temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.Author.value));
        setAuthor(temp);

        //Set status
        int tempInt = cursor.getInt(cursor.getColumnIndex(BottleAttribute.Status.value));
        setStatus(tempInt);

        //set Last user
        temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.LastUser.value));
        setLastUser(temp);

        //Set created date
        temp = cursor.getString(
                cursor.getColumnIndex(BottleAttribute.Created.value));
        setCreated(temp);

        //Set date last updated
        temp = cursor.getString(
                cursor.getColumnIndex(BottleAttribute.Date.value));
        setLastUpdated(temp);

        //Set location
        double lat = cursor.getDouble(
                cursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_LATITUDE));
        double lon = cursor.getDouble(
                cursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_LONGITUDE));
        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        setLocation(loc);

        //Set ratings
        temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.Ratings.value));
        if (temp == null)
            temp = "0 0 0 0";
        setRatings(temp);
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public boolean readyToInsert() {

        Log.d("Bottle DB", "Verifying values for DB");
        Log.d("Bottle DB", "ContentValue Length :: " + contentValues.size());
        int size = 0;
        for (BottleAttribute attribute : BottleAttribute.values()) {
            if (!contentValues.containsKey(attribute.value)) {
                Log.e("Bottle DB", "Verification FAILED: Key " + attribute.value + " is missing");
                return false;
            }
            size++;
        }
        if (contentValues.size() != size)
            return false;
        else
            return true;
    }


    // ------------ Message Getter and Setters ------------ \\
    public String getMessage() {
        // Get state from Parse
        return message;
    }

    public void setMessage(String message){
        // Save state to Parse
        if (message == null)
            return;
        this.message = message;
        contentValues.put(BottleAttribute.Message.value, message);
    }


    // ------------ Status Getter and Setters ------------ \\

    public int getStatus() {
        // Get state from Parse
        return status;
    }

    public void setStatus(int status){
        if (!BottleStatus.valid(status))
            return;
        this.status = status;
        contentValues.put(BottleAttribute.Status.value, status);
    }

    // ------------ Last User Getter and Setters ------------ \\

    public String getLastUser() {
        // Get state from Parse
        return lastUser;
    }

    public void setLastUser(String user){
        if (user == null)
            return;
        // Save state to Parse
        this.lastUser = user;
        contentValues.put(BottleAttribute.LastUser.value, user);
    }


    // ------------ Author Getter and Setters ------------ \\

    public String getAuthor() {

        // Get state from Parse
        return author;
    }

    public void setAuthor(String author) {
        if (author == null)
            return;
        this.author = author;
        contentValues.put(BottleAttribute.Author.value, author);
    }


    // ------------ Ratings Getter and Setters ------------ \\


    public List<Integer> getRatings() {

        //TODO FIX THIS, IT IS BAD
        if (this.ratings != null)
            return (this.ratings);
        String values = contentValues.getAsString(BottleAttribute.Ratings.value);
        this.ratings = new ArrayList<>(4);
        if (values == null || values.length() == 0) {
            values = "0 0 0 0";
        }
        String [] splitValues = values.split(" ");
        for (int i = 0; i < 4; i++) {
            ratings.set(i, Integer.valueOf(splitValues[i]));
        }
        return (this.ratings);
    }

    public void setRatings(String ratings) {
        String [] tempSplit = ratings.split(" ");
        if (tempSplit.length < 4)
            tempSplit = new String[]{"0", "0", "0", "0"};
        ArrayList<Integer> rating = new ArrayList<>(0);
        for (int i = 0; i < 4; i++) {
            rating.add(Integer.valueOf(tempSplit[i]));
        }
        this.ratings = rating;
    }
    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
        saveRatings();
    }

    private void saveRatings() {
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
        this.location = location;
        contentValues.put(BottleAttribute.Latitude.value, location.getLatitude());
        contentValues.put(BottleAttribute.Longitude.value, location.getLongitude());
    }

    public Location getLocation() {
        return location;
    }


    // ------------ Created Date Getter and Setters ------------ \\

    public void setCreated(Calendar created) {
        this.created = created;
        contentValues.put(BottleAttribute.Created.value, DATE_STRING_FORMAT.format(this.created.getTime()));
    }

    public void setCreated(String timeCreated) {
        if (timeCreated == null)
            return;
        try {
            Calendar time = Calendar.getInstance();
            time.setTime(DATE_STRING_FORMAT.parse(timeCreated));
            setCreated(time); //set created

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Calendar getCreated() {
        return created;
    }


    // ------------ LastedUpdated Getter and Setters ------------ \\

    public void setLastUpdated(Calendar lastUpdated) {
        this.lastUpdated = lastUpdated;
        contentValues.put(BottleAttribute.Date.value, this.lastUpdated.toString());
    }

    public void setLastUpdated(String  timeUpdated) {
        Calendar time = Calendar.getInstance();
        try {
            time.setTime(DATE_STRING_FORMAT.parse(timeUpdated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setLastUpdated(time); //set last updated
    }


    public Calendar getLastUpdated() {
        return lastUpdated;
    }

}
