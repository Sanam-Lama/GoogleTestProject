package com.example.googlemapstestproject;

import android.content.Context;
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

public class ClusterRenderer extends ClusterActivity implements ClusterManager.OnClusterItemClickListener<MyItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MyItem>, GoogleMap.OnInfoWindowClickListener {

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
    public boolean onClusterItemClick(MyItem item) {
        // Does nothing, but you could go into the user's profile page, for example.

        Log.e("Print","4 foot");
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {

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
        clusterManager.setOnClusterItemClickListener(this);
       // clusterManager.setOnClusterInfoWindowClickListener(this);


        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(this);

      //  googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());


//        jsonArray = readAssets();

        // for new JSON file
        jsonObject = readAssets();

        // Add cluster items (markers) to the cluster manager.
        addItems();


       // clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems(inflater));
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

                MyItem infoWindowItem = new MyItem(latLng, name, vicinity);

                clusterManager.addItem(infoWindowItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // for new JSON file
    private JSONObject readAssets() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("JSONFile")));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder("");
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }

            return new JSONObject(stringBuilder.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONObject();
    }


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

    // option menu: this needs to be at the bottom of the screen
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected item: " + item.getTitle(),Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {

            case R.id.google_icon:
                return true;

            case R.id.listview_icon:
                return true;

//            case item.setIcon(google_icon):
//                return true;
//
//            case item.setIcon(R.drawable.ic_launcher_foreground):
//                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
