package com.example.googlemapstestproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdpater extends RecyclerView.Adapter<RecyclerViewAdpater.ImageViewHolder> {

    private GoogleMap googleMap;
    private double latitude, longitude;
    private Context context;

    // an arraylist of class LocationDetails which is a collection of name, vicinity and latlng
    ArrayList<LocationDetails> locationDetails = new ArrayList<>();

    public void setLocationDetails(ArrayList<LocationDetails> locationDetails, Context context) {
        this.locationDetails = locationDetails;
        this.context = context;
    }

    // setter method to set the googleMap
    public void setGoogleMap(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public RecyclerViewAdpater(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, final int position) {

//        viewHolder.image_view.setText("Miami Beach");
        Glide.with(context)
                .asBitmap()
                .load(R.drawable.blue_circle)
                .into(viewHolder.image_view);

        viewHolder.name.setText(locationDetails.get(position).getName());
        viewHolder.vicinity.setText(locationDetails.get(position).getVicinity());

        viewHolder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                latitude = locationDetails.get(position).getLat();
                longitude = locationDetails.get(position).getLng();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationDetails.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view;
        TextView name, vicinity;
        RelativeLayout relative_layout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view = (ImageView)itemView.findViewById(R.id.imageView);
            name = (TextView)itemView.findViewById(R.id.name);
            vicinity = (TextView)itemView.findViewById(R.id.vicinity);
            relative_layout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);

            // to make the image (circle) transparent
            image_view.setAlpha(0.6f);

        }
    }
}
