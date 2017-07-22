package com.bugsnguns.revolutfx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Antonio on 19.07.2017.
 */

//Storing last currencies data
public class CurrencyDBHelper extends SQLiteOpenHelper {

    public static String LOG_TAG = "CurrencyDBHelper_LOG";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LastCurrency.dbHelper";
    public SQLiteDatabase db;
    public Cursor cursor;
    public DataHandler dataHandler = ExchangeActivity.dataHandler;

    //constructor
    public CurrencyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CURRENCY ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "RATE REAL); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onWrite() {
        Log.v(LOG_TAG, "Going to clear and recreate DB");
        db.execSQL("DROP TABLE IF EXISTS "+ "CURRENCY");
        onCreate(db);
        Log.v(LOG_TAG, "DB cleared and recreated successfully");

        /*
        //adding lastRateUSD to DB
        ContentValues usdValues = new ContentValues();
        usdValues.put("NAME", "USD");
        usdValues.put("RATE", dataHandler.lastRateUSD);
        db.insert("CURRENCY", null, usdValues);
        Log.v(LOG_TAG, "USD rate saved to DB successfully");

        //adding lastRateGBP to DB
        ContentValues gbpValues = new ContentValues();
        usdValues.put("NAME", "GBP");
        usdValues.put("RATE", dataHandler.lastRateGBP);
        db.insert("CURRENCY", null, gbpValues);
        Log.v(LOG_TAG, "GBP rate saved to DB successfully");
        */
    }

    public void createCursor() {
        db = this.getWritableDatabase();
        cursor = db.query("CURRENCY",
                new String[] {"RATE"},
                null, null, null, null, null);
        Log.v(LOG_TAG, "rows count in table CURRENCY " + cursor.getCount());
    }


}
