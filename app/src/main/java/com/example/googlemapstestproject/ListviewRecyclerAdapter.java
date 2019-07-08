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

import com.google.android.gms.maps.model.LatLng;

import java.net.ConnectException;
import java.util.ArrayList;

public class ListviewRecyclerAdapter extends RecyclerView.Adapter<ListviewRecyclerAdapter.ListViewHolder> {

    ArrayList<LocationDetails> locationDetails;
    private Context context;

    public ListviewRecyclerAdapter(ArrayList<LocationDetails> locationDetails, Context context) {
        this.locationDetails = locationDetails;
        this.context = context;

//        Log.e("location details", locationDetails.get(0).getName());
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_layout, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, final int position) {
        viewHolder.image_view.setImageResource(R.drawable.ic_print_black);
        viewHolder.text_title.setText("Location Name: " + locationDetails.get(position).getName());
        viewHolder.latlng.setText("Lat/Lng: " + String.valueOf( locationDetails.get(position).getLat()) + ", " + locationDetails.get(position).getLng());
        viewHolder.address.setText("Vicinity: " + locationDetails.get(position).getVicinity());

        viewHolder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("nameObject",locationDetails.get(position).getName());
                intent.putExtra("vicinityObject", locationDetails.get(position).getVicinity());
                intent.putExtra("latObject",String.valueOf(locationDetails.get(position).getLat()));
                intent.putExtra("lngObject",String.valueOf(locationDetails.get(position).getLng()));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationDetails.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        TextView text_title, latlng, address;
        ImageView image_view;
        RelativeLayout relative_layout;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            text_title = (TextView)itemView.findViewById(R.id.title);
            image_view = (ImageView)itemView.findViewById(R.id.imageView);
            latlng = (TextView)itemView.findViewById(R.id.latlng);
            address = (TextView)itemView.findViewById(R.id.address);
            relative_layout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
