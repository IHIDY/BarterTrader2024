package com.example.group8_bartertrader.provider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProviderProductList extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference productsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_product_list);

        // Initialize Firebase Authentication
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize Firebase Database Reference
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        // Initialize RecyclerView and Adapter
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setAdapter(productAdapter);

        // Fetch products from Firebase
        fetchProductsFromFirebase();
    }

    private void fetchProductsFromFirebase() {
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserEmail = currentUser.getEmail();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null && currentUserEmail != null && currentUserEmail.equals(product.getEmail())) {
                        Log.d("Product fetched", "ID: " + product.getId() + ", Availability: " + product.isAvailable() + ", Email: " + product.getEmail());
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ProviderProductList", "loadProducts:onCancelled", databaseError.toException());
                Toast.makeText(ProviderProductList.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
