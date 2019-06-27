package com.example.googlemapstestproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;

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

public class ClusterRenderer extends ClusterActivity implements ClusterManager.OnClusterItemClickListener<MyItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MyItem>, GoogleMap.OnInfoWindowClickListener {

    private ClusterManager<MyItem> clusterManager;
    private GoogleMap googleMap;
    private JSONArray jsonArray;



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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8992389, 35.4811701), 15));

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


       // jsonArray = readAssets();

        new TestAsync().execute();

        // Add cluster items (markers) to the cluster manager.
        addItems();




       // clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems(inflater));
    }

    private void addItems() {


        // Set some lat/lng coordinates to start with.

        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double lat = jsonObject.getDouble("lat");
                double lng = jsonObject.getDouble("lng");
                LatLng latLng = new LatLng(lat, lng);
//                Log.e("Latitude",Double.toString(lat));
                MyItem offsetItem = new MyItem(lat, lng);

                String title = "Location Name";

                String snippet = "Snippet";

                MyItem infoWindowItem = new MyItem(latLng, title, snippet);


                // Add the cluster item (marker) to the cluster manager.
                clusterManager.addItem(infoWindowItem);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    // reading the data
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


    public class TestAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // using StringBuffer for immutability
            StringBuffer response = null;

            // Step 1: create a URL object
            URL url = null;

            try {
                url = new URL("https://maps.googleapis.com/maps/api/json");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {

                // Step 2: HttpURLConnection is an android provided library
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                //  BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine;
                response = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
