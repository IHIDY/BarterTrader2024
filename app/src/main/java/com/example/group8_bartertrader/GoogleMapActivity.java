package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class GoogleMapActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        // Get location from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("itemLocation")) {
            String locationString = intent.getStringExtra("itemLocation");

            if (locationString != null && !locationString.isEmpty()) {
                Log.d("ITEMLOCATION>>>", locationString);

                // Open Google Maps app with a search query
                openGoogleMapsWithSearch(locationString.trim());
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGoogleMapsWithSearch(String locationString) {
        // Create a URI to search for the location
        String uri = "geo:0,0?q=" + locationString;

        // Create an Intent to launch Google Maps with the search query
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));

        // Check if there's an app to handle the intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Start the activity and open the map
            startActivity(mapIntent);
            Log.d("MAP_INTENT", "Google Maps launched with query: " + locationString);
        } else {
            // Handle the case where no maps app is available
            Log.e("MAP_INTENT", "No app available to handle the Google Maps intent");
            Toast.makeText(this, "No app available to open Google Maps", Toast.LENGTH_SHORT).show();
        }
    }
}
