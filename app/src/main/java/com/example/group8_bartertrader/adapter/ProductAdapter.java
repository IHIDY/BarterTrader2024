package com.example.group8_bartertrader.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    // Constructor to pass the product list
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the product_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the product at the given position
        Product product = productList.get(position);

        // Set the product data to the view components
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productCategory.setText("Category: " + product.getCategory());
        holder.productLocation.setText("Location: " + product.getLocation());

        // You can also add logic for availability if needed
        String availability = product.isAvailable();
//        holder.productAvailability.setText("Status: " + availability);
        holder.productAvailability.setText("Status: " + "Available");

        // Log the isAvailable value
        Log.d("From Product Adapter", "Product ID: " + product.getId() + ", Availability: " + product.isAvailable());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class to hold references to views in the layout
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productCategory, productLocation, productAvailability;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productCategory = itemView.findViewById(R.id.productCategory);
            productLocation = itemView.findViewById(R.id.productLocation);
            productAvailability = itemView.findViewById(R.id.productAvailability);
        }
    }
}
