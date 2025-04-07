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

/**
 * ProviderDash class that manages the dashboard activity for the product provider.
 * Allows navigation to various activities for settings, posting products, received offers, and viewing posted products.
 */
public class ProviderDash extends AppCompatActivity {

    private DatabaseReference productsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_dash);

        // Button initialization
        Button pBtn = findViewById(R.id.pBtn);
        Button postUsedProductsBtn = findViewById(R.id.postButton);
        Button receivedOffersBtn = findViewById(R.id.button_received_offers);
        Button viewPostProducts = findViewById(R.id.viewPostProducts);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        // Adjust padding based on system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up navigation buttons
        pBtn.setOnClickListener(v -> navigateTo(SettingsActivity.class));
        postUsedProductsBtn.setOnClickListener(v -> navigateTo(ProductForm.class));
        receivedOffersBtn.setOnClickListener(v -> navigateTo(ReceivedOfferActivity.class));
        viewPostProducts.setOnClickListener(v -> navigateTo(ProviderProductList.class));
    }

    /**
     * Helper method to start a new activity.
     */
    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(ProviderDash.this, activityClass);
        startActivity(intent);
    }

    /**
     * Edit a product based on its product ID.
     */
    private void editProduct(String productId) {
        Intent intent = new Intent(ProviderDash.this, ProductForm.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    /**
     * Delete a product from the database by its product ID.
     */
    private void deleteProduct(String productId) {
        productsRef.child(productId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(ProviderDash.this, "Product deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProviderDash.this, "Failed to delete product", Toast.LENGTH_SHORT).show());
    }

    /**
     * Set up edit and delete buttons for a product.
     */
    private void setUpProductButtons(View productView, String productId) {
        Button editButton = productView.findViewById(R.id.editButton);
        Button deleteButton = productView.findViewById(R.id.deleteButton);

        // Set edit and delete button listeners
        editButton.setOnClickListener(v -> editProduct(productId));
        deleteButton.setOnClickListener(v -> deleteProduct(productId));
    }
}
