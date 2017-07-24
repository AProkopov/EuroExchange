package com.bugsnguns.revolutfx.View;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bugsnguns.revolutfx.Model.CurrencyDBHelper;
import com.bugsnguns.revolutfx.Presenter.DataHandler;
import com.bugsnguns.revolutfx.Presenter.ParseTask;
import com.bugsnguns.revolutfx.R;

public class ExchangeActivity extends AppCompatActivity {


    public static CurrencyDBHelper dbHelper;
    public static DataHandler dataHandler;
    private Spinner currencySpinnerFrom;
    private Spinner currencySpinnerTo;
    private EditText editText;
    private TextView resultView;
    private Handler handler;
    private int delay = 30000;

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
        resultViewBuilder();
        editTextBuilder();
        spinnerBuilder();

        //making delay between JSON requests
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //get JSON and parse
                new ParseTask().execute();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected void onStop() {
        //save last currency rates in DB
        dbHelper.onWrite();
        super.onStop();
    }

    protected void resultViewBuilder() {
        resultView = (TextView) findViewById(R.id.resultView);
    }

    protected void editTextBuilder() {
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateResultView();
            }
        });
    }

    //method for update resultView in case of any changes in Spinners or editText
    //checking that editText is not empty
    //if editText is empty -> result is zero
    protected void updateResultView() {
        if (!editText.getText().toString().matches("")) {
            resultView.setText(dataHandler.onConvert(Double.parseDouble(editText.getText()
                    .toString())));
        } else {
            resultView.setText("0");
        }
    }


    //spinnerBuilder() fills spinners by values
    protected void spinnerBuilder() {
        currencySpinnerFrom = (Spinner) findViewById(R.id.currencySpinnerFrom);
        currencySpinnerTo = (Spinner) findViewById(R.id.currencySpinnerTo);

        // setting adapters
        ArrayAdapter<?> adapterFrom =
                ArrayAdapter.createFromResource(this, R.array.currency_names,
                        android.R.layout.select_dialog_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapterTo =
                ArrayAdapter.createFromResource(this, R.array.currency_names,
                        android.R.layout.select_dialog_item);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currencySpinnerFrom.setAdapter(adapterFrom);
        currencySpinnerTo.setAdapter(adapterTo);

        //selection handler for Spinner (From)
        currencySpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenStr = currencySpinnerFrom.getSelectedItem().toString();

                switch (chosenStr) {
                    case "EUR":
                        dataHandler.setRateFrom(1.0);
                        break;
                    case "USD":
                        dataHandler.setRateFrom(dataHandler.getLastRateUSD());
                        break;
                    case "GBP":
                        dataHandler.setRateFrom(dataHandler.getLastRateGBP());
                        break;
                }

                updateResultView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //selection handler for Spinner (To)
        currencySpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenStr = currencySpinnerTo.getSelectedItem().toString();

                switch (chosenStr) {
                    case "EUR":
                        dataHandler.setRateTo(1.0);
                        break;
                    case "USD":
                        dataHandler.setRateTo(dataHandler.getLastRateUSD());
                        break;
                    case "GBP":
                        dataHandler.setRateTo(dataHandler.getLastRateGBP());
                        break;
                }

                updateResultView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
