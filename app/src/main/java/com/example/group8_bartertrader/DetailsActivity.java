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
        //this.setupMapButton(); check if necessary
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

    protected void showProductName(String name) {}

    protected void showProductCondition(String condition) {}

    protected void showProductCategory(String category) {}

    protected void showProductLocation(String location) {}

    protected void showProductDetails(String detials) {}

    protected void setupOfferExchangeButton() {}

    protected void setupExitButton() {}

    protected void setupMapButton() {} //check if necessary

    protected void move2OfferExchange() {}

    protected void move2RecieverDash() {}

    protected void showOnGoogleMap(String location) {}
}
