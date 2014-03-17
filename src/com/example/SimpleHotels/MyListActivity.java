package com.example.SimpleHotels;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veritoff on 3/11/14.
 */
public class MyListActivity extends ListActivity implements OnCheckedChangeListener {

    private static String urlMN = "http://www.mobiata.com/testfiles/minneapolis-short.json";
    private static String urlSF = "http://www.mobiata.com/testfiles/sanfran-short.json";

    private static final String TAG_BODY = "body";
    private static final String TAG_RESPONSE = "HotelListResponse";
    private static final String TAG_LIST = "HotelList";
    private static final String TAG_SUMMARY = "HotelSummary";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "averageRate";
    private static final String TAG_CURRENCY = "rateCurrencyCode";
    private static final String TAG_RATING = "hotelRating";
    private static final String TAG_ADDRESS = "address1";
    private static final String TAG_CITY = "city";
    private static final String TAG_STATE = "stateProvinceCode";
    private static final String TAG_POSTAL_CODE = "postalCode";
    private static final String TAG_MEDIA = "media";
    private static final String TAG_IMAGE = "url";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final int MN = 0;
    private static final int SF = 1;

    JSONArray hotels = null;

    private ProgressDialog pDialog;
    ToggleButton button;

    MyArrayAdapter adapterMN;
    MyArrayAdapter adapterSF;

    List<Hotel> hotelListMN;
    List<Hotel> hotelListSF;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        button = (ToggleButton) findViewById(R.id.toggle);
        button.setOnCheckedChangeListener(this);
        button.setChecked(false);

        HotelDownloaderTask task = new HotelDownloaderTask();
        task.execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Hotel hotel = (Hotel) getListAdapter().getItem(position);
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra("HOTEL", hotel);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setListAdapter(adapterSF);
        } else {
            setListAdapter(adapterMN);
        }
    }

    public class HotelDownloaderTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyListActivity.this);
            pDialog.setMessage("Retrieving data...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            hotels = getHotelList(urlMN);
            if(hotels == null) {
                // Error
            }
            hotelListMN = convertToList(hotels);
            hotels = getHotelList(urlSF);
            if(hotels == null) {
                // Error
            }
            hotelListSF = convertToList(hotels);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(pDialog.isShowing()) pDialog.dismiss();

            adapterMN = new MyArrayAdapter(MyListActivity.this, hotelListMN);
            adapterSF = new MyArrayAdapter(MyListActivity.this, hotelListSF);
            setListAdapter(adapterMN);
        }

    }

    public JSONArray getHotelList(String url) {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        try {
            HttpResponse httpResponse = client.execute(httpGet);
            final HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                InputStream inputStream = null;
                String result = null;
                try {
                    inputStream = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder stringBuilder = new StringBuilder();

                    String line = null;
                    while((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    result = stringBuilder.toString();
                    jsonObject = new JSONObject(result);
                } finally {
                    if(inputStream != null) inputStream.close();
                    httpEntity.consumeContent();
                }
            }

            jsonObject = jsonObject.getJSONObject(TAG_BODY);
            jsonObject = jsonObject.getJSONObject(TAG_RESPONSE);
            jsonObject = jsonObject.getJSONObject(TAG_LIST);
            jsonArray = jsonObject.getJSONArray(TAG_SUMMARY);

        } catch (Exception e) {
            httpGet.abort();
            Log.w("getHotelList", "Error getting data from " + url);
        }
        return jsonArray;
    }

    public List<Hotel> convertToList(JSONArray jsonArray) {

        List<Hotel> hotels = new ArrayList<Hotel>();
        JSONObject j;
        Hotel h;
        int rating;
        String name, price, address, imageUrl;
        double latitude, longitude;

        try {
            for(int i = 0; i < jsonArray.length(); i++) {
                j = jsonArray.getJSONObject(i);
                rating = j.getInt(TAG_RATING);
                name = j.getString(TAG_NAME);
                price = j.getString(TAG_PRICE) + " " + j.getString(TAG_CURRENCY);
                imageUrl = j.getJSONArray(TAG_MEDIA).getJSONObject(0).getString(TAG_IMAGE);
                address = j.getString(TAG_ADDRESS) + " " + j.getString(TAG_CITY) +
                        ", " + j.getString(TAG_STATE) + " " +
                        Integer.toString(j.getInt(TAG_POSTAL_CODE));
                latitude = j.getDouble(TAG_LATITUDE);
                longitude = j.getDouble(TAG_LONGITUDE);
                h = new Hotel(rating, name, price, imageUrl, address, latitude, longitude);
                hotels.add(h);
            }
        } catch (Exception e) {
            Log.w("convertToList", "Error with JSONObject");
        }

        return hotels;
    }
}