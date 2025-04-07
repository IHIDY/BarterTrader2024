package com.example.group8_bartertrader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.ChatActivity;
import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Offer;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private Context context;

    private List<Offer> offerList;

    /**
     * adapter for offer
     * @param offerList
     * @param context
     */
    public OfferAdapter(List<Offer> offerList, Context context) {
        this.context = context;
        this.offerList = offerList;
    }

    /**
     * created view
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the offer_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new OfferViewHolder(view);
    }

    /**
     * view holder
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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

        if ("accepted".equalsIgnoreCase(offer.getStatus())) {
            holder.chatButton.setEnabled(true);
            holder.chatButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("offerId", offer.getId());
                intent.putExtra("currentUserEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                context.startActivity(intent);
            });
        } else {
            holder.chatButton.setEnabled(false);
        }
    }

    /**
     * item count getter
     * @return
     */
    @Override
    public int getItemCount() {
        return offerList.size();
    }
    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView offeredItemName, offeredItemDescription, offeredItemCategory, offeredItemLocation;
        TextView targetItemName, targetItemDescription, targetItemCategory, targetItemLocation;
        TextView status;
        Button chatButton;

        /**
         * holds offer view
         * @param itemView
         */
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

            chatButton = itemView.findViewById(R.id.chatButton);
        }
    }
}
