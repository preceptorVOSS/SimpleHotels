package com.example.SimpleHotels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by veritoff on 3/16/14.
 */
public class MapView extends Activity {

    TextView name;
    TextView rating;
    TextView price;
    TextView address;

    Hotel hotel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        Intent intent = getIntent();
        hotel = (Hotel) intent.getParcelableExtra("HOTEL");

        name = (TextView) findViewById(R.id.name);
        rating = (TextView) findViewById(R.id.rating);
        price = (TextView) findViewById(R.id.price);
        address = (TextView) findViewById(R.id.address);

        name.setText(hotel.getName());
        rating.setText("Rating: " + Integer.toString(hotel.getRating()));
        price.setText(hotel.getPrice());
        address.setText(hotel.getAddress());
    }
}