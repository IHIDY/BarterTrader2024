package com.example.group8_bartertrader;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;

public class DetailsActivity extends AppCompatActivity {
    private TextView productName, productCondition, productCategory, productLocation, productDetails;
    private Button offerExchangeButton, exitButton;
    public Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        productName = findViewById(R.id.productName);
        productCondition = findViewById(R.id.productCondition);
        productCategory = findViewById(R.id.productCategory);
        productLocation = findViewById(R.id.productLocation);
        productDetails = findViewById(R.id.productDetails);

        offerExchangeButton = findViewById(R.id.OfferExchangeButton);
        exitButton = findViewById(R.id.ExitButton);
    }

    protected Product getSelectedProduct() {}

    public void showProductDetails(Product product) {}

    protected void showProductName(String name) {}

    protected void showProductCondition(String condition) {}

    protected void showProductCategory(String category) {}

    protected void showProductLocation(String location) {}

    protected void showProductDetails(String detials) {}

    protected void setupOfferExchangeButton() {}

    protected void setupExitButton() {}

    protected void setupMapButton() {}

    protected void move2OfferExchange() {}

    protected void move2RecieverDash() {}

    protected void showOnGoogleMap(String location) {}
}
