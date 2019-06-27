package com.example.googlemapstestproject;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.google.maps.android.clustering.ClusterManager;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ClusterActivity extends AppCompatActivity implements OnMapReadyCallback {

//    private ClusterManager<MyItem> clusterManager;
    private GoogleMap googleMap;
    String apiKey = "AIzaSyD0UD9bfUS71Go5KZmbn8esePRit1VPXvg";

    MaterialSearchBar search_bar;
    private List<AutocompletePrediction> predictionList;

    RecyclerView recyclerView;
    List<PlacesPOJO.CustomA> results;
    List<StoreModel> storeModels;
    ApiInterface apiService;
    LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        search_bar = (MaterialSearchBar) findViewById(R.id.searchBar);

        initMap();
        // Initialize Places.
        Places.initialize(ClusterActivity.this, apiKey);

        // Create a new Places client instance.
        final PlacesClient placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

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

        fetchLocation();

        // API Client and network call
        apiService = RetroAPIClient.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

        private void fetchLocation() {

            SmartLocation.with(this).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            String latLngString = location.getLatitude() + "," + location.getLongitude();
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    });











//        // UNCOMMENT this once the API for places is added and the JSON file that i added initially is removed
//        search_bar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // confirm the setCountry for US and add it here to look for auto complete predictions in US
//                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
//                        .setCountry("us")
//                        .setTypeFilter(TypeFilter.ADDRESS)
//                        .setSessionToken(token)
//                        .setQuery(s.toString())
//                        .build();
//
//                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
//                        if (task.isSuccessful()) {
//                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
//                            if (predictionsResponse != null) {
//                                predictionList = predictionsResponse.getAutocompletePredictions();
//
//                                // provide an arraylist of string to convert predictionList to string
//                                List<String> suggestionList = new ArrayList<>();
//                                for (int i=0; i<predictionList.size(); i++) {
//                                    AutocompletePrediction prediction = predictionList.get(i);
//                                    suggestionList.add(prediction.getFullText(null).toString());
//                                }
//
//                                // now the predictionlist is converted to string of suggestionlist which will be passed to our search_bar
//                                search_bar.updateLastSuggestions(suggestionList);
//                                if (!search_bar.isSuggestionsVisible()) {
//                                    search_bar.showSuggestionsList();
//                                }
//                            }
//                        }
//                        else {
//                            Log.e("mytag", "Prediction fetching task unsuccessful.");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        // once the suggestion is selected then we need to get the latlng of the place selected
//        search_bar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
//            @Override
//            public void OnItemClickListener(int position, View v) {
//
//                if (position >= predictionList.size()) {
//                    return;
//                }
//
//                // fetch the prediction selected by user
//                AutocompletePrediction selectedPrediction = predictionList.get(position);
//                String suggestion = search_bar.getLastSuggestions().get(position).toString();
//                search_bar.setText(suggestion);
//
//                // create a delay of 1 second to remove the suggestion after selected
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        search_bar.clearSuggestions();
//                    }
//                }, 1000);
//
//
//                // when the user selects the suggestion, we want to close the soft keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                if (imm != null)
//                    imm.hideSoftInputFromWindow(search_bar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//                // pass this placeId to get the latlng of the place
//                String placeId = selectedPrediction.getPlaceId();
//                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
//
//                // create a request
//                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
//                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
//                    @Override
//                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
//
//                        Place place = fetchPlaceResponse.getPlace();
//                        Log.e("mytag", "Place found: " + place.getName());
//                        LatLng latLngOfPlace = place.getLatLng();
//
//                        if (latLngOfPlace != null) {
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, 15));
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//
//                    // if API failed to fetch the place
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        if (e instanceof ApiException) {
//                            ApiException apiException = (ApiException) e;
//                            apiException.printStackTrace();
//                            int statusCode = apiException.getStatusCode();
//                            Log.e("mytag", "place not found: " + e.getMessage());
//                            Log.e("mytag", "status code: " + statusCode);
//                        }
//
//
//                    }
//                });
//            }
//
//            @Override
//            public void OnItemDeleteListener(int position, View v) {
//
//            }
//        });
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
