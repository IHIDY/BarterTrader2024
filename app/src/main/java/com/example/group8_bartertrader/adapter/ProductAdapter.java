package com.example.group8_bartertrader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.DetailsActivity;
import com.example.group8_bartertrader.GoogleMapActivity;
import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductListener mOnProductListener;

    // Constructor to pass the listener

    /**
     * product constructor
     * @param productList
     * @param onProductListener
     */
    public ProductAdapter(List<Product> productList, OnProductListener onProductListener) {
        this.productList = productList;
        this.mOnProductListener = onProductListener;
    }


    /**
     * create view holder
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the product_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view, mOnProductListener);
    }

    /**
     * bind view holder
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the product at the given position
        Product product = productList.get(position);

        // Set the product data to the view components
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productCategory.setText("Category: " + product.getCategory());
        holder.productLocation.setText("Location: " + product.getLocation());

        // Set availability status
        boolean availability = product.isAvailable();
        holder.productAvailability.setText("Status: " + (availability ? "Not Available" : "Available"));

        // Set up button click listeners
        holder.mapButton.setOnClickListener(v -> {
            Context context = v.getContext();
            String location = product.getLocation();
            if (location == null || location.isEmpty()) {
                Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent mapIntent = new Intent(context, GoogleMapActivity.class);
            mapIntent.putExtra("itemLocation", location.trim());
            context.startActivity(mapIntent);
        });

        // Setting up the "View Details" button
        setupDetailButton(holder.detailButton, product);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(currentUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                /**
                 * on data change
                 * @param snapshot The current data at the location
                 */
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String role = snapshot.child("role").getValue(String.class);
                    if ("Provider".equals(role)) {
                        holder.editButton.setVisibility(View.VISIBLE);
                        holder.deleteButton.setVisibility(View.VISIBLE);
                        holder.editButton.setOnClickListener(v -> mOnProductListener.onEditClick(product));
                        holder.deleteButton.setOnClickListener(v -> mOnProductListener.onDeleteClick(product));
                    } else {
                        holder.editButton.setVisibility(View.GONE);
                        holder.deleteButton.setVisibility(View.GONE);
                    }
                }

                /**
                 * when cancelled
                 * @param error A description of the error that occurred
                 */
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    holder.editButton.setVisibility(View.GONE);
                    holder.deleteButton.setVisibility(View.GONE);
                }
            });
        }

        // Set up Edit and Delete buttons
        holder.editButton.setOnClickListener(v -> mOnProductListener.onEditClick(product));
        holder.deleteButton.setOnClickListener(v -> mOnProductListener.onDeleteClick(product));
    }

    /**
     * detail button setter
     * @param detailButton
     * @param product
     */
    private void setupDetailButton(Button detailButton, Product product) {
        detailButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent detailIntent = new Intent(context, DetailsActivity.class);
            detailIntent.putExtra("Product", product);
            context.startActivity(detailIntent);
        });
    }

    /**
     * item count getter
     * @return
     */
    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * sets the product list
     * @param productList
     */
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged(); // Notify adapter when data changes
    }

    // Interface to handle edit and delete actions

    /**
     * interface for editing and deleting actions
     */
    public interface OnProductListener {
        /**
         * editing
         * @param product
         */
        void onEditClick(Product product);  // For editing

        /**
         * deleting
         * @param product
         */
        void onDeleteClick(Product product);  // For deleting
    }

    // ViewHolder class to hold references to views in the layout

    /**
     * product view holder
     */
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productCategory, productLocation, productAvailability;
        Button mapButton, detailButton, editButton, deleteButton;

        /**
         * product view holder
         * @param itemView
         * @param onProductListener
         */
        public ProductViewHolder(View itemView, OnProductListener onProductListener) {
            super(itemView);

            // Initialize the views
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productCategory = itemView.findViewById(R.id.productCategory);
            productLocation = itemView.findViewById(R.id.productLocation);
            productAvailability = itemView.findViewById(R.id.productAvailability);
            mapButton = itemView.findViewById(R.id.mapButton);
            detailButton = itemView.findViewById(R.id.detailButton);
            editButton = itemView.findViewById(R.id.editButton);  // Edit button
            deleteButton = itemView.findViewById(R.id.deleteButton);  // Delete button
        }
    }
}
