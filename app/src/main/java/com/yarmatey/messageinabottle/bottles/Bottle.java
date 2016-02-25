package com.yarmatey.messageinabottle.bottles;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.yarmatey.messageinabottle.bottles.enums.BottleAttribute;
import com.yarmatey.messageinabottle.bottles.enums.MessageStatus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jonathan on 1/27/2016.
 */
public class Bottle extends PirateMessage {

    /**
     * Generic Bottle class,
     * built on top of Parse layer.
     *
     * Values of the bottle class:
     *
     */
    public Bottle() {
        contentValues = new ContentValues();
        setMessage("Test message");
        setAuthor("Pirate");
        setStatus(MessageStatus.Available.state);
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
                cursor.getColumnIndex(BottleAttribute.Latitude.value));
        double lon = cursor.getDouble(
                cursor.getColumnIndex(BottleAttribute.Longitude.value));
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

    // ------------ Last User Getter and Setters ------------ \\

    public String getLastUser() {
        // Get state from db
        return contentValues.getAsString(BottleAttribute.LastUser.value);
    }

    public void setLastUser(String user){
        if (user == null)
            return;
        contentValues.put(BottleAttribute.LastUser.value, user);
    }

    // ------------ LastedUpdated Getter and Setters ------------ \\

    public void setLastUpdated(Calendar lastUpdated) {
        contentValues.put(BottleAttribute.Date.value, DATE_STRING_FORMAT.format(lastUpdated.getTime()));
    }

    public void setLastUpdated(String  lastUpdated) {
        if (lastUpdated == null)
            return;
        contentValues.put(BottleAttribute.Date.value, lastUpdated);
    }


    public Calendar getLastUpdated() {
        String timeStr = contentValues.getAsString(BottleAttribute.Date.value);
        Calendar time = Calendar.getInstance();
        try {
            time.setTime(DATE_STRING_FORMAT.parse(timeStr));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return time;
    }
}
