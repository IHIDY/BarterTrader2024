package com.example.group8_bartertrader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private List<Offer> offerList;

    public OfferAdapter(List<Offer> offerList) {
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the offer_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);

        holder.offeredItemName.setText(offer.getOfferedItemName());
        holder.offeredItemDescription.setText(offer.getOfferedItemDescription());
        holder.offeredItemCategory.setText("Category: " + offer.getOfferedItemCategory());
        holder.offeredItemLocation.setText("Location: " + offer.getOfferedItemLocation());

        holder.targetItemName.setText(offer.getTargetItemName());
        holder.targetItemDescription.setText(offer.getTargetItemDescription());
        holder.targetItemCategory.setText("Category: " + offer.getTargetItemCategory());
        holder.targetItemLocation.setText("Location: " + offer.getTargetItemLocation());

        holder.status.setText("Status: " + offer.getStatus());
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView offeredItemName, offeredItemDescription, offeredItemCategory, offeredItemLocation;
        TextView targetItemName, targetItemDescription, targetItemCategory, targetItemLocation;
        TextView status;

        public OfferViewHolder(View itemView) {
            super(itemView);
            offeredItemName = itemView.findViewById(R.id.productName);
            offeredItemDescription = itemView.findViewById(R.id.productDescription);
            offeredItemCategory = itemView.findViewById(R.id.productCategory);
            offeredItemLocation = itemView.findViewById(R.id.productLocation);

            targetItemName = itemView.findViewById(R.id.targetProductName);
            targetItemDescription = itemView.findViewById(R.id.targetProductDescription);
            targetItemCategory = itemView.findViewById(R.id.targetProductCategory);
            targetItemLocation = itemView.findViewById(R.id.targetProductLocation);

            status = itemView.findViewById(R.id.status);
        }
    }
}
