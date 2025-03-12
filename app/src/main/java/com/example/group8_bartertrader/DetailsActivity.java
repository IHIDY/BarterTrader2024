package com.example.group8_bartertrader;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;

public class DetailsActivity extends AppCompatActivity {
    public Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        this.selectedProduct = getSelectedProduct();
        this.showProductDetails(this.selectedProduct);
        this.setupOfferExchangeButton();
        this.setupExitButton();
    }

    protected Product getSelectedProduct() {}

    public void showProductDetails(Product product) {
        this.showProductName(product.getName());
        this.showProductCondition(product.getCondition());
        this.showProductCategory(product.getCategory());
        this.showProductLocation(product.getLocation());
        this.showProductDetails(product.getDescription());
    }

    protected void showProductName(String name) {
        TextView productName = findViewById(R.id.productName);
        productName.setText(name);
    }

    protected void showProductCondition(String condition) {
        TextView productCondition = findViewById(R.id.productCondition);
        productCondition.setText(condition);
    }

    protected void showProductCategory(String category) {
        TextView productCategory = findViewById(R.id.productCategory);
        productCategory.setText(category);
    }

    protected void showProductLocation(String location) {
        TextView productLocation = findViewById(R.id.productLocation);
        productLocation.setText(location);
    }

    protected void showProductDetails(String details) {
        TextView productDetails = findViewById(R.id.productDetails);
        productDetails.setText(details);
    }

    protected void setupOfferExchangeButton() {
        Button offerExchangeButton = findViewById(R.id.OfferExchangeButton);
        offerExchangeButton.setOnClickListener(view -> move2OfferExchange());
    }

    protected void setupExitButton() {
        Button exitButton = findViewById(R.id.ExitButton);
        exitButton.setOnClickListener(view -> move2RecieverDash());
    }

    protected void move2OfferExchange() {}

    protected void move2RecieverDash() {}
}
