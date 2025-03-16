package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
            String locationString = intent.getStringExtra("itemLocation");

            if (locationString != null && locationString.contains(",")) {
                try {
                    String[] location = locationString.split(",");
                    double latitude = Double.parseDouble(location[0].trim());
                    double longitude = Double.parseDouble(location[1].trim());
                    itemLocation = new LatLng(latitude, longitude);
                } catch (NumberFormatException e) {
                    itemLocation = null;
                    e.printStackTrace();
                }
            }
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
        } else {
            Toast.makeText(this, "Invalid location data", Toast.LENGTH_SHORT).show();
        }
    }
}
