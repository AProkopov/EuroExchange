package com.bugsnguns.revolutfx;

import android.util.Log;
import java.math.BigDecimal;

/**
 * Created by Antonio on 19.07.2017.
 */

// handling data (currencies from JSON, SQLite)
public class DataHandler {

    public CurrencyDBHelper db;
    private static String LOG_TAG = "DataHandler_LOG";
    private volatile double lastRateUSD;
    private volatile double lastRateGBP;

    //rateFrom and rateTo uses handle rates of currencies that chosen in ExchangeActivity UI
    private double rateFrom;
    private double rateTo;

    public DataHandler() {
        //set values lastRateUSD and getLastRateGBP from DB
        Log.v(LOG_TAG, "DataHandler constructor called");
        db = ExchangeActivity.dbHelper;
        db.createCursor();
        if (db.cursor.getCount() != 0) {
            db.cursor.moveToLast();
            lastRateGBP = db.cursor.getDouble(0);
            Log.v(LOG_TAG, "saved rate of lastRateGBP is " + lastRateGBP);
            db.cursor.moveToPrevious();
            lastRateUSD = db.cursor.getDouble(0);
            Log.v(LOG_TAG, "saved rate of lastRateUSD is " + lastRateUSD);
        }

    }

    //getters and setters for private variables
    public double getLastRateUSD() {
        return lastRateUSD;
    }

    public double getLastRateGBP() {
        return lastRateGBP;
    }

    public void setRateFrom(double rateFrom) {
        this.rateFrom = rateFrom;
    }

    public void setRateTo(double rateTo) {
        this.rateTo = rateTo;
    }

    //method checks for currency rates changes and update lastValueUSD and lastValueGBP
    public void checkCurrencyUpdate(double newRateUSD, double newRateGBP) {
        Log.v(LOG_TAG, "lastRateUSD is " + lastRateUSD);
        Log.v(LOG_TAG, "newRateUSD is " + newRateUSD);
        if (lastRateUSD != newRateUSD) {
            lastRateUSD = newRateUSD;
            Log.v(LOG_TAG, "lastRateUSD is updated");
        }

        Log.v(LOG_TAG, "lastRateGBP is " + lastRateGBP);
        Log.v(LOG_TAG, "newRateGBP is " + newRateGBP);
        if (lastRateGBP != newRateGBP) {
            lastRateGBP = newRateGBP;
            Log.v(LOG_TAG, "lastRateGBP is updated");
        }
    }

    //method on Convert: connecting data between EditText and TextView in ExchangeActivity
    //if there is no rateFrom value (no internet connection while the first launch)
    public String onConvert(double value) {
        try {
            BigDecimal result = BigDecimal.valueOf((rateTo / rateFrom * value))
                    .setScale(2, BigDecimal.ROUND_UP);
            return result.toString();
        } catch (ArithmeticException e) {
            return BigDecimal.valueOf((0.00)).setScale(2, BigDecimal.ROUND_UP).toString();
        }

    }
}
