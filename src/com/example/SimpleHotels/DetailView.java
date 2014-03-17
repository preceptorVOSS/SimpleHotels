package com.example.SimpleHotels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

/**
 * Created by veritoff on 3/15/14.
 */
public class DetailView extends Activity {

    TextView name;
    TextView price;
    TextView rating;
    ImageView image;

    String imageUrl;
    Hotel hotel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        Intent intent = getIntent();
        hotel = (Hotel) intent.getParcelableExtra("HOTEL");

        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        rating = (TextView) findViewById(R.id.rating);
        image = (ImageView) findViewById(R.id.image);

        name.setText(hotel.getName());
        price.setText(hotel.getPrice());
        rating.setText("Rating: " + Integer.toString(hotel.getRating()));
        imageUrl = hotel.getImageUrl();

        BitmapDownloaderTask task = new BitmapDownloaderTask();
        task.execute();
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapView.class);
        intent.putExtra("HOTEL", hotel);
        startActivity(intent);
    }

    public class BitmapDownloaderTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            return getBitmap(imageUrl);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        }
    }

    public Bitmap getBitmap(String url) {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url);
        Bitmap bitmap = null;

        try {
            HttpResponse httpResponse = client.execute(httpGet);
            final HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = httpEntity.getContent();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } finally {
                    if (inputStream != null) inputStream.close();
                    httpEntity.consumeContent();
                }
            }
        } catch (Exception e) {
            httpGet.abort();
            Log.w("getBitmap", "Error getting image from " + url);
        }

        return bitmap;
    }
}