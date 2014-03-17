package com.example.SimpleHotels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by veritoff on 3/11/14.
 */
public class MyArrayAdapter extends android.widget.ArrayAdapter<Hotel> {

    private final Context context;
    private final List<Hotel> hotels;

    public MyArrayAdapter(Context context, List<Hotel> hotels) {
        super(context, R.layout.row_layout, hotels);
        this.context = context;
        this.hotels = hotels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView ratingView = (TextView) rowView.findViewById(R.id.rating);
        TextView priceView = (TextView) rowView.findViewById(R.id.price);
        nameView.setText(hotels.get(position).getName());
        ratingView.setText("Rating: " + Integer.toString(hotels.get(position).getRating()));
        priceView.setText(hotels.get(position).getPrice());

        return rowView;
    }

    @Override
    public Hotel getItem(int position) {
        return hotels.get(position);
    }
}
