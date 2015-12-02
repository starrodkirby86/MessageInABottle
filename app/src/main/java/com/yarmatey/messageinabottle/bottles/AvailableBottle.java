package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 11/25/2015.
 */

@ParseClassName("AvailableBottle")
public class AvailableBottle extends ParseObject {

    public static final String LOCATION = "location";
    public static final String MESSAGE = "message";
    public static final String TYPE = "type";
    public static final String AUTHOR = "author";
    public static final String LAST_USER = "user";
    public static final String COMMENTS = "comments";
    public static final String RATINGS = "ratings";


    public AvailableBottle() {
        super();
    }
    public static ParseQuery<AvailableBottle> getQuery(ParseGeoPoint point, double range) {
        ParseQuery<AvailableBottle> query = ParseQuery.getQuery(AvailableBottle.class);
        query.whereNear(LOCATION, point);
        //Retrieve 1 PickedUpBottle. Do not proceed unto 2.
        query.setLimit(1);
        query.whereWithinKilometers(LOCATION, point, range);
        query.whereNotEqualTo(LAST_USER, ParseUser.getCurrentUser());
        return query;
    }

    public ParseGeoPoint getPoint() {return getParseGeoPoint(LOCATION);}
    public String getMessage(){return getString(MESSAGE);}
    public int getBottleType(){return getInt(TYPE);}
    public ParseUser getLastUser(){return getParseUser(LAST_USER);}
    public List<String> getComments() {return getList(COMMENTS);}
    public ParseUser getAuthor() {return getParseUser(AUTHOR);}
    public List<Integer> getRatings() {
        List<Object> list = getList(RATINGS);
        List<Integer> ratings = new ArrayList<>(4);
        if (list == null || list.size() < 4) {
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
    public void setAuthor(ParseUser author) {
        put(AUTHOR, author);
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
        put(RATINGS,ratings);
    }

    public void addComment(String newComment) {
        //comments.add(newComment);
        List<String> old = getComments();
        old.add(newComment);
        setComments(old);
    }
    public void setAll(ParseGeoPoint point, String message, int type, ParseUser author, ParseUser user, List<String> comments, List<Integer> ratings) {
        setPoint(point);
        setMessage(message);
        setBottleType(type);
        setAuthor(author);
        setLastUser(user);
        setComments(comments);
        setRatings(ratings);
    }
    public void setAll(PickedUpBottle newObject){
        setPoint(newObject.getPoint());
        setMessage(newObject.getMessage());
        setAuthor(newObject.getAuthor());
        setBottleType(newObject.getBottleType());
        setLastUser(newObject.getLastUser());
        setComments(newObject.getComments());
        setRatings(newObject.getRatings());
    }
}
