package com.yarmatey.messageinabottle;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.yarmatey.messageinabottle.sql.BottleContract;

import java.util.Map;
import java.util.Set;

/**
 * Created by jason on 2/18/16.
 */
public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = System.currentTimeMillis();

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    valueCursor.getString(idx) + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createBottleValues() {
        ContentValues bottleValues = new ContentValues();
        bottleValues.put(BottleContract.BottleEntry.COLUMN_AUTHOR, "Test Mcgee!");
        bottleValues.put(BottleContract.BottleEntry.COLUMN_CREATED, 1230);
        bottleValues.put(BottleContract.BottleEntry.COLUMN_DATE, 1);
        bottleValues.put(BottleContract.BottleEntry.COLUMN_MESSAGE, "Hello World!");
        bottleValues.put(BottleContract.BottleEntry.COLUMN_LASTUSER, "Jason");
        bottleValues.put(BottleContract.BottleEntry.COLUMN_LATITUDE, 1.021);
        bottleValues.put(BottleContract.BottleEntry.COLUMN_LONGITUDE, 0.1);
        bottleValues.put(BottleContract.BottleEntry.COLUMN_RATING, 1);
        bottleValues.put(BottleContract.BottleEntry.COLUMN_STATUS, 0);
        return bottleValues;
    }
    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
