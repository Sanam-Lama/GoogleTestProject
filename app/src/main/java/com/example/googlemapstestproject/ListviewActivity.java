package com.example.googlemapstestproject;

/* This is the activity to display the grid(list) view of the clicked location using recyclerview */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class ListviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListviewRecyclerAdapter listviewRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<LocationDetails> locationDetails;

    FloatingActionButton googleFAB;
    FloatingActionButton launcherFAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerListView);
//        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        ((GridLayoutManager) layoutManager).setOrientation(HORIZONTAL);
//        ((LinearLayoutManager) layoutManager).setOrientation(HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        int size = getIntent().getIntExtra("size",0);
        locationDetails = new ArrayList<>();
        for(int i=0; i<size; i++){
            LocationDetails temp = getIntent().getParcelableExtra("details"+i);
            locationDetails.add(temp);
        }

        listviewRecyclerAdapter = new ListviewRecyclerAdapter(locationDetails, getApplicationContext());
        recyclerView.setAdapter(listviewRecyclerAdapter);

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

        // onclick listener for googleFAB button
        googleFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClusterRenderer.class);
                startActivity(intent);
            }
        });
    }

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

}
