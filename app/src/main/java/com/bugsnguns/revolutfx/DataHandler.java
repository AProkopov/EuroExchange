package com.bugsnguns.revolutfx;


import android.util.Log;

/**
 * Created by Antonio on 19.07.2017.
 */

// handling data (currencies from JSON, SQLite)
public class DataHandler {

    public static String LOG_TAG = "DataHandler_LOG";
    public volatile double lastRateUSD;
    public volatile double lastRateGBP;
    public CurrencyDBHelper db;

    public DataHandler () {

        //set values lastRateUSD and getLastRateGBP from DB
        Log.v(LOG_TAG, "DataHandler constructor called");
        db = ExchangeActivity.dbHelper;
        db.createCursor();
        if(db.cursor.getCount() != 0) {
            db.cursor.moveToLast();
            lastRateGBP = db.cursor.getDouble(0);
            Log.v(LOG_TAG, "saved rate of lastRateGBP is " + lastRateGBP);
            db.cursor.moveToPrevious();
            lastRateUSD = db.cursor.getDouble(0);
            Log.v(LOG_TAG, "saved rate of lastRateUSD is " + lastRateUSD);
        }

    }

    //method checks for currency rates changes and update lastValueUSD and lastValueGBP
    public void checkCurrencyUpdate (double newRateUSD, double newRateGBP) {
        Log.v(LOG_TAG, "lastRateUSD is " + lastRateUSD);
        Log.v(LOG_TAG, "newRateUSD is " + newRateUSD);
        if(lastRateUSD != newRateUSD) {
            lastRateUSD = newRateUSD;
            Log.v(LOG_TAG, "lastRateUSD is updated");
        }

        Log.v(LOG_TAG, "lastRateGBP is " + lastRateGBP);
        Log.v(LOG_TAG, "newRateGBP is " + newRateGBP);
        if(lastRateGBP != newRateGBP) {
            lastRateGBP = newRateGBP;
            Log.v(LOG_TAG, "lastRateGBP is updated");
        }

    }
}
