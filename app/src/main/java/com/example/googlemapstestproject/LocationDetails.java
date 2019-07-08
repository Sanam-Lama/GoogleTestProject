package com.example.googlemapstestproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class LocationDetails implements Parcelable {

    public static final Creator<LocationDetails> CREATOR = new Creator<LocationDetails>() {
        @Override
        public LocationDetails createFromParcel(Parcel source) {
            return new LocationDetails(source);
        }

        @Override
        public LocationDetails[] newArray(int size) {
            return new LocationDetails[0];
        }
    };

    private String name;
    private String vicinity;
//    private LatLng latLng;
    private double lat, lng;

//    public LocationDetails(String name, String vicinity, LatLng latLng) {
//        this.name = name;
//        this.vicinity = vicinity;
//        this.latLng = latLng;
//    }


    public LocationDetails(String name, String vicinity, double lat, double lng) {
        this.name = name;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

//    public LatLng getLatLng() {
//        return latLng;
//    }

//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LocationDetails(Parcel source) {
        name = source.readString();
        vicinity = source.readString();
        lat = source.readDouble();
        lng = source.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }
}
