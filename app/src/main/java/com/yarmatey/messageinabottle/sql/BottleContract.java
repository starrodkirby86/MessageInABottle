package com.yarmatey.messageinabottle.sql;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.yarmatey.messageinabottle.bottles.BottleAttribute;

/**
 * Created by jason on 2/16/16.
 */
public class BottleContract {

    //The path to the content provider
    public static final String CONTENT_AUTHORITY = "com.yarmatey.messageinabottle.sql.provider";

    //Where the content will be
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOTTLE = "bottle";

    public static final class BottleEntry implements BaseColumns {
        //The content URI for every BottleEntry
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOTTLE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_BOTTLE;

        public static final String TABLE_NAME = "bottles";

        public static final String COLUMN_MESSAGE = BottleAttribute.Message.value;

        public static final String COLUMN_AUTHOR = BottleAttribute.Author.value;

        public static final String COLUMN_RATING = BottleAttribute.Ratings.value;

        public static final String COLUMN_STATUS = BottleAttribute.Status.value;

        public static final String COLUMN_LASTUSER = BottleAttribute.LastUser.value;

        public static final String COLUMN_LATITUDE = BottleAttribute.Latitude.value;

        public static final String COLUMN_LONGITUDE = BottleAttribute.Longitude.value;

        public static final String COLUMN_DATE = BottleAttribute.Date.value;

        public static final String COLUMN_CREATED = BottleAttribute.Created.value;

        public static Uri buildBottleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
