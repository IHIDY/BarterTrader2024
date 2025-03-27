package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group8_bartertrader.model.Product;
import com.example.group8_bartertrader.provider.ProviderProductList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderDash extends AppCompatActivity {

    private DatabaseReference productsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_dash);

        Button pBtn = findViewById(R.id.pBtn);
        Button postUsedProductsBtn = findViewById(R.id.postButton);
        Button receivedOffersBtn = findViewById(R.id.button_received_offers);
        Button viewPostProducts = findViewById(R.id.viewPostProducts);

        // Initialize Firebase Authentication
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize Firebase Database Reference
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to SettingsActivity
        pBtn.setOnClickListener(v -> {
            Log.d("BTN CLICKED", "P BTN CLICKED");
            Intent intent = new Intent(ProviderDash.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Navigate to ProductForm for posting a new product
        postUsedProductsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderDash.this, ProductForm.class);
            startActivity(intent);
        });

        // Navigate to ReceivedOfferActivity for received offers
        receivedOffersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderDash.this, ReceivedOfferActivity.class);
            startActivity(intent);
        });

        // Navigate to ProviderProductList to view posted products
        viewPostProducts.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderDash.this, ProviderProductList.class);
            startActivity(intent);
        });
    }

    // Method to edit product by productId
    private void editProduct(String productId) {
        Intent intent = new Intent(ProviderDash.this, EditProductActivity.class);
        intent.putExtra("productId", productId); // Pass the productId to EditProductActivity
        startActivity(intent);
    }

    // Method to delete product by productId
    private void deleteProduct(String productId) {
        productsRef.child(productId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(ProviderDash.this, "Product deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProviderDash.this, "Failed to delete product", Toast.LENGTH_SHORT).show());
    }

    // Method to handle the edit and delete button clicks in the product list
    private void setUpProductButtons(View productView, String productId) {
        Button editButton = productView.findViewById(R.id.editButton); // Assuming the button's id is editButton
        Button deleteButton = productView.findViewById(R.id.deleteButton); // Assuming the button's id is deleteButton

        // Set edit button listener
        editButton.setOnClickListener(v -> editProduct(productId));

        // Set delete button listener
        deleteButton.setOnClickListener(v -> deleteProduct(productId));
    }
}
