package com.example.googlemapstestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    TextView location_name, location_geo, location_address;
    ImageView image_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Log.e("Info activity", "inside info activity");
        image_view = (ImageView)findViewById(R.id.imageView);
        location_name = (TextView)findViewById(R.id.locationName);
        location_geo = (TextView)findViewById(R.id.locationGeo);
        location_address = (TextView)findViewById(R.id.locationAddress);

        image_view.setImageResource(R.drawable.ic_cup);

        String name = getIntent().getStringExtra("nameObject");
        location_name.setText("Location Name: " + name);
//        Log.e("location name", "Name object");

        String vicinity = getIntent().getStringExtra("vicinityObject");
        location_address.setText("Vicinity: " + vicinity);

        String latObject = getIntent().getStringExtra("latObject");
        String lngObject = getIntent().getStringExtra("lngObject");
        location_geo.setText("Location: " + "latitude: {" + latObject + "}, " + "longitude: {" + lngObject + "}");

    }
}
