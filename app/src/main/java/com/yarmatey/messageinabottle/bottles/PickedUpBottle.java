package com.yarmatey.messageinabottle.bottles;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by root on 11/5/15.
 */
@ParseClassName("PickedUpBottle")
public class PickedUpBottle extends ParseObject {

    public PickedUpBottle() {
        super();
    }

    public static ParseQuery<PickedUpBottle> getQuery() {
        return ParseQuery.getQuery(PickedUpBottle.class);
    }

    public ParseGeoPoint getPoint(){return getParseGeoPoint("point");}
    public String getMessage(){return getString("message");}
    public int getBottleType(){return getInt("bottleType");}
    public ParseUser getLastUser(){return getParseUser("user");}
    public List<String> getComments() {return getList("comments");}

    public void setPoint(ParseGeoPoint p){
        //point = p;
        put("point", p);
    }
    public void setMessage(String m){
        //message = m;
        put("message",m);
    }
    public void setBottleType(int t){
        //bottleType = t;
        put("type",t);
    }
    public void setLastUser(ParseUser user){
        //user = u;
        put("user", user);
    }
    public void setComments(List<String> c){
        //comments = c;
        put("comments", c);
    }

    public void addComment(String newComment){
        //comments.add(newComment);
        List<String> old = getComments();
        old.add(newComment);
        setComments(old);
    }
    public void setAll(AvailableBottle newObject){
        setPoint(newObject.getPoint());
        setMessage(newObject.getMessage());
        setBottleType(newObject.getBottleType());
        setLastUser(newObject.getLastUser());
        setComments(newObject.getComments());
    }
}
