package com.yarmatey.messageinabottle.sql;

import com.google.android.gms.maps.model.LatLng;
import com.yarmatey.messageinabottle.bottles.Bottle;
import com.yarmatey.messageinabottle.sql.BottleContract.BottleEntry;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Created by jason on 2/16/16.
 */
public class BottleDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bottles.db";

    private static final int DATABASE_VERSION = 2;


    public BottleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ALARM_TABLE = "CREATE TABLE " + BottleEntry.TABLE_NAME + " (" +
                BottleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BottleEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                BottleEntry.COLUMN_DATE + " INTEGER NOT NULL," +
                BottleEntry.COLUMN_CREATED + " INTEGER NOT NULL, " +
                BottleEntry.COLUMN_MESSAGE + " TEXT NOT NULL, " +
                BottleEntry.COLUMN_LASTUSER + " TEXT NOT NULL, " +
                BottleEntry.COLUMN_LATITUDE + " REAL NOT NULL," +
                BottleEntry.COLUMN_LONGITUDE + " REAL NOT NULL," +
                BottleEntry.COLUMN_RATING + " INTEGER NOT NULL," +
                BottleEntry.COLUMN_STATUS + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_ALARM_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BottleEntry.TABLE_NAME);
    }


    public static String getBottleMessage(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(BottleEntry.COLUMN_MESSAGE));
    }

    public static long getBottleDate(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(BottleEntry.COLUMN_DATE));
    }

    public static Location getBottleLocation (Cursor cursor) {
        double latitude = cursor.getDouble(cursor.getColumnIndex(BottleEntry.COLUMN_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(BottleEntry.COLUMN_LONGITUDE));
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public static int getBottleID(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(BottleEntry._ID));
    }
}
