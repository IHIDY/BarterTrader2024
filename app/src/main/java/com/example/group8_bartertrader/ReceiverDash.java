package com.example.group8_bartertrader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.group8_bartertrader.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReceiverDash extends AppCompatActivity implements LocationHelper.OnLocationFetchListener {

    private Button receiverSettingBtn;
    private Button myOffersBtn;

    private TextView locationTextView;
    private LocationHelper locationHelper;
    private DatabaseReference productsRef;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiver_dash);

        receiverSettingBtn = findViewById(R.id.recsettingbutton);
        myOffersBtn = findViewById(R.id.myOffersButton);
        locationTextView = findViewById(R.id.locationTextView);

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

        receiverSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "GO TO SETTINGS BUTTON CLICKED");
                Intent intent = new Intent(ReceiverDash.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        myOffersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "MY OFFERS BUTTON CLICKED");
                Intent intent = new Intent(ReceiverDash.this, MyOffersActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationFetched(double latitude, double longitude) {
        locationTextView.setText("Lat: " + latitude + ", Long: " + longitude);
        fetchProductsFromFirebase(latitude, longitude);
    }

    @Override
    public void onLocationFetchFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void fetchProductsFromFirebase(double latitude, double longitude) {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        // Parse latLngLocation from the product
                        double[] productLatLng = LocationHelper.parseLatLngLocation(product.getLatLngLocation());
                        if (productLatLng != null && isWithinRange(latitude, longitude, productLatLng[0], productLatLng[1])) {
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

    private boolean isWithinRange(double userLat, double userLong, double productLat, double productLong) {
        final int RADIUS = 10;
        double distance = calculateDistance(userLat, userLong, productLat, productLong);
        return distance <= RADIUS;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate distance between two coordinates
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void updateProductList(List<Product> productList) {
        // Update the UI with the filtered product list
        // use a RecyclerView to display the products
    }
}