package com.example.zghadyali.googlegram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * FeedReaderDbHelper uses the FeedReaderContract to instantiate the database so that it is both
 * writable and readable and contains the addItemtoDB(), readItemsfromDB(), and deleteItemtoDB()
 * methods which are used in the searchGoogle and feed fragments
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private SQLiteDatabase Writable_DB;
    private SQLiteDatabase Readable_DB;

    //used to have a database that can both be written to and read from
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Writable_DB = getWritableDatabase();
        Readable_DB = getReadableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FeedReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //takes as input the link for the image that the user wants to save to the feed and adds it to
    //the database, doesn't return anything
    public void addItemtoDB(String user_item){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.ITEM_NAME, user_item);

        long newRowId;
        newRowId = Writable_DB.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values);
    }
    //takes no input and returns an ArrayList that contains the links for the images the user saved
    //to their feed
    public ArrayList<String> readItemsfromDB(){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        ArrayList<String> res = new ArrayList<String>();

        String[] projection = {
                com.example.zghadyali.googlegram.FeedReaderContract.FeedEntry._ID,
                com.example.zghadyali.googlegram.FeedReaderContract.FeedEntry.ITEM_NAME
        };

        Cursor c = Readable_DB.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if (c.moveToFirst()) {  //evaluates to true if cursor successfully moves to first entry
            while (c.moveToNext()) {    //evaluates to true if cursor successfully moves next
                res.add(c.getString(1));    //adds the link to the ArrayList
            }
        }
        return res;

    }
    //takes as input the link for the image that the user wants to delete from their feed and
    //deletes the link from the database
    public void deleteItemtoDB(String delete_item){
        Writable_DB.delete(FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.ITEM_NAME + "=?",
                new String[]{delete_item}
        );
    }
}