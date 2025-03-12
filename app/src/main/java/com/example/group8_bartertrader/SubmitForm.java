package com.example.group8_bartertrader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitForm extends AppCompatActivity {
    private EditText productName, productLocation, productDescription;
    private Spinner productCategory;
    private Button submitButton, backButton, getLocationButton;
    public DatabaseReference databaseReference;
    private String selectedCategory;

    // Declare the location client
    private FusedLocationProviderClient fusedLocationClient;

    // Variables to store location data
    private String address = ""; // For geocoded address (street name)
    private String latLng = "";  // For latitude and longitude

    // The info of the target product
    private String targetProductId, targetProductName, targetProductCategory, targetProductDescription, targetProductLocation;

    // The info of the provder
    private String providerEmail;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        productName = findViewById(R.id.productName);
        productCategory = findViewById(R.id.productCategory);
        productLocation = findViewById(R.id.productLocation);
        productDescription = findViewById(R.id.productDescription);
        submitButton = findViewById(R.id.submitProduct);
        backButton = findViewById(R.id.backButton);
        getLocationButton = findViewById(R.id.getLocationButton);

        // Get the info of the target product and provider
        Intent intent = getIntent();
        if (intent != null) {
            targetProductId = intent.getStringExtra("productId");
            targetProductName = intent.getStringExtra("productName");
            targetProductCategory = intent.getStringExtra("productCategory");
            targetProductLocation = intent.getStringExtra("productLocation");
            targetProductDescription = intent.getStringExtra("productDescription");
            providerEmail = intent.getStringExtra("providerEmail");
        }

        // Initialize the fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // List of categories for the Spinner
        String[] categories = {"Select Category", "Electronics", "Furniture", "Clothing & Accessories", "Books", "Toys & Games",
                "Sports & Outdoors", "Baby & Kids", "Home Improvement", "Vehicles", "Health & Beauty",
                "Pet Supplies", "Collectibles", "Home Decor", "Office Supplies"};

        // Create an ArrayAdapter using the categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        productCategory.setAdapter(adapter);

        // Set a listener for category selection
        productCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = parentView.getItemAtPosition(position).toString(); // Get selected category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedCategory = null; // Handle case when no category is selected
            }
        });

        // Firebase setup
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        mAuth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(v -> submitOffer());

        backButton.setOnClickListener(v -> onBackPressed());

        // Get Current Location when the button is pressed
        getLocationButton.setOnClickListener(v -> getCurrentLocation(location -> {
            if (location != null) {
                // Use Geocoder to get the address from the location
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    assert addresses != null;
                    if (!addresses.isEmpty()) {
                        // Get the address and display it
                        address = addresses.get(0).getAddressLine(0); // Store the address
                        productLocation.setText(address); // Show the address in the EditText

                        // Store latitude and longitude as a string
                        latLng = location.getLatitude() + "," + location.getLongitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SubmitForm.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    public void submitOffer() {
        String name = productName.getText().toString().trim();
        String category = selectedCategory; // Use selected category from Spinner
        String description = productDescription.getText().toString().trim();
        String currentUserEmail = mAuth.getCurrentUser().getEmail();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String OfferId = databaseReference.push().getKey();
        Map<String, Object> offerData = new HashMap<>();
        offerData.put("ProviderEmail", providerEmail);
        offerData.put("ReceiverEmail", currentUserEmail);
        offerData.put("OfferedItemName", name);
        offerData.put("OfferedItemCategory", category);
        offerData.put("OfferedItemLocation", address);
        offerData.put("OfferedItemDescription", description);
        offerData.put("TargetItemId", targetProductId);
        offerData.put("TargetItemName", targetProductName);
        offerData.put("TargetItemCategory", targetProductCategory);
        offerData.put("TargetItemLocation", targetProductLocation);
        offerData.put("TargetItemDescription", targetProductDescription);
        offerData.put("Status", "pending");

        databaseReference.child(OfferId).setValue(offerData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Product Form", "Product offered successfully");
                Toast.makeText(SubmitForm.this, "Product offered successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the form after submission
            } else {
                Log.d("Product Form", "Failed to offer exchange");
                Toast.makeText(SubmitForm.this, "Failed to offer exchange", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation(LocationCallback callback) {
        // Check if the app has permission to access location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        // Get the last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        callback.onLocationReceived(location);
                    } else {
                        Toast.makeText(SubmitForm.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, try to get location again
                getCurrentLocation(location -> {
                    // Handle location data here
                });
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setMockAuth(FirebaseAuth mockAuth) {
        this.mAuth = mockAuth;
    }
    // Callback interface for passing the location
    interface LocationCallback {
        void onLocationReceived(Location location);
    }
}