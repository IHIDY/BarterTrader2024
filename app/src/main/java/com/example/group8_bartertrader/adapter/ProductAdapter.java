package com.example.group8_bartertrader.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.GoogleMapActivity;
import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the product_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_details, parent, false);
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
        boolean availability = product.isAvailable();
//        holder.productAvailability.setText("Status: " + availability);
        holder.productAvailability.setText("Status: " + "Available");

//        holder.mapButton.setOnClickListener(v -> {
//            Context context = v.getContext();
//            Intent mapIntent = new Intent(context, GoogleMapActivity.class);
//            mapIntent.putExtra("itemLocation", product.getLocation());
//            context.startActivity(mapIntent);
//        });
//
//        holder.detailButton.setOnClickListener(v -> {
//            Context context = v.getContext();
//            Intent detailIntent = new Intent(context, DetailsActivity.class);
//            detailIntent.putExtra("Product",product);
//            context.startActivity(detailIntent);
//        });
        // Log the isAvailable value
        Log.d("From Product Adapter", "Product ID: " + product.getId() + ", Availability: " + product.isAvailable());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    // ViewHolder class to hold references to views in the layout
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productCategory, productLocation, productAvailability;
        Button mapButton, detailButton;
        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productCategory = itemView.findViewById(R.id.productCategory);
            productLocation = itemView.findViewById(R.id.productLocation);
            productAvailability = itemView.findViewById(R.id.productAvailability);
//            mapButton = itemView.findViewById(R.id.mapButton);
//            detailButton = itemView.findViewById(R.id.detailButton);
        }
    }
}
