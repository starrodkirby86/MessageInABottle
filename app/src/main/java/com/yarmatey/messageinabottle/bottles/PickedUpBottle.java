package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 11/5/15.
 */
@ParseClassName("PickedUpBottle")
public class PickedUpBottle extends ParseObject {

    public static final String LOCATION = "location";
    public static final String MESSAGE = "message";
    public static final String TYPE = "type";
    public static final String AUTHOR = "author";
    public static final String LAST_USER = "user";
    public static final String COMMENTS = "comments";
    public static final String RATINGS = "ratings";

    private int rated;
    private List<Integer> ratings;

    public PickedUpBottle() {
        super();
        rated = 0;
    }

    public static ParseQuery<PickedUpBottle> getQuery() {
        return ParseQuery.getQuery(PickedUpBottle.class);
    }

    public ParseGeoPoint getPoint(){return getParseGeoPoint(LOCATION);}
    public String getMessage(){return getString(MESSAGE);}
    public int getBottleType(){return getInt(TYPE);}
    public ParseUser getLastUser(){return getParseUser(LAST_USER);}
    public List<String> getComments() {return getList(COMMENTS);}
    public ParseUser getAuthor() {return getParseUser(AUTHOR);}
    public List<Integer> getRatings() {
        if (ratings != null)
            return ratings;
        List<Object> list = getList(RATINGS);
        ratings = new ArrayList<>(4);
        if (list == null) {
            for (int i = 0; i < 4; i++)
                ratings.add(0);
            setRatings(ratings);
            return ratings;
        }
        for (Object item : list) {
            ratings.add((int) item);
        }
        return ratings;
    }
    public int getRated() {return rated;}

    public void setRated(int rated) { this.rated = rated;}
    public void setPoint(ParseGeoPoint p){
        //point = p;
        put(LOCATION, p);
    }
    public void setMessage(String m){
        //message = m;
        put(MESSAGE,m);
    }
    public void setBottleType(int t){
        //bottleType = t;
        put(TYPE,t);
    }
    public void setLastUser(ParseUser user){
        //user = u;
        put(LAST_USER, user);
    }
    public void setComments(List<String> c){
        //comments = c;
        put(COMMENTS, c);
    }
    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }
    private void saveRatings() {
        put(RATINGS, ratings);
    }
    public void addComment(String newComment){
        //comments.add(newComment);
        List<String> old = getComments();
        old.add(newComment);
        setComments(old);
    }
    public void setAuthor(ParseUser author) {
        put(AUTHOR, author);
    }
    public void setAll(AvailableBottle newObject){
        setPoint(newObject.getPoint());
        setMessage(newObject.getMessage());
        setBottleType(newObject.getBottleType());
        setLastUser(newObject.getLastUser());
        setComments(newObject.getComments());
        setAuthor(newObject.getAuthor());
        setRatings(newObject.getRatings());
        saveRatings();
    }
}