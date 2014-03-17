package com.example.SimpleHotels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by veritoff on 3/11/14.
 */
public class Hotel implements Parcelable {

    private int rating;
    private String name;
    private String price;
    private String imageUrl;
    private String address;
    private double latitude;
    private double longitude;

    public Hotel(int rating, String name, String price, String imageUrl,
                 String address, double latitude, double longitude) {
        this.rating = rating;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Hotel(Parcel in) {
        String[] data = new String[4];
        double[] latLong = new double[2];
        in.readStringArray(data);
        in.readDoubleArray(latLong);
        this.rating = in.readInt();
        this.name = data[0];
        this.price = data[1];
        this.imageUrl = data[2];
        this.address = data[3];
        this.latitude = latLong[0];
        this.longitude = latLong[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[] {this.name,
            this.price, this.imageUrl, this.address});
        out.writeDoubleArray(new double[] {this.latitude,
                this.longitude});
        out.writeInt(rating);
    }

    public static final Creator<Hotel> CREATOR = new Parcelable.Creator<Hotel>() {
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
