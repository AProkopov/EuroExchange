package com.bugsnguns.revolutfx;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ExchangeActivity extends AppCompatActivity {


    public static CurrencyDBHelper dbHelper;
    private Handler handler;
    private int delay = 7000;
    public static DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create CurrencyDBHelper object
        dbHelper = new CurrencyDBHelper(this);
        Log.v("DBTag", "DB created");
        dbHelper.createCursor();

        //create DataHandler object
        dataHandler = new DataHandler();

        //get JSON and parse for the first time
        new ParseTask().execute();

        //Activity and UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        //making delay between JSON requests
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                //get JSON and parse
                new ParseTask().execute();
                //
                dbHelper.onWrite();
                //
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected void onDestroy() {
        //save last currency rates in DB
        dbHelper.onWrite();
        super.onDestroy();
    }
}
