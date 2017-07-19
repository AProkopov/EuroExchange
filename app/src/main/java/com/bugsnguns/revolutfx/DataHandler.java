package com.bugsnguns.revolutfx;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Antonio on 19.07.2017.
 */

// handling data (currencies from JSON, SQLite)
public class DataHandler {

    private Context context;
    public double lastRateUSD;
    public double getLastRateGBP;

    public DataHandler () {

        //lastRateUSD и getLastRateGBP присваиваем значения из БД
    }

    public void checkCurrencyUpdate (double lastRate, double newRate) {
        if (lastRate != newRate) {
            lastRate = newRate;
        }
    }
}
