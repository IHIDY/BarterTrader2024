package com.example.group8_bartertrader.adapter;

import android.content.Context;
import android.content.Intent;
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
        holder.productName.setText(offer.getOfferedItemName());
        holder.targetProductName.setText(offer.getTargetItemName());
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
        TextView productName, targetProductName, status;
        public ReceivedOfferViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            targetProductName = itemView.findViewById(R.id.targetProductName);
            status = itemView.findViewById(R.id.status);
        }
    }
}
