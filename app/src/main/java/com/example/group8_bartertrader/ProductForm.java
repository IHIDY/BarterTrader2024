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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductForm extends AppCompatActivity {

    private EditText productName, productLocation, productDescription;
    private Spinner productCategory, productCondition;
    private Button submitButton, backButton;
    private DatabaseReference databaseReference, preferencesRef, notificationsRef;
    private String selectedCategory, selectedCondition;

    public ProductForm(){}
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
        submitButton.setOnClickListener(v -> submitProduct());
        backButton.setOnClickListener(v -> onBackPressed());
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

    private void submitProduct() {
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

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "No email";

        String productId = databaseReference.push().getKey();
        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", name);
        productData.put("category", category);
        productData.put("condition", condition);
        productData.put("location", location); // Location field is now a custom string
        productData.put("description", description);
        productData.put("isAvailable", true);
        productData.put("datePosted", formattedDate);
        productData.put("email", currentUserEmail);

        databaseReference.child(productId).setValue(productData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar.make(submitButton, "Product posted successfully", Snackbar.LENGTH_SHORT).show();

                checkPreferences(location, category, generateString(location, category));
                finish();
            } else {
                Snackbar.make(submitButton, "Failed to post product", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
