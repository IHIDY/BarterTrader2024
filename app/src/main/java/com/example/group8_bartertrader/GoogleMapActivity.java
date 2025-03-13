package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap map;
    LatLng itemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        // Get location from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("itemLocation")) {
            String[] location = intent.getStringExtra("itemLocation").split(",");
            double latitude = Double.parseDouble(location[0]);
            double longitude = Double.parseDouble(location[1]);
            itemLocation = new LatLng(latitude, longitude);
        }

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (itemLocation != null) {
            // Add marker and move camera
            map.addMarker(new MarkerOptions().position(itemLocation).title("Item Location"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 15));
        }
    }
}
