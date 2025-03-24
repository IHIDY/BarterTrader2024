package com.example.group8_bartertrader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SearchProductActivity extends AppCompatActivity {
    private Spinner category;
    private Button searchButton;
    private EditText keyword, Distance;
    private String selectedCategory;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
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

        fetchProductsFromFirebase("Halifax",null,"");
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

    public void search() {
        String word = "";
        String temp = "";
        try {
            word = keyword.getText().toString().trim();
            temp = Distance.getText().toString().trim();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Must be number " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        fetchProductsFromFirebase(temp, selectedCategory, word);
    }

    private void fetchProductsFromFirebase(String locations,String category,String word) {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    String categoryOfP = product.getCategory();
                    String name = product.getName();
                    String location = product.getLocation();
                    if (product != null) {
                        // Parse latLngLocation from the product
//                        double[] productLatLng = LocationHelper.parseLatLngLocation(product.getLatLngLocation());

                        // This is jst dummy, until it is fixed
                        double[] productLatLng = {44.6480, -63.5750};

                        if(category==null||category.equals("Select Category")&&location.contains(locations)){
                            if (productLatLng != null && name.contains(word)) {
                                productList.add(product);
                            }
                        }
                        else {
                            if (productLatLng != null && categoryOfP.equals(category)&&name.contains(word)&&location.contains(locations)) {
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
                Toast.makeText(SearchProductActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
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