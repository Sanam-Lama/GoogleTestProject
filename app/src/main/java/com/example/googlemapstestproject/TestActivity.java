package com.example.googlemapstestproject;

/* This activity here is just for the testing purpose */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

public class TestActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdpater recyclerViewAdpater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // adding circle to the arraylist: recyclerview
        recycler_view = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setOrientation(HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        recyclerViewAdpater = new RecyclerViewAdpater(getBaseContext());
        recycler_view.setAdapter(recyclerViewAdpater);
    }
}
