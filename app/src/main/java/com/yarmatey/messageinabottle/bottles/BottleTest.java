package com.yarmatey.messageinabottle.bottles;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.yarmatey.messageinabottle.sql.BottleContract;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jason on 2/21/16.
 */
public class BottleTest {

    private int rated;
    private Cursor mCursor;
    /**
     * Latest ratings given by users.
     */
    private int latCol;
    private int longCol;
    private int messageCol;
    private int statusCol;
    private int authorCol;
    private int ratingCol;
    private List<Integer> ratings;
    private Context mContext;

    /**
     * Generic Bottle class,
     * built on top of Parse layer.
     */
    public BottleTest(Cursor cursor, Context context) {
        // Needed for Parse
        this.mCursor = cursor;
        this.mContext = context;
        latCol = mCursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_LATITUDE);
        longCol = mCursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_LONGITUDE);
        messageCol = mCursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_MESSAGE);
        statusCol = mCursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_STATUS);
        authorCol = mCursor.getColumnIndex(BottleContract.BottleEntry.COLUMN_AUTHOR);
        ratingCol = mCursor.getColumnIndex(BottleAttribute.Ratings.value);
    }

    public void setCursor(Cursor Cursor) {
        this.mCursor = Cursor;
    }

    public Location getLocation() {
        // Get value from Pars
        Location location = new Location("");
        location.setLatitude(mCursor.getDouble(latCol));
        location.setLongitude(mCursor.getDouble(longCol));
        return location;
    }

//    public void setPoint(ParseGeoPoint p){
//        // Save value to Parse
//        super.put(BottleAttribute.Location.value, p);
//    }


    public String getMessage() {
        // Get value from Parse
        return mCursor.getString(messageCol);
    }

//    public void setMessage(String m){
//        // Save value to Parse
//        super.put(BottleAttribute.Message.value, m);
//    }


    public int getBottleType() {
        // Get value from Parse
        return mCursor.getInt(statusCol);
    }

//    public void setBottleType(int t){
//        // Save value to Parse
//        super.put(BottleAttribute.Status.value, t);
//    }


//    public ParseUser getLastUser() {
//        // Get value from Parse
//        return super.getParseUser(BottleAttribute.LastUser.value);
//    }

//    public void setLastUser(ParseUser user){
//        // Save value to Parse
//        super.put(BottleAttribute.LastUser.value, user);
//    }

//    public List<String> getComments() {
//        // Get value from Parse
//        return getList(BottleAttribute.Comments.value);
//    }
//
//    public void setComments(List<String> c){
//        // Save value to Parse
//        super.put(BottleAttribute.Comments.value, c);
//    }
//
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
        return mCursor.getString(authorCol);
    }

//    public void setAuthor(ParseUser author) {
//        // Save value to Parse
//        super.put(BottleAttribute.Author.value, author);
//    }


    public List<Integer> getRatings() {
        // If ratings have already been loaded,
        // return list of ratings immediately
        if (this.ratings != null)
            return (this.ratings);
        // Otherwise, attempt to get information from Parse
        String ratingString = mCursor.getString(ratingCol);
        // Create empty list of ratings
        this.ratings = new ArrayList<>();
        // Check if there are any previous ratings
        // for this specific bottle
        if (ratingString == null || ratingString.length() == 0) {
            // If no information, create empty list of zeros
            ratingString = "0 0 0 0";
        }
        String [] split = ratingString.split(" ");
        // Fill local ratings list
        for (int i = 0; i < ratingString.length(); i++) {
            ratings.add(i, Integer.valueOf(split[i]));
        }
        // Return final list of ratings
        return this.ratings;
    }

//    public void setRatings(List<Integer> ratings) {
//        // Save values locally only
//        this.ratings = ratings;
//    }

//    private void saveRatings() {
//        // Save values to Parse
//        super.put(BottleAttribute.Ratings.value, ratings);
//    }


    public int getRated() {
        // Get local value
        return (this.rated);
    }

    public void setRated(int rated) {
        // Save value locally
        this.rated = rated;
    }


//    public void setAll(ParseGeoPoint point, String message, int type, ParseUser author, ParseUser user, List<String> comments, List<Integer> ratings) {
//        // Update all attributes of this bottle
//        this.setPoint(point);
//        this.setMessage(message);
//        this.setBottleType(type);
//        this.setAuthor(author);
//        this.setLastUser(user);
//        this.setComments(comments);
//        this.setRatings(ratings);
//        // NOTE: Ratings must be set and
//        // updated before they are saved here!
//        this.saveRatings();
//    }

//    public void setAll(Bottle newObject) {
//        // Re-use our other function for setting
//        // all attributes of this bottle
//        this.setAll(
//                newObject.getPoint(),
//                newObject.getMessage(),
//                newObject.getBottleType(),
//                newObject.getAuthor(),
//                newObject.getLastUser(),
//                newObject.getComments(),
//                newObject.getRatings()
//        );
//    }

//    public void setStatus(int newType) {
//        mContext.getContentResolver().update(
//                BottleContract.BottleEntry.CONTENT_URI,
//
//        );
//    }

}
