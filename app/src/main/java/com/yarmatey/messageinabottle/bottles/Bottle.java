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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonathan on 1/27/2016.
 */
public class Bottle {
    /**
     *
     */
    private int rated;
    /**
     * Latest ratings given by users.
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

    public ContentValues getContentValues() {
        return contentValues;
    }

    /**
     * Generic Bottle class,
     * built on top of Parse layer.
     */
    public Bottle() {
        // Needed for Parse
        contentValues = new ContentValues();
        setMessage("Test message");
        setAuthor("Pirate");
        setBottleStatus(0);
        setLastUser("Another Pirate");
        setRated(0);
        setLastUpdated(Calendar.getInstance());
        setCreated(Calendar.getInstance());
        Location location = new Location("");
        location.setLongitude(35);
        location.setLatitude(-120);
        setLocation(location);
        List<Integer> array = new ArrayList<>(0);
        for (int i = 0; i < 4; i++)
            array.add(i, i);
        setRatings(array);

    }

    public Bottle(Cursor cursor) {
        String temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.Message.value));
        if (temp == null)
            temp = "";
        setMessage(temp); //insert message
        temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.Author.value));
        if (temp == null)
            temp = "";
        setAuthor(temp); //insert author
        int tempInt = cursor.getInt(cursor.getColumnIndex(BottleAttribute.Status.value));
        setBottleStatus(tempInt); //insert status
        temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.LastUser.value));
        if (temp == null)
            temp = "";
        setLastUser(temp);
        temp = cursor.getString(
                cursor.getColumnIndex(BottleAttribute.Created.value));
        setCreated(temp);
        temp = cursor.getString(
                cursor.getColumnIndex(BottleAttribute.Date.value));
        setLastUpdated(temp);
        double lat = cursor.getDouble(
                cursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_LATITUDE));
        double lon = cursor.getDouble(
                cursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_LONGITUDE));
        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        setLocation(loc); //set latitude and longitude
        temp = cursor.getString(cursor.getColumnIndex(BottleAttribute.Ratings.value));
        if (temp == null)
            temp = "0 0 0 0";
        setRatings(temp); //set ratings
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

    }

    public String getMessage() {
        // Get value from Parse
        return message;
    }

    public void setMessage(String message){
        // Save value to Parse
        this.message = message;
        contentValues.put(BottleAttribute.Message.value, message);
    }


    public int getBottleStatus() {
        // Get value from Parse
        return status;
    }

    public void setBottleStatus(int status){
        // Save value to Parse
        this.status = status;
        contentValues.put(BottleAttribute.Status.value, status);
    }


    public String getLastUser() {
        // Get value from Parse
        return lastUser;
    }

    public void setLastUser(String user){
        // Save value to Parse
        this.lastUser = user;
        contentValues.put(BottleAttribute.LastUser.value, user);
    }


//    public List<String> getComments() {
//        // Get value from Parse
//        return
//    }

//    public void setComments(List<String> c){
//        // Save value to Parse
//        super.put(BottleAttribute.Comments.value, c);
//    }

//    public void addComment(String newComment) {
//        // In order to preserve all previous comments,
//        // all comments must be loaded from Parse first
//        List<String> old = this.getComments();
//        // Then, we can append new comment to this list
//        old.add(newComment);
//        // And update Parse with this information
//        this.setComments(old);
//    }


    public String getAuthor() {
        // Get value from Parse
        return author;
    }

    public void setAuthor(String author) {
        // Save value to Parse
        this.author = author;
        contentValues.put(BottleAttribute.Author.value, author);
    }


    public List<Integer> getRatings() {
        // If ratings have already been loaded,
        // return list of ratings immediately
        if (this.ratings != null)
            return (this.ratings);
        // Otherwise, attempt to get information from Parse
        String values = contentValues.getAsString(BottleAttribute.Ratings.value);
        // Create empty list of ratings
        this.ratings = new ArrayList<>(4);
        // Check if there are any previous ratings
        // for this specific bottle
        if (values == null || values.length() == 0) {
            // If no information, create empty list of zeros
            values = "0 0 0 0";
        }
        String [] splitValues = values.split(" ");
        // Fill local ratings list
        for (int i = 0; i < 4; i++) {
            // If any Parse information,
            // get ratings directly from there
            ratings.set(i, Integer.valueOf(splitValues[i]));
        }
        // Return final list of ratings
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
    }
    public void setRatings(List<Integer> ratings) {
        // Save values locally only
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
        // Save values to Parse
        contentValues.put(BottleAttribute.Ratings.value, builder.toString());
    }


    public int getRated() {
        // Get local value
        return (this.rated);
    }

    public void setRated(int rated) {
        // Save value locally
        this.rated = rated;
    }


    public void setLocation(Location location) {
        this.location = location;
        contentValues.put(BottleAttribute.Latitude.value, location.getLatitude());
        contentValues.put(BottleAttribute.Longitude.value, location.getLongitude());
    }

    public void setCreated(Calendar created) {
        this.created = created;
        contentValues.put(BottleAttribute.Created.value, this.created.toString());
    }

    public void setCreated(String timeCreated) {
        if (timeCreated == null)
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US); //insert last user
        try {
            Calendar time = Calendar.getInstance();
            time.setTime(sdf.parse(timeCreated));
            setCreated(time); //set created

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setLastUpdated(Calendar lastUpdated) {
        this.lastUpdated = lastUpdated;
        contentValues.put(BottleAttribute.Date.value, this.lastUpdated.toString());
    }

    public void setLastUpdated(String  timeUpdated) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US); //insert last user
        Calendar time = Calendar.getInstance();
        try {
            time.setTime(sdf.parse(timeUpdated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setLastUpdated(time); //set last updated
    }

}
