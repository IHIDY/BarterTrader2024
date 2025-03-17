package com.example.group8_bartertrader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class ReceiverDash extends AppCompatActivity implements LocationHelper.OnLocationFetchListener {

    private Button receiverSettingBtn;
    private Button myOffersBtn;

    private TextView locationTextView;
    private LocationHelper locationHelper;
    private DatabaseReference productsRef;
    private List<Product> productList;
    private Spinner category;
    private Button searchButton;
    private EditText keyword,Distance;
    private int distance;
    private final int RADIUS = 10;
    private final int Radius_E = 6371; // Radius of the Earth in km
    private String selectedCategory;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiver_dash);

        receiverSettingBtn = findViewById(R.id.recsettingbutton);
        myOffersBtn = findViewById(R.id.myOffersButton);
        locationTextView = findViewById(R.id.locationTextView);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        category = findViewById(R.id.filterSpinner);
        keyword = findViewById(R.id.inputEditText);
        Distance = findViewById(R.id.distanceEditText);
        searchButton = findViewById(R.id.filterButton);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);


        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        locationHelper = new LocationHelper(this);

        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationHelper.getCurrentLocation(this);
        }

        fetchProductsFromFirebase(RADIUS,null,"");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        receiverSettingBtn.setOnClickListener(v -> {
            Log.d("TEST", "GO TO SETTINGS BUTTON CLICKED");
            startActivity(new Intent(ReceiverDash.this, SettingsActivity.class));
        });

        myOffersBtn.setOnClickListener(v -> {
            Log.d("TEST", "MY OFFERS BUTTON CLICKED");
            startActivity(new Intent(ReceiverDash.this, MyOffersActivity.class));
        });

        String[] categories = {"Select Category", "Electronics", "Furniture", "Clothing & Accessories", "Books", "Toys & Games",
                "Sports & Outdoors", "Baby & Kids", "Home Improvement", "Vehicles", "Health & Beauty",
                "Pet Supplies", "Collectibles", "Home Decor", "Office Supplies"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        category.setAdapter(adapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               selectedCategory = parentView.getItemAtPosition(position).toString();
           }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedCategory = null;
            }
        });

        searchButton.setOnClickListener(v -> search());

    }


    public void search(){
        String word = "";
        try {
            word = keyword.getText().toString().trim();
            String temp = Distance.getText().toString().trim();
            if(!temp.equals("")){
                distance = Integer.parseInt(temp);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this,"Must be number "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        if(distance<=0){
            distance=RADIUS;
        }
        fetchProductsFromFirebase(distance,selectedCategory,word);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onLocationFetched(double latitude, double longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
        locationTextView.setText("Lat: " + latitude + ", Long: " + longitude);
        fetchProductsFromFirebase(RADIUS,null,"");
    }

    @Override
    public void onLocationFetchFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void fetchProductsFromFirebase(int distance,String category,String word) {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    String categoryOfP = product.getCategory();
                    String name = product.getName();
                    if (product != null) {
                        // Parse latLngLocation from the product
                        double[] productLatLng = LocationHelper.parseLatLngLocation(product.getLatLngLocation());
                        if(category==null||category.equals("Select Category")){
                            if (productLatLng != null && isWithinRange(latitude, longitude, productLatLng[0], productLatLng[1],distance)&&name.contains(word)) {
                                productList.add(product);
                            }
                        }
                        else {
                            if (productLatLng != null && isWithinRange(latitude, longitude, productLatLng[0], productLatLng[1],distance)&&categoryOfP.equals(category)&&name.contains(word)) {
                                productList.add(product);
                            }
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

    private boolean isWithinRange(double userLat, double userLong, double productLat, double productLong,int maxDistance) {
        double distance = calculateDistance(userLat, userLong, productLat, productLong);
        return distance <= maxDistance;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate distance between two coordinates
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Radius_E * c;
    }

    private void updateProductList(List<Product> productList) {
        // Update the UI with the filtered product list
        // use a RecyclerView to display the products
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setAdapter(productAdapter);
    }
}