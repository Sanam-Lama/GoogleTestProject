package com.example.googlemapstestproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.googlemapstestproject.R.drawable.google_icon;
import java.math.BigDecimal;

public class ClusterRenderer extends ClusterActivity implements GoogleMap.OnInfoWindowClickListener {

    private ClusterManager<MyItem> clusterManager;
    private GoogleMap googleMap;
   // private JSONArray jsonArray;
    private JSONObject jsonObject;


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private class PersonRenderer extends DefaultClusterRenderer<MyItem> {
        public PersonRenderer(Context context, GoogleMap googleMap, ClusterManager clusterManager) {
        super(context, googleMap, clusterManager);

        }
    }

    @Override
    protected void startDemo(GoogleMap g) {

//        Log.e("Print","Sanam guuu");
        googleMap = g;
        clusterManager = new ClusterManager<>(this, googleMap);

        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.8688197, 151.2092955), 15));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<MyItem>(this, googleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                Log.e("Marker",marker.getTitle().toString());
                Intent intent = new Intent(getBaseContext(), InfoActivity.class);

                intent.putExtra("nameObject", marker.getTitle());
                intent.putExtra("vicinityObject", marker.getSnippet());

                LatLng latlng = marker.getPosition();
//                Log.e("longitude", Double.toString(latlng.longitude));
//                Log.e("latitude", Double.toString(latlng.latitude));

                intent.putExtra("latObject", Double.toString(latlng.latitude));
                intent.putExtra("lngObject", Double.toString(latlng.longitude));

                startActivity(intent);
            }
        });

        // for new JSON file
        jsonObject = readAssets();

        // Add cluster items (markers) to the cluster manager.
         addItems();
    }

//    private void addItems() {
//
//
//        // Set some lat/lng coordinates to start with.
//
//        try {
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                double lat = jsonObject.getDouble("lat");
//                double lng = jsonObject.getDouble("lng");
//                LatLng latLng = new LatLng(lat, lng);
////                Log.e("Latitude",Double.toString(lat));
//              //  MyItem offsetItem = new MyItem(lat, lng);
//
//                String title = "Location Name";
//
//                String snippet = "Snippet";
//
//                MyItem infoWindowItem = new MyItem(latLng, title, snippet);
//
//
//                // Add the cluster item (marker) to the cluster manager.
//                clusterManager.addItem(infoWindowItem);
//
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    // recreating addItems() for this new JSON file
    private void addItems() {

        try {

            JSONArray resultsArray = jsonObject.getJSONArray("results");
//            Log.e("REsults Array: ", resultsArray.toString());
//            itemCount = resultsArray.length();
            for (int i=0; i<resultsArray.length(); i++) {
                JSONObject resultsObject = resultsArray.getJSONObject(i);
                String name = resultsObject.getString("name");
//                Log.e("Name: ", name);

                String vicinity = resultsObject.getString("vicinity");
//                Log.e("Vicinity: ", vicinity);

                JSONObject geometryObject = resultsObject.getJSONObject("geometry");
//                Log.e("Geometry: ", geometryObject.toString());

                JSONObject locationObject = geometryObject.getJSONObject("location");
//                Log.e("Location: ", locationObject.toString());

                Double lat = Double.parseDouble(locationObject.getString("lat"));
                Double lng = Double.parseDouble(locationObject.getString("lng"));

//                Log.e("Lat", lat.toString());
                LatLng latLng = new LatLng(lat, lng);

//                Log.e("latlng", latLng.toString());

                // why isnot latlng being printed in the infoWIndow??
                MyItem infoWindowItem = new MyItem(latLng, name, vicinity);
                clusterManager.addItem(infoWindowItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Add item",itemCount.toString());
//        return itemCount;
    }

    // for new JSON file



    // reading the data from the asset JSON file
//    private JSONArray readAssets() {
////        Log.e("Print","Sanam GUUU");
//        try{
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("JSONFile")));
//            String line = "";
//            StringBuilder stringBuilder = new StringBuilder("");
//            while ((line = bufferedReader.readLine()) != null){
//                stringBuilder.append(line);
//            }
//
//            return new JSONArray(stringBuilder.toString());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new JSONArray();
//    }

}
