package com.example.group8_bartertrader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReceiverDash extends AppCompatActivity implements LocationHelper.OnLocationFetchListener {

    // UI Components
    Button receiverSettingBtn;
    TextView locationTextView;
    RecyclerView productRecyclerView;

    // Helper and Database References
    LocationHelper locationHelper;
    DatabaseReference productsRef;

    // Product List and Adapter
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiver_dash);

        // Initialize UI Components
        receiverSettingBtn = findViewById(R.id.recsettingbutton);
        locationTextView = findViewById(R.id.locationTextView);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        // Initialize Product List and Adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        // Initialize Firebase Database Reference
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        // Initialize Location Helper
        locationHelper = new LocationHelper(this);

        // Check and Request Location Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationHelper.getCurrentLocation(this);
        }

        // Handle Window Insets for Edge-to-Edge Display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set OnClickListener for Settings Button
        receiverSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "LOGIN BUTTON CLICKED");
                Intent intent = new Intent(ReceiverDash.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    // Handle Permission Request Results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Callback for Successful Location Fetch
    @Override
    public void onLocationFetched(double latitude, double longitude) {
        String cityName = getCityName(latitude, longitude);
        locationTextView.setText("Location: " + cityName);
        fetchProductsFromFirebase(latitude, longitude);
    }

    // Callback for Failed Location Fetch
    @Override
    public void onLocationFetchFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Convert Latitude and Longitude to City Name
    private String getCityName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String cityName = address.getAdminArea(); // Get the administrative area (e.g., state)
                if (cityName == null) {
                    cityName = address.getLocality(); // Get the locality (e.g., city)
                    if (cityName == null) {
                        cityName = address.getSubAdminArea(); // Get the sub-administrative area
                    }
                }
                return cityName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Location: Not available";
    }

    // Fetch Products from Firebase within a Certain Range
    private void fetchProductsFromFirebase(double latitude, double longitude) {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        double[] productLatLng = LocationHelper.parseLatLngLocation(product.getLatLngLocation());
                        if (productLatLng != null && isWithinRange(latitude, longitude, productLatLng[0], productLatLng[1])) {
                            productList.add(product);
                        }
                    }
                }
                updateProductList(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ReceiverDash", "loadProducts:onCancelled", databaseError.toException());
                Toast.makeText(ReceiverDash.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Check if a Product is Within a Certain Range
    private boolean isWithinRange(double userLat, double userLong, double productLat, double productLong) {
        final int RADIUS = 10; // Radius in kilometers
        double distance = calculateDistance(userLat, userLong, productLat, productLong);
        return distance <= RADIUS;
    }

    // Calculate Distance Between Two Coordinates Using Haversine Formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Update the Product List in the RecyclerView
    private void updateProductList(List<Product> productList) {
        productAdapter.setProductList(productList);
        productAdapter.notifyDataSetChanged();
    }
}