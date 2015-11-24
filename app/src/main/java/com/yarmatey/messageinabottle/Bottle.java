package com.yarmatey.messageinabottle;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 11/5/15.
 */
public class Bottle extends ParseObject {
/*
    private ParseGeoPoint point;
    private String message;
    private int bottleType;
    private String user;
    private List<String> comments;
*/
    public Bottle(){
        /*
            point = new ParseGeoPoint(0,0); //currentLocation.getLatitude(), currentLocation.getLongitude());
            message = "";
            bottleType = -1;
            user = "";
            comments = new ArrayList<>();
        */

        put("point", new ParseGeoPoint(0,0));
        put("message", "");
        put("type", -1);
        put("user", "");
        put("comments", new ArrayList<>());
    }

    public Bottle(ParseGeoPoint p, String m, int t, String u, List<String> c){
        /*
            point = p; //currentLocation.getLatitude(), currentLocation.getLongitude());
            message = m;
            bottleType = t;
            user = u;
            comments = c;
        */

        put("point", p);
        put("message", m);
        put("type", t);
        put("user", u);
        put("comments", c);
    }

    public ParseGeoPoint getPoint(){return getParseGeoPoint("point");}
    public String getMessage(){return get("message").toString();}
    public int getBottleType(){return getInt("bottleType");}
    public String getUser(){return get("user").toString();}
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
    public void setUser(String u){
        //user = u;
        put("user",u);
    }
    public void setComments(List<String> c){
        //comments = c;
        put("comments",c);
    }

    public void addComment(String newComment){
        //comments.add(newComment);
        List<String> old = getComments();
        old.add(newComment);
        setComments(old);
    }
    public void setAll(Bottle newObject){
        setPoint(newObject.getPoint());
        setMessage(newObject.getMessage());
        setBottleType(newObject.getBottleType());
        setUser(newObject.getUser());
        setComments(newObject.getComments());
    }

}
