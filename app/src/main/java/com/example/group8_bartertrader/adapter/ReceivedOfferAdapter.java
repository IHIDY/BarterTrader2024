package com.example.group8_bartertrader.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.OfferDetailsActivity;
import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Offer;

import java.util.List;

public class ReceivedOfferAdapter extends RecyclerView.Adapter<ReceivedOfferAdapter.ReceivedOfferViewHolder> {
    private List<Offer> offerList;
    private Context context;

    public ReceivedOfferAdapter(List<Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReceivedOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_offer_item, parent, false);
        return new ReceivedOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedOfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.status.setText("Status: " + offer.getStatus());

        Log.d("DEBUG_ADAPTER", "Bind offer: " + offer.getOfferedItemName());
        Log.d("DEBUG_ADAPTER", "Location: " + offer.getOfferedItemLocation());

        holder.productName.setText("I Offer: " + offer.getOfferedItemName());
        holder.productCategory.setText("Category: " + (offer.getOfferedItemCategory()));
        holder.productLocation.setText("Location: " + (offer.getOfferedItemLocation()));
        holder.productDescription.setText("Description: " + (offer.getOfferedItemDescription()));

        holder.targetItemName.setText("For: " + offer.getTargetItemName());
        holder.targetItemDescription.setText("Description: " + (offer.getTargetItemDescription()));
        holder.targetItemCategory.setText("Category: " + (offer.getTargetItemCategory()));
        holder.targetItemLocation.setText("Location: " +(offer.getTargetItemLocation()));
        holder.status.setText("Status: " + offer.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OfferDetailsActivity.class);
            intent.putExtra("offerId", offer.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class ReceivedOfferViewHolder extends RecyclerView.ViewHolder {
        TextView productName, status, productCategory, productLocation, productDescription, targetItemName, targetItemDescription, targetItemCategory, targetItemLocation;
        public ReceivedOfferViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productLocation = itemView.findViewById(R.id.productLocation);
            productDescription = itemView.findViewById(R.id.productDescription);

            targetItemName = itemView.findViewById(R.id.targetProductName);
            targetItemDescription = itemView.findViewById(R.id.targetProductDescription);
            targetItemCategory = itemView.findViewById(R.id.targetProductCategory);
            targetItemLocation = itemView.findViewById(R.id.targetProductLocation);
            status = itemView.findViewById(R.id.status);
        }
    }
}
