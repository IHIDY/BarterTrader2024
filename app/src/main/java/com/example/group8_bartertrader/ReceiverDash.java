package com.example.group8_bartertrader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReceiverDash extends AppCompatActivity implements LocationHelper.OnLocationFetchListener {

    public Context context;
    Button receiverSettingBtn;
    TextView locationTextView;
    RecyclerView productRecyclerView;
    LocationHelper locationHelper;
    DatabaseReference productsRef;
    List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiver_dash);

        receiverSettingBtn = findViewById(R.id.recsettingbutton);
        locationTextView = findViewById(R.id.locationTextView);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        locationHelper = new LocationHelper(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationHelper.getCurrentLocation(this);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set onclick for settings button
        receiverSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "LOGIN BUTTON CLICKED");
                Intent intent = new Intent(ReceiverDash.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

//permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationFetched(double latitude, double longitude) {
        String cityName = LocationHelper.getCityName(latitude, longitude);
        locationTextView.setText("Location: " + cityName);
        fetchProductsFromFirebase(latitude, longitude);
    }

    @Override
    public void onLocationFetchFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }



    void fetchProductsFromFirebase(double latitude, double longitude) {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        double[] productLatLng = LocationHelper.parseLatLngLocation(product.getLatLngLocation());
                        if (productLatLng != null && LocationHelper.isWithinRange(latitude, longitude, productLatLng[0], productLatLng[1])) {
                            productList.add(product);
                        }
                    }
                }
                updateProductList(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ReceiverDash", "loadProducts:onCancelled", databaseError.toException());
                Toast.makeText(ReceiverDash.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductList(List<Product> productList) {
        productAdapter.setProductList(productList);
        productAdapter.notifyDataSetChanged();
    }
}