package com.bugsnguns.revolutfx;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ExchangeActivity extends AppCompatActivity {


    public CurrencyDB db;
    private Handler handler;
    private int delay = 3000;
    private DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create CurrencyDB object
        db = new CurrencyDB(this);
        Log.v("DBTag", "DB created");

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
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected void onDestroy() {
        //данные о последних курсах сохраняются в бд
        //код сохранения
        super.onDestroy();
    }
}
