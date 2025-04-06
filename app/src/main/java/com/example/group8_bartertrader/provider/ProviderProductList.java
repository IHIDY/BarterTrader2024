package com.example.group8_bartertrader.provider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.ProductForm;
import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProviderProductList extends AppCompatActivity implements ProductAdapter.OnProductListener {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference productsRef;
    private FirebaseUser currentUser;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_product_list);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        productRecyclerView = findViewById(R.id.productRecyclerView);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);
        productRecyclerView.setAdapter(productAdapter);

        fetchProductsFromFirebase();
    }

    private void fetchProductsFromFirebase() {
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserEmail = currentUser.getEmail();
        if (currentUserEmail == null) {
            Toast.makeText(this, "Error fetching user email", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingIndicator.setVisibility(View.VISIBLE);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null && currentUserEmail.equals(product.getEmail())) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
                loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ProviderProductList", "loadProducts:onCancelled", databaseError.toException());
                Toast.makeText(ProviderProductList.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onEditClick(Product product) {
        // Handle Edit action
        Intent intent = new Intent(ProviderProductList.this, ProductForm.class);
        intent.putExtra("productId", product.getId());  // Pass the product ID to the edit activity
        startActivity(intent);
    }


    @Override
    public void onDeleteClick(Product product) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you would like to delete?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    productsRef.child(product.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                                fetchProductsFromFirebase();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
