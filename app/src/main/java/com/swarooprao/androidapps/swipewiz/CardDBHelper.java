package com.swarooprao.androidapps.swipewiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Swaroop on 11/7/2015.
 */
public class CardDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SwipeWiz.db";
    private static final int DATABASE_VERSION = 1;
    public static final String SAVEDCARDS_TABLE_NAME = "savedcards";
    public static final String SAVEDCARDS_COLUMN_CARDNAME = "cardname";
    public static final String SAVEDCARDS_COLUMN_CARDNUMBER = "cardnumber";
    public static final String SAVEDCARDS_COLUMN_ID = "_id";
    public static final String SAVEDCARDS_COLUMN_BILLINGDATE = "billdate";

    public CardDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SAVEDCARDS_TABLE_NAME + "(" +
                        SAVEDCARDS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        SAVEDCARDS_COLUMN_CARDNAME + " TEXT, " +
                        SAVEDCARDS_COLUMN_CARDNUMBER + " TEXT, " +
                        SAVEDCARDS_COLUMN_BILLINGDATE + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addCard (String cardName, String cardNumber, int billingDate) {
        long retVal = 0;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues rowData = new ContentValues();
        rowData.put (SAVEDCARDS_COLUMN_CARDNAME, cardName);
        rowData.put (SAVEDCARDS_COLUMN_CARDNUMBER, cardNumber);
        rowData.put (SAVEDCARDS_COLUMN_BILLINGDATE, billingDate);
        retVal = db.insert(SAVEDCARDS_TABLE_NAME, null, rowData);

        return retVal;
    }

    public Cursor getCards () {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SAVEDCARDS_TABLE_NAME, null);
        return cursor;
    }

    public void deleteCard (long rowId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SAVEDCARDS_TABLE_NAME, "_id = " + rowId, null);
    }
}
