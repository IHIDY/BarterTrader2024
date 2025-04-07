package com.example.group8_bartertrader;

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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import java.util.HashMap;
import java.util.Map;

public class ProductForm extends AppCompatActivity {

    private EditText productName, productLocation, productDescription;
    private Spinner productCategory, productCondition;
    private Button submitButton, backButton;
    private DatabaseReference databaseReference, preferencesRef, notificationsRef;
    private String selectedCategory, selectedCondition;
    private String productId; // Holds the ID of the product being edited

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

        setupSpinners();
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        preferencesRef = FirebaseDatabase.getInstance().getReference("Preferences");
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications");

        // Check if this activity was opened for editing an existing product
        if (getIntent().hasExtra("productId")) {
            productId = getIntent().getStringExtra("productId");
            populateProductData();
            Log.d("ProductForm", "Editing product with ID: " + productId);
        }

        submitButton.setOnClickListener(v -> {
            // Separate the logic based on whether productId exists (edit) or not (new submission)
            if (productId != null) {
                updateProduct();
            } else {
                submitNewProduct();
            }
        });
        backButton.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Sets up the spinners for selecting product category and condition.
     * Populates the spinners with predefined categories and conditions.
     */
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
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCondition = null;
            }
        });
    }
    private String generateString(String location, String category) {
        Log.d("<<< GenerateString() >>>", "You have a " + category + " product, in " + location);
        return "You have a " + category + " product, in " + location;
    }

    private void pushToNotification(String receiverEmail, String message) {

        // Create a map to hold the data
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("receiverEmail", receiverEmail);
        notificationData.put("message", message);
        notificationData.put("time", null);

        // Push the data with a unique ID
        notificationsRef.push().setValue(notificationData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Notification pushed successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to push notification", e);
                });
    }

    // This is hardcoded, needs to be changed later
    private void checkPreferences(String location, String category, String message) {

        preferencesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String email = childSnapshot.child("email").getValue(String.class);
                    String preferredCategory = childSnapshot.child("preferredCategory").getValue(String.class);
                    String preferredLocation = childSnapshot.child("preferredLocation").getValue(String.class);

                    if (category.equalsIgnoreCase(preferredCategory) && location.equalsIgnoreCase(preferredLocation)) {
                        // Do something, e.g., log it or perform an action
                        String userId = email;
                        Log.d("Electronics User", "User " + email + " prefers Electronics in "+ location);
                        // Put notification into notification field.

                        pushToNotification(email, message);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Error reading preferences: " + error.getMessage());
            }
        });
    }
    /**
     * Populates the product data into the form fields when editing an existing product.
     */
    private void populateProductData() {
        databaseReference.child(productId).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                Map<String, Object> productData = (Map<String, Object>) dataSnapshot.getValue();
                if (productData != null) {
                    productName.setText((String) productData.get("name"));
                    productDescription.setText((String) productData.get("description"));
                    productLocation.setText((String) productData.get("location"));

                    String category = (String) productData.get("category");
                    String condition = (String) productData.get("condition");

                    ArrayAdapter categoryAdapter = (ArrayAdapter) productCategory.getAdapter();
                    productCategory.setSelection(categoryAdapter.getPosition(category));

                    ArrayAdapter conditionAdapter = (ArrayAdapter) productCondition.getAdapter();
                    productCondition.setSelection(conditionAdapter.getPosition(condition));
                }
            }
        }).addOnFailureListener(e ->
                Toast.makeText(ProductForm.this, "Failed to load product", Toast.LENGTH_SHORT).show());
    }

    /**
     * Handles the submission of a new product to the Firebase database.
     * Validates input fields and then adds the new product data to the database.
     */
    private void submitNewProduct() {
        String name = productName.getText().toString().trim();
        String category = selectedCategory;
        String description = productDescription.getText().toString().trim();
        String condition = selectedCondition;
        String location = productLocation.getText().toString().trim().toUpperCase();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description)
                || TextUtils.isEmpty(location) || TextUtils.isEmpty(condition)) {
            Snackbar.make(submitButton, "All fields are required", Snackbar.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "No email";

        // Create new product - generate new product ID
        String newProductId = databaseReference.push().getKey();

        Map<String, Object> productData = new HashMap<>();
        productData.put("id", newProductId);
        productData.put("name", name);
        productData.put("category", category);
        productData.put("condition", condition);
        productData.put("location", location);
        productData.put("description", description);
        productData.put("isAvailable", true);
        productData.put("email", currentUserEmail);

        databaseReference.child(newProductId).setValue(productData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar.make(submitButton, "Product submitted successfully!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> finish())
                        .show();
                checkPreferences(location, category, generateString(location, category));
            } else {
                Snackbar.make(submitButton, "Failed to submit product", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles updating an existing product in the Firebase database.
     * Validates input fields and updates the product data in the database.
     */
    private void updateProduct() {
        String name = productName.getText().toString().trim();
        String category = selectedCategory;
        String description = productDescription.getText().toString().trim();
        String condition = selectedCondition;
        String location = productLocation.getText().toString().trim().toUpperCase();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description)
                || TextUtils.isEmpty(location) || TextUtils.isEmpty(condition)) {
            Snackbar.make(submitButton, "All fields are required", Snackbar.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "No email";

        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", name);
        productData.put("category", category);
        productData.put("condition", condition);
        productData.put("location", location);
        productData.put("description", description);
        productData.put("isAvailable", true);
        productData.put("email", currentUserEmail);

        databaseReference.child(productId).setValue(productData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar.make(submitButton, "Product updated successfully!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> finish())
                        .show();
            } else {
                Snackbar.make(submitButton, "Failed to update product", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
