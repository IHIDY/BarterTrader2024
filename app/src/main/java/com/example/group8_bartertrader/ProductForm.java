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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProductForm extends AppCompatActivity {
    private EditText productName, productLocation, productDescription;
    private Spinner productCategory; // Changed to Spinner
    private Button submitButton, backButton;
    private DatabaseReference databaseReference;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        productName = findViewById(R.id.productName);
        productCategory = findViewById(R.id.productCategory); // Spinner
        productLocation = findViewById(R.id.productLocation);
        productDescription = findViewById(R.id.productDescription);
        submitButton = findViewById(R.id.submitProduct);
        backButton = findViewById(R.id.backButton);

        // List of categories for the Spinner
        String[] categories = {"Select Category","Electronics", "Furniture", "Clothing & Accessories", "Books", "Toys & Games",
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProduct();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void submitProduct() {
        String name = productName.getText().toString().trim();
        String category = selectedCategory; // Use selected category from Spinner
        String location = productLocation.getText().toString().trim();
        String description = productDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(location) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String productId = databaseReference.push().getKey();
        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", name);
        productData.put("category", category);
        productData.put("locationString", location);
        productData.put("location", null);
        productData.put("description", description);
        productData.put("isAvailable", true);
//        productData.put("SellerEmail")

        databaseReference.child(productId).setValue(productData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Product Form", "Product posted successfully");
                Toast.makeText(ProductForm.this, "Product posted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the form after submission
            } else {
                Log.d("Product Form", "Failed to post product");
                Toast.makeText(ProductForm.this, "Failed to post product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
