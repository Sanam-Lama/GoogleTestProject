package com.example.googlemapstestproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.mancj.materialsearchbar.MaterialSearchBar;

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
import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

public abstract class ClusterActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    FloatingActionButton googleFAB;
    FloatingActionButton launcherFAB;
    Integer itemCount;

    // recyclerview
    ArrayList<LocationDetails> locationDetails = new ArrayList<>();

    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdpater recyclerViewAdpater;
    private JSONObject jsonObject;

    // search bar
    MaterialSearchBar search_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        /* Since initMap() is running asynchronously in the background, itemcount is not being populated
         * to the main thread, hence calling before initMap is called to have the count of JSON item  */
        jsonObject = readAssets();
        try {

            JSONArray resultsArray = jsonObject.getJSONArray("results");
            itemCount = resultsArray.length();
//            Log.e("Results Array", resultsArray.getJSONObject(0).toString());


            for (int i=0; i<resultsArray.length(); i++) {
                JSONObject resultsObject = resultsArray.getJSONObject(i);
//                JSONObject temp = resultsArray.getJSONObject(0);
                JSONObject geometry = resultsObject.getJSONObject("geometry");
//            Log.e("Geometry", geometry.toString());

                JSONObject location = geometry.getJSONObject("location");
//                Log.e("Location", location.toString());

                String lat = location.getString("lat");
                String lng = location.getString("lng");

                String name = resultsObject.getString("name");
                String vicinity = resultsObject.getString("vicinity");
//                Log.e("NAME", name);
//                Log.e("VICINITY", vicinity);

                // converting lat lng that are string to double because Latlng requires double values
//                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));


                // object of LocationDetails class
                LocationDetails jsonDetails = new LocationDetails(name, vicinity, Double.parseDouble(lat), Double.parseDouble(lng));
                // to store the array that we just read
                locationDetails.add(jsonDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //search bar
        search_bar = (MaterialSearchBar) findViewById(R.id.searchBar);
        search_bar.inflateMenu(R.menu.option_menu);
//        search_bar.setMenuIcon(R.drawable.cast_ic_notification_small_icon);

        initMap();

        // adding circle to the arraylist: recyclerview
        recycler_view = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setOrientation(HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
//        Log.e("Item Count3",itemCount.toString());

        recyclerViewAdpater = new RecyclerViewAdpater(getApplicationContext());
        recyclerViewAdpater.setLocationDetails(locationDetails, getApplicationContext());
        recycler_view.setAdapter(recyclerViewAdpater);

        search_bar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
           @Override
           public void onSearchStateChanged(boolean enabled) {

           }

           @Override
           public void onSearchConfirmed(CharSequence text) {
               startSearch(text.toString(), true, null, true);
           }

           @Override
           public void onButtonClicked(int buttonCode) {
               if (buttonCode == search_bar.BUTTON_NAVIGATION) {
                   //opening or closing a navigation drawer
               } else if (buttonCode == search_bar.BUTTON_BACK) {
                   search_bar.disableSearch();
               }
           }
       });

        // to display the option menu in the search bar instead of displaying it in the main toolbar/actionbar
        search_bar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.google_icon:
                        Toast.makeText(ClusterActivity.this, "Item: Google was selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.listview_icon:
                        Toast.makeText(ClusterActivity.this, "Item: Listview was selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

        // Floating action button
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

        // onclicklistener for listview
        launcherFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClusterActivity.this, ListviewActivity.class);
                int size = locationDetails.size();
                intent.putExtra("size",size);

//                Log.e("Size of location detail", String.valueOf(locationDetails.size()));

                for (int i = 0; i<locationDetails.size(); i++) {
                    intent.putExtra("details"+ i,locationDetails.get(i));
                }

//                intent.putExtra("detailsObject", locationDetails);

//                Log.e("test1", locationDetails.get(0).getName());
                startActivity(intent);
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

        // calling setter method from recyclerview adapter to set the googleMap
        recyclerViewAdpater.setGoogleMap(googleMap);
        Log.e("Print", "GOOGLE MAP ON READY");

        startDemo(googleMap);
//        Log.e("Count",itemCount.toString());
//        Log.e("Item Count2",itemCount.toString());


    }

    protected abstract void startDemo(GoogleMap googleMap);

    protected abstract void onInfoWindowClick(Marker marker);

    // adding floating action button
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

    // reading data from the asset JSON file
    protected JSONObject readAssets() {
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
//        Autocomplete.setfield(); may want to use when using API for the basic places API (not working because need to pay for API)
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
