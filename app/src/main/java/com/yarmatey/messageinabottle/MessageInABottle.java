package com.yarmatey.messageinabottle;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Boris on 11/22/2015.
 */
public class MessageInABottle extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

       Parse.enableLocalDatastore(this);
        Parse.initialize(getApplicationContext(), "OZBa3WcZ3gwkZYGUabDMjyt9Kq3YBWY3cfoDLPnH", "IUJSIrtz1JamTw41lyBW1SDp8rWzzg04j7jV3a95");
    }
}
