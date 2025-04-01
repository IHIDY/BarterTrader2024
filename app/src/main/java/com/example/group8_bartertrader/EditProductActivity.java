package com.example.group8_bartertrader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EditProductActivity extends AppCompatActivity {

    private EditText editName, editDescription, editCategory, editLocation;
    private Button saveButton;
    private DatabaseReference productsRef;
    private String productId;
    private Product product; // Store product data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize Firebase Database reference
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        // Get the productId passed from the ProviderProductList activity
        productId = getIntent().getStringExtra("productId");

        // Initialize UI components
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editCategory = findViewById(R.id.editCategory);
        editLocation = findViewById(R.id.editLocation);
        saveButton = findViewById(R.id.saveButton);

        // Fetch the product from Firebase
        fetchProductFromFirebase();

        // Set up the save button listener
        saveButton.setOnClickListener(v -> saveProductChanges());
    }

    // Method to fetch the product data from Firebase based on productId
    private void fetchProductFromFirebase() {
        // Fetch product from Firebase by productId
        productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the product exists
                if (dataSnapshot.exists()) {
                    product = dataSnapshot.getValue(Product.class);

                    if (product != null) {
                        // Populate the fields with existing product data
                        editName.setText(product.getName());
                        editDescription.setText(product.getDescription());
                        editCategory.setText(product.getCategory());
                        editLocation.setText(product.getLocation());
                    }
                } else {
                    // If the product doesn't exist
                    Toast.makeText(EditProductActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error fetching product data
                Log.w("EditProductActivity", "loadProduct:onCancelled", databaseError.toException());
                Toast.makeText(EditProductActivity.this, "Failed to load product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to save the changes made to the product
    private void saveProductChanges() {
        // Get the modified product data from the input fields
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        // Check for non-empty fields before saving
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || location.isEmpty()) {
            Toast.makeText(EditProductActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the product object with the new values
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setLocation(location);

        // Save the updated product back to Firebase
        productsRef.child(productId).setValue(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity after successful update
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                });
    }
}
