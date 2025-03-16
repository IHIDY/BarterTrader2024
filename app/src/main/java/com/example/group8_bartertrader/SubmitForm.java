package com.example.group8_bartertrader;

import android.Manifest;
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
import java.util.List;

public class SubmitForm extends AppCompatActivity {
    private EditText productName, productLocation, productDescription;
    private Spinner productCategory;
    private Button submitButton, backButton, getLocationButton;
    private String selectedCategory;
    private FusedLocationProviderClient fusedLocationClient;
    private String providerEmail;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    SubmitHelper submitHelper; // 使用 SubmitHelper

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        mAuth = FirebaseAuth.getInstance();
        submitHelper = new SubmitHelper(databaseReference, mAuth); // 初始化 SubmitHelper

        String[] categories = {"Select Category", "Electronics", "Furniture", "Clothing & Accessories", "Books", "Toys & Games",
                "Sports & Outdoors", "Baby & Kids", "Home Improvement", "Vehicles", "Health & Beauty",
                "Pet Supplies", "Collectibles", "Home Decor", "Office Supplies"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        productCategory.setAdapter(adapter);

        productCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedCategory = null;
            }
        });

        submitButton.setOnClickListener(v -> {
            String name = productName.getText().toString().trim();
            String category = selectedCategory;
            String description = productDescription.getText().toString().trim();
            String currentUserEmail = mAuth.getCurrentUser().getEmail();
            String location = productLocation.getText().toString().trim();

            submitHelper.submitOffer(name, category, description, location, currentUserEmail,
                    "testProduct123", "Test Product", "Electronics", "Test Description", "New York");
        });

        backButton.setOnClickListener(v -> onBackPressed());

        getLocationButton.setOnClickListener(v -> getCurrentLocation(location -> {
            if (location != null) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        String address = addresses.get(0).getAddressLine(0);
                        productLocation.setText(address);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SubmitForm.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void getCurrentLocation(LocationCallback callback) {
        productLocation.setText("123 Fake Street, Faketown, FK 12345");
        Location fakeLocation = new Location("dummyprovider");
        fakeLocation.setLatitude(37.7749);
        fakeLocation.setLongitude(-122.4194);
        callback.onLocationReceived(fakeLocation);
    }

    interface LocationCallback {
        void onLocationReceived(Location location);
    }
}
