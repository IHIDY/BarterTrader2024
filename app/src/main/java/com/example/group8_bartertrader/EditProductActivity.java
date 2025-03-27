package com.example.group8_bartertrader;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProductActivity extends AppCompatActivity {

    private EditText productNameEditText, productDescriptionEditText, productLocationEditText;
    private Spinner productCategorySpinner;
    private Button saveButton, cancelButton;
    private String productId;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize Firebase Database Reference
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        // Initialize Views
        productNameEditText = findViewById(R.id.productNameEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productCategorySpinner = findViewById(R.id.productCategoryEditText);  // Using Spinner for category
        productLocationEditText = findViewById(R.id.productLocationEditText);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Retrieve product ID passed from the intent
        productId = getIntent().getStringExtra("productId");

        // Fetch product data and populate the fields
        fetchProductDetails();

        // Save button logic
        saveButton.setOnClickListener(v -> saveProductChanges());

        // Cancel button logic
        cancelButton.setOnClickListener(v -> finish());  // Close the activity
    }

    private void fetchProductDetails() {
        // Fetch product from Firebase using productId and populate the fields
        if (productId == null) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show();
            return;
        }

        productsRef.child(productId).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                Product product = dataSnapshot.getValue(Product.class);
                if (product != null) {
                    productNameEditText.setText(product.getName());
                    productDescriptionEditText.setText(product.getDescription());

                    // Set the category in the spinner
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) productCategorySpinner.getAdapter();
                    int position = adapter.getPosition(product.getCategory());
                    productCategorySpinner.setSelection(position);

                    productLocationEditText.setText(product.getLocation());
                }
            } else {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProductActivity.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveProductChanges() {
        String name = productNameEditText.getText().toString().trim();
        String description = productDescriptionEditText.getText().toString().trim();
        String category = productCategorySpinner.getSelectedItem().toString().trim();  // Get selected category from Spinner
        String location = productLocationEditText.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a product object with the updated values
        Product updatedProduct = new Product(productId, name, description, category, location, true);  // Assuming 'true' for availability (can be edited as needed)

        // Update the product in Firebase
        productsRef.child(productId).setValue(updatedProduct).addOnSuccessListener(aVoid -> {
            Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity after saving
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
        });
    }
}
