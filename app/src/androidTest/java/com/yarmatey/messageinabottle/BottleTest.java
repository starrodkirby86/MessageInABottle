package com.yarmatey.messageinabottle;

import android.database.Cursor;
import android.location.Location;
import android.test.AndroidTestCase;

import com.yarmatey.messageinabottle.bottles.Bottle;
import com.yarmatey.messageinabottle.bottles.enums.MessageStatus;
import com.yarmatey.messageinabottle.sql.BottleContract;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Jason on 2/22/2016.
 */
public class BottleTest extends AndroidTestCase {


    Bottle testBottle;

    public void setUp() throws Exception {
        testBottle = new Bottle();
    }



    public void testVerifyBottleCreation() throws Exception {
        Assert.assertNotNull(testBottle);
    }

    /*
            TESTING SETTERS AND GETTERS
     */
    public void testBottleMessage() throws Exception {
        String testMessage = "A pirate's life for me";
        testBottle.setMessage(testMessage);
        assertEquals(testMessage, testBottle.getMessage());
    }

    public void testBottleAuthor() throws Exception {
        String testAuthor = "Blackbeard";
        testBottle.setAuthor(testAuthor);
        assertEquals(testAuthor, testBottle.getAuthor());
    }

    public void testBottleStatus() throws Exception {
        int testStatus = MessageStatus.Map.state;
        testBottle.setStatus(testStatus);
        assertEquals(testStatus, testBottle.getStatus());
    }

    public void testBottleLocation() throws Exception {
        Location testLocation = new Location("");
        testLocation.setLatitude(12);
        testLocation.setLongitude(12);
        testBottle.setLocation(testLocation);
        assertEquals(testLocation.getLatitude(), testBottle.getLocation().getLatitude());
        assertEquals(testLocation.getLongitude(), testBottle.getLocation().getLongitude());
    }

    public void testBottleCreatedDate() throws Exception {
        Calendar testTime = Calendar.getInstance();
        testTime.setTimeInMillis(1456187819132l);
        testBottle.setCreated(testTime);
        assertEquals(testTime, testBottle.getCreated());

        testTime.setTimeInMillis(1456183429233l);
        String stringTime = Bottle.DATE_STRING_FORMAT.format(testTime.getTime());
        testBottle.setCreated(stringTime);
        assertEquals(testTime, testBottle.getCreated());

    }

    public void testBottleLastUpdate() throws Exception {
        Calendar updatedTime = Calendar.getInstance();
        updatedTime.setTimeInMillis(1456187819233l);
        testBottle.setLastUpdated(updatedTime);
        assertEquals(updatedTime, testBottle.getLastUpdated());

        updatedTime.setTimeInMillis(1456183429233l);
        String stringTime = Bottle.DATE_STRING_FORMAT.format(updatedTime.getTime());
        testBottle.setLastUpdated(stringTime);
        assertEquals(updatedTime, testBottle.getLastUpdated());

    }

    public void testBottleRating() throws Exception {
        String ratings = "1 0 0 1";
        testBottle.setRatings(ratings);
        List<Integer> ratingList = new ArrayList<>();
        ratingList.add(1);
        ratingList.add(0);
        ratingList.add(0);
        ratingList.add(1);
        assertEquals(ratingList, testBottle.getRatings());
        ratingList.set(0, 2);
        ratingList.set(2, 3);
        assertNotSame(ratingList, testBottle.getRatings());
        testBottle.setRatings(ratingList);
        assertEquals(ratingList, testBottle.getRatings());
    }

    public void testReadyToInsert() throws Exception {
        assertEquals(testBottle.readyToInsert(), true);
    }

    public void testQueryToDb() throws Exception {
        Cursor cursor = getContext().getContentResolver().query(BottleContract.BottleEntry.CONTENT_URI, null, null, null, null);
        assertNotNull(cursor);
    }

    public void testInsertToDb() throws Exception {
        getContext().getContentResolver().insert(BottleContract.BottleEntry.CONTENT_URI, testBottle.getContentValues());
    }


    public void tearDown() throws Exception {
    }
}
