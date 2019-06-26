package com.example.googlemapstestproject;

/* Implement ClusterItem to represent a marker on the map. The cluster item returns the position of the
 * marker as a LatLng object, and an optional title or snippet.
 * */

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {

    private final LatLng latLng;
    private final String title;
    private final String snippet;

    public MyItem(LatLng latLng, String title, String snippet) {
        this.latLng = latLng;
        this.title = title;
        this.snippet = snippet;
    }

    public MyItem(double lat, double lng) {
        latLng = new LatLng(lat, lng);
        title = "";
        snippet = "";

    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
