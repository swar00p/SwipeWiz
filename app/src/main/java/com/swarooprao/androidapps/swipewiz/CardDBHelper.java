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

    public CardDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SAVEDCARDS_TABLE_NAME + "(" +
                        SAVEDCARDS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        SAVEDCARDS_COLUMN_CARDNAME + " TEXT, " +
                        SAVEDCARDS_COLUMN_CARDNUMBER + " TEXT, " +
                        SAVEDCARDS_COLUMN_BILLINGDATE + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addCard (String cardName, String cardNumber, int billingDate) {
        boolean retVal = true;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues rowData = new ContentValues();
        rowData.put (SAVEDCARDS_COLUMN_CARDNAME, cardName);
        rowData.put (SAVEDCARDS_COLUMN_CARDNUMBER, cardNumber);
        rowData.put (SAVEDCARDS_COLUMN_BILLINGDATE, billingDate);
        db.insert(SAVEDCARDS_TABLE_NAME, null, rowData);

        return retVal;
    }

    public Cursor getCards () {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SAVEDCARDS_TABLE_NAME, null);
        return cursor;
    }
}
