package com.bugsnguns.revolutfx;

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

public class ExchangeActivity extends AppCompatActivity {


    public static CurrencyDBHelper dbHelper;
    private Handler handler;
    private int delay = 30000;
    public static DataHandler dataHandler;
    public Spinner currencySpinnerFrom;
    public Spinner currencySpinnerTo;
    public EditText editText;
    public TextView resultView;

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
        resultViewBulder();
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

    protected void resultViewBulder() {
        resultView = (TextView) findViewById(R.id.resultView);
    }

    protected void editTextBuilder() {
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateResultView();
            }
        });
    }

    //method for update resultView in case of any changes in Spinners or editText
    //checking that editText is not empty
    protected void updateResultView () {
        if (!editText.getText().toString().matches("")) {
            resultView.setText(dataHandler.onConvert(Double.parseDouble(editText.getText()
                    .toString())));
        }
    }


    //метод spinnerBuilder() заполняет раскрывающиеся списки значениями
    protected void spinnerBuilder() {

        //заполнение Spinner названиями валют

        currencySpinnerFrom = (Spinner) findViewById(R.id.currencySpinnerFrom);
        currencySpinnerTo = (Spinner) findViewById(R.id.currencySpinnerTo);

        // Настраиваем адаптеры
        ArrayAdapter<?> adapterFrom =
                ArrayAdapter.createFromResource(this, R.array.currency_names,
                        android.R.layout.select_dialog_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapterTo =
                ArrayAdapter.createFromResource(this, R.array.currency_names,
                        android.R.layout.select_dialog_item);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Вызываем адаптеры
        currencySpinnerFrom.setAdapter(adapterFrom);
        currencySpinnerTo.setAdapter(adapterTo);

        //обработчик выбора элемента Spinner (From)
        currencySpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenStr = currencySpinnerFrom.getSelectedItem().toString();

                switch (chosenStr) {
                    case "EUR":
                        dataHandler.rateFrom = 1.0;
                        break;
                    case "USD":
                        dataHandler.rateFrom = dataHandler.lastRateUSD;
                        break;
                    case "GBP":
                        dataHandler.rateFrom = dataHandler.lastRateGBP;
                        break;
                }

                updateResultView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //обработчик выбора элемента Spinner (To)
        currencySpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenStr = currencySpinnerTo.getSelectedItem().toString();

                switch (chosenStr) {
                    case "EUR":
                        dataHandler.rateTo = 1.0;
                        break;
                    case "USD":
                        dataHandler.rateTo = dataHandler.lastRateUSD;
                        break;
                    case "GBP":
                        dataHandler.rateTo = dataHandler.lastRateGBP;
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
