package com.example.group8_bartertrader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductForm extends AppCompatActivity {

    private EditText productName, productLocation, productDescription;
    private Spinner productCategory, productCondition;
    private Button submitButton, backButton, getLocationButton;
    private DatabaseReference databaseReference;
    private String selectedCategory, selectedCondition;
    private FusedLocationProviderClient fusedLocationClient;
    private String address = "";
    private String latLng = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        productName = findViewById(R.id.productName);
        productCategory = findViewById(R.id.productCategory);
        productCondition = findViewById(R.id.productCondition);
        productLocation = findViewById(R.id.productLocation);
        productDescription = findViewById(R.id.productDescription);
        submitButton = findViewById(R.id.submitProduct);
        backButton = findViewById(R.id.backButton);
        getLocationButton = findViewById(R.id.getLocationButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupSpinners();

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        submitButton.setOnClickListener(v -> submitProduct());
        backButton.setOnClickListener(v -> onBackPressed());
        getLocationButton.setOnClickListener(v -> getCurrentLocation(this::handleLocationResult));
    }

    private void setupSpinners() {
        String[] categories = {"Select Category", "Electronics", "Furniture", "Clothing & Accessories", "Books", "Toys & Games",
                "Sports & Outdoors", "Baby & Kids", "Home Improvement", "Vehicles", "Health & Beauty",
                "Pet Supplies", "Collectibles", "Home Decor", "Office Supplies"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        productCategory.setAdapter(adapter);

        String[] conditions = {"Select Condition", "Good", "Average", "Bad"};
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, conditions);
        productCondition.setAdapter(conditionAdapter);

        productCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });

        productCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCondition = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void submitProduct() {
        String name = productName.getText().toString().trim();
        String category = selectedCategory;
        String description = productDescription.getText().toString().trim();
        String condition = selectedCondition;

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description)
                || TextUtils.isEmpty(address) || TextUtils.isEmpty(condition)) {
            Snackbar.make(submitButton, "All fields are required", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "No email";

        String productId = databaseReference.push().getKey();
        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", name);
        productData.put("category", category);
        productData.put("condition", condition);
        productData.put("location", address);
        productData.put("latLngLocation", latLng);
        productData.put("description", description);
        productData.put("isAvailable", true);
        productData.put("datePosted", formattedDate);
        productData.put("email", currentUserEmail);

        databaseReference.child(productId).setValue(productData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Product Form", "Product posted successfully");
                Snackbar.make(submitButton, "Product posted successfully", Snackbar.LENGTH_SHORT).show();
                // Add a 2-second delay before calling finish()
                new Handler().postDelayed(() -> finish(), 2000);  // 2000 ms = 2 seconds
            } else {
                Log.d("Product Form", "Failed to post product");
                Snackbar.make(submitButton, "Failed to post product", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                callback.onLocationReceived(location);
            } else {
                Snackbar.make(getLocationButton, "Unable to get location", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLocationResult(Location location) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0);
                productLocation.setText(address);
                latLng = location.getLatitude() + "," + location.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(getLocationButton, "Unable to get address", Snackbar.LENGTH_SHORT).show();
        }
    }

    interface LocationCallback {
        void onLocationReceived(Location location);
    }
}