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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class SubmitForm extends AppCompatActivity {
    private EditText productName, productLocation, productDescription;
    private Spinner productCategory;
    private Button submitButton, backButton;
    private String selectedCategory;
    private FusedLocationProviderClient fusedLocationClient;
    private String providerEmail;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    SubmitHelper submitHelper; // Subimt Helper we use for interacting with backend
    String targetProviderEmail, targetProductId, targetProductName, targetProductCategory, targetProductLocation, targetProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        // Get all the info from Intent
        Intent intent = getIntent();
        targetProductId = intent.getStringExtra("productId");
        targetProductName = intent.getStringExtra("productName");
        targetProductCategory = intent.getStringExtra("productCategory");
        targetProductLocation = intent.getStringExtra("productLocation");
        targetProductDescription = intent.getStringExtra("productDescription");
        targetProviderEmail = intent.getStringExtra("providerEmail");

        System.out.println(targetProductName);

        // Get all the UI Element
        productName = findViewById(R.id.productName);
        productCategory = findViewById(R.id.productCategory);
        productLocation = findViewById(R.id.productLocation);
        productDescription = findViewById(R.id.productDescription);
        submitButton = findViewById(R.id.submitProduct);
        backButton = findViewById(R.id.backButton);

        // Initialize FirebaseAuth FirebaseDatabase SubmitHelper
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        mAuth = FirebaseAuth.getInstance();
        submitHelper = new SubmitHelper(databaseReference, mAuth);

        // Set up the spinner
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

        // Set up the submitButton
        submitButton.setOnClickListener(v -> {
            String name = productName.getText().toString().trim();
            String category = selectedCategory;
            String description = productDescription.getText().toString().trim();
            String currentUserEmail = mAuth.getCurrentUser().getEmail();
            String location = productLocation.getText().toString().trim().toUpperCase();

            submitHelper.submitOffer(name, category, description, location, currentUserEmail,
                    targetProductId, targetProductName, targetProductCategory, targetProductDescription, targetProductLocation, targetProviderEmail,
                    success -> {
                        View rootView = findViewById(android.R.id.content);
                        if (success) {
                            Snackbar.make(rootView, "Offer submitted successfully!", Snackbar.LENGTH_SHORT).show();

                            Intent nextIntent = new Intent(SubmitForm.this, ReceiverDash.class);
                            startActivity(nextIntent);
                            finish();
                        } else {
                            Snackbar.make(rootView, "Failed to submit offer!, Offer already exists for this product", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set up the BackButton
        backButton.setOnClickListener(v -> onBackPressed());
    }

}
