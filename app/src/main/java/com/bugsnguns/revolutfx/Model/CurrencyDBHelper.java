package com.bugsnguns.revolutfx.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bugsnguns.revolutfx.View.ExchangeActivity;

/**
 * Created by Antonio on 19.07.2017.
 */

//Storing last currencies data
public class CurrencyDBHelper extends SQLiteOpenHelper {

    public SQLiteDatabase db;
    public Cursor cursor;
    private static String LOG_TAG = "CurrencyDBHelper_LOG";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LastCurrency.dbHelper";

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

    //writing data in DB
    public void onWrite() {
        Log.v(LOG_TAG, "Going to clear and recreate DB");
        db.execSQL("DROP TABLE IF EXISTS " + "CURRENCY");
        onCreate(db);
        Log.v(LOG_TAG, "DB cleared and recreated successfully");

        //writing lastRateUSD to DB
        ContentValues usdValues = new ContentValues();
        usdValues.put("NAME", "USD");
        usdValues.put("RATE", ExchangeActivity.dataHandler.getLastRateUSD());
        db.insert("CURRENCY", null, usdValues);
        Log.v(LOG_TAG, "USD rate saved to DB successfully");

        //writing lastRateGBP to DB
        ContentValues gbpValues = new ContentValues();
        gbpValues.put("NAME", "GBP");
        gbpValues.put("RATE", ExchangeActivity.dataHandler.getLastRateGBP());
        db.insert("CURRENCY", null, gbpValues);
        Log.v(LOG_TAG, "GBP rate saved to DB successfully");

    }

    //creating Cursor to get data from DB
    public void createCursor() {
        db = this.getWritableDatabase();
        cursor = db.query("CURRENCY",
                new String[]{"RATE"},
                null, null, null, null, null);
        Log.v(LOG_TAG, "rows count in table CURRENCY " + cursor.getCount());
    }
}
