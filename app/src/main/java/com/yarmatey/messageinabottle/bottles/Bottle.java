package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jonathan on 1/27/2016.
 */
public class Bottle extends ParseObject {
    /**
     *
     */
    private int rated;
    /**
     * Latest ratings given by users.
     */
    private List<Integer> ratings;


    /**
     * Generic Bottle class,
     * built on top of Parse layer.
     */
    public Bottle() {
        // Needed for Parse
        super();
    }


    public ParseGeoPoint getPoint() {
        // Get value from Parse
        return super.getParseGeoPoint(BottleAttribute.Location.value);
    }

    public void setPoint(ParseGeoPoint p){
        // Save value to Parse
        super.put(BottleAttribute.Location.value, p);
    }


    public String getMessage() {
        // Get value from Parse
        return super.getString(BottleAttribute.Message.value);
    }

    public void setMessage(String m){
        // Save value to Parse
        super.put(BottleAttribute.Message.value, m);
    }


    public int getBottleType() {
        // Get value from Parse
        return super.getInt(BottleAttribute.Type.value);
    }

    public void setBottleType(int t){
        // Save value to Parse
        super.put(BottleAttribute.Type.value, t);
    }


    public ParseUser getLastUser() {
        // Get value from Parse
        return super.getParseUser(BottleAttribute.LastUser.value);
    }

    public void setLastUser(ParseUser user){
        // Save value to Parse
        super.put(BottleAttribute.LastUser.value, user);
    }


    public List<String> getComments() {
        // Get value from Parse
        return super.getList(BottleAttribute.Comments.value);
    }

    public void setComments(List<String> c){
        // Save value to Parse
        super.put(BottleAttribute.Comments.value, c);
    }

    public void addComment(String newComment) {
        // In order to preserve all previous comments,
        // all comments must be loaded from Parse first
        List<String> old = this.getComments();
        // Then, we can append new comment to this list
        old.add(newComment);
        // And update Parse with this information
        this.setComments(old);
    }


    public ParseUser getAuthor() {
        // Get value from Parse
        return super.getParseUser(BottleAttribute.Author.value);
    }

    public void setAuthor(ParseUser author) {
        // Save value to Parse
        super.put(BottleAttribute.Author.value, author);
    }


    public List<Integer> getRatings() {
        // If ratings have already been loaded,
        // return list of ratings immediately
        if (this.ratings != null)
            return (this.ratings);
        // Otherwise, attempt to get information from Parse
        List<Object> list = super.getList(BottleAttribute.Ratings.value);
        // Create empty list of ratings
        this.ratings = new ArrayList<>(4);
        // Check if there are any previous ratings
        // for this specific bottle
        if (list == null || list.size() == 0) {
            // If no information, create empty list of zeros
            list = new ArrayList<>(Arrays.asList(new Object[] {
                0, 0, 0, 0
            }));
        }
        // Fill local ratings list
        for (int i = 0; i < 4; i++) {
            // If any Parse information,
            // get ratings directly from there
            if(i < list.size()) {
                this.ratings.add((int) list.get(i));
            } else {
                // Otherwise, pad with zero
                this.ratings.add(0);
            }
        }
        // Return final list of ratings
        return (this.ratings);
    }

    public void setRatings(List<Integer> ratings) {
        // Save values locally only
        this.ratings = ratings;
    }

    private void saveRatings() {
        // Save values to Parse
        super.put(BottleAttribute.Ratings.value, ratings);
    }


    public int getRated() {
        // Get local value
        return (this.rated);
    }

    public void setRated(int rated) {
        // Save value locally
        this.rated = rated;
    }


    public void setAll(ParseGeoPoint point, String message, int type, ParseUser author, ParseUser user, List<String> comments, List<Integer> ratings) {
        // Update all attributes of this bottle
        this.setPoint(point);
        this.setMessage(message);
        this.setBottleType(type);
        this.setAuthor(author);
        this.setLastUser(user);
        this.setComments(comments);
        this.setRatings(ratings);
        // NOTE: Ratings must be set and
        // updated before they are saved here!
        this.saveRatings();
    }

    public void setAll(Bottle newObject) {
        // Re-use our other function for setting
        // all attributes of this bottle
        this.setAll(
                newObject.getPoint(),
                newObject.getMessage(),
                newObject.getBottleType(),
                newObject.getAuthor(),
                newObject.getLastUser(),
                newObject.getComments(),
                newObject.getRatings()
        );
    }
}
