package com.yarmatey.messageinabottle;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.yarmatey.messageinabottle.bottles.AvailableBottle;
import com.yarmatey.messageinabottle.bottles.PickedUpBottle;
import com.yarmatey.messageinabottle.bottles.PirateMast;

//Created by Jason on 10/28/2015.

public class MessageInABottle extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
       // ParseObject.registerSubclass(PickedUpBottle.class);
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        ParseObject.registerSubclass(PickedUpBottle.class);
        ParseObject.registerSubclass(AvailableBottle.class);
        ParseObject.registerSubclass(PirateMast.class);
        Parse.initialize(this, "OZBa3WcZ3gwkZYGUabDMjyt9Kq3YBWY3cfoDLPnH", "IUJSIrtz1JamTw41lyBW1SDp8rWzzg04j7jV3a95");
    }

}
