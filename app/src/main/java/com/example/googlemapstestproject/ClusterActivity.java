package com.example.googlemapstestproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

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

public abstract class ClusterActivity extends AppCompatActivity implements OnMapReadyCallback {

//    private ClusterManager<MyItem> clusterManager;
    private GoogleMap googleMap;
    FloatingActionButton googleFAB;
    FloatingActionButton launcherFAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        initMap();

        FloatingActionButton floating_button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        googleFAB = (FloatingActionButton) findViewById(R.id.googleFAB);
        launcherFAB = (FloatingActionButton) findViewById(R.id.launcherFAB);

        floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ClusterActivity.this, "Floating Action Button Clicked", Toast.LENGTH_SHORT).show();

                if (!isFABOpen) {
                    showFABMenu();
                }
                else {
                    closeFABMenu();
                }
            }
        });
//        new TestAsync().execute();
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

    boolean isFABOpen;
    private void showFABMenu() {
        isFABOpen = true;
        googleFAB.animate().translationX(-getResources().getDimension(R.dimen.standard_55));
        launcherFAB.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        googleFAB.animate().translationX(0);
        launcherFAB.animate().translationY(0);
    }

//    public class TestAsync extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            // using StringBuffer for immutability
//            StringBuffer response = null;
//
//            // Step 1: create a URL object
//            URL url = null;
//
//            try {
//                // url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyD0UD9bfUS71Go5KZmbn8esePRit1VPXvg");
//
////                url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyBMzzVMfEEbuQZAdaSrEDAcZiFpBWWSXB4");
//                    url = new URL("https://maps.googleapis.com/maps/api/place/search/json?radius=1500&type=restaurant&sensor=false&key=AIzaSyDBxsLNgYurpySX9l34dl6Hw25TySM-bmE&location=-33.8670522,151.1957362");
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                // Step 2: HttpURLConnection is an android provided library
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                InputStream in = new BufferedInputStream(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
////                Log.e("Print",reader.toString());
//                String inputLine;
//                response = new StringBuffer();
//                while ((inputLine = reader.readLine()) != null) {
//                    response.append(inputLine);
//                    Log.e("Print",inputLine.toString());
//                }
//
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                JSONObject object = new JSONObject(response.toString());
//
//                JSONArray resultArray = object.getJSONArray("results");
//                Log.e("result", resultArray.toString());
//
//                if (resultArray.length() == 0) {
//                    // no results
//                }
//                else {
//                    int length = resultArray.length();
//                    for (int i=0; i<length; i++) {
//                        Toast.makeText(getApplicationContext(), resultArray.getJSONObject(i).getString("name") , Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }


}
