package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.model.Product;

import java.io.Serializable;

public class DetailsActivity extends AppCompatActivity {

    private TextView productName, productCategory, productLocation, productDescription, productCondition, providerEmail;
    private Button offerExchangeButton, ExitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);

        // Initialize views
        productName = findViewById(R.id.productName);
        productCategory = findViewById(R.id.productCategory);
        productLocation = findViewById(R.id.productLocation);
        productDescription = findViewById(R.id.productDetails);
        productCondition = findViewById(R.id.productCondition);
        offerExchangeButton = findViewById(R.id.OfferExchangeButton);
        ExitButton = findViewById(R.id.ExitButton);
        providerEmail = findViewById(R.id.providerEmail);

        // Get the product passed from the adapter
        Product product = (Product) getIntent().getSerializableExtra("Product");


        // Display product details
        if (product != null) {
            productName.setText(product.getName());
            productCategory.setText("Category: " + product.getCategory());
            productLocation.setText("Location: " + product.getLocation());
            productDescription.setText("Description: " + product.getDescription());
            productCondition.setText("Condition: " + product.getCondition());
            providerEmail.setText("Email: " + product.getEmail());
        }

        // Set up the "Offer Exchange" button
        offerExchangeButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsActivity.this, SubmitForm.class);
            intent.putExtra("Product", product);
            startActivity(intent);
        });
        ExitButton.setOnClickListener(v -> {
            finish();
        });
    }
}
