package com.example.zghadyali.googlegram;

import android.provider.BaseColumns;

/**
 * FeedReaderContract sets up the SQLite database to store the links for the images the user wants
 * to save to the feed. Specifies the table created in the database and creates the SQL string for
 * adding entries and deleting entries
 */
public class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String ITEM_NAME = "link";
        public static final String TABLE_NAME = "LINK_LIST";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.ITEM_NAME + TEXT_TYPE +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

}
