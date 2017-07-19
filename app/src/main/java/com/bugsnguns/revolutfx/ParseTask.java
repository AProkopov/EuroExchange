package com.bugsnguns.revolutfx;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Antonio on 18.07.2017.
 */

public class ParseTask extends AsyncTask<Void, Void, String> {

    public static String LOG_TAG = "my_log";
    private static final String TAG_USD = "USD";
    private static final String TAG_GBP = "GBP";
    private static final String TAG_RATES = "rates";


    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;
    private String resultJson = "";

    @Override
    protected String doInBackground(Void... params) {

        //getting data from remote resource
        try {
            URL url = new URL("http://api.fixer.io/latest?symbols=USD,GBP");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            inputStream.close();

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);
        // выводим целиком полученную json-строку
        Log.d(LOG_TAG, strJson);

        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(strJson);
            JSONObject ratesJSON = dataJsonObj.getJSONObject(TAG_RATES);
            double valueUSD = ratesJSON.getDouble(TAG_USD);
            double valueGBP = ratesJSON.getDouble(TAG_GBP);
            Log.d(LOG_TAG, "valueUSD is " + valueUSD);
            Log.d(LOG_TAG, "valueGBP is " + valueGBP);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
