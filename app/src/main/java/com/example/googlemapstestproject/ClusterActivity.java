package com.example.googlemapstestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class ClusterActivity extends AppCompatActivity implements OnMapReadyCallback {

//    private ClusterManager<MyItem> clusterManager;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        initMap();

    }

    // initializing the map
    private void initMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        Log.e("Print", "GOOGLE MAP ON READY");

        startDemo(googleMap);

    }

    protected abstract void startDemo(GoogleMap googleMap);

}
