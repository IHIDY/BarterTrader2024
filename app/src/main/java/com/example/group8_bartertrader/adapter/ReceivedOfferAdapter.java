package com.example.group8_bartertrader.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.OfferDetailsActivity;
import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Offer;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class ReceivedOfferAdapter extends RecyclerView.Adapter<ReceivedOfferAdapter.ReceivedOfferViewHolder> {
    private List<Offer> offerList;
    private Context context;
    private DatabaseReference offers;

    public ReceivedOfferAdapter(List<Offer> offerList, Context context){
        this(offerList, context, FirebaseDatabase.getInstance().getReference("Offers"));
    }
    public ReceivedOfferAdapter(List<Offer> offerList, Context context, DatabaseReference db) {
        this.offerList = offerList;
        this.context = context;
        this.offers = db;
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

        Log.d("DEBUG_ADAPTER", "Bind offer: " + offer.getOfferedItemName());
        holder.status.setText("Status: " + offer.getStatus());
        holder.productName.setText("I Offer: " + offer.getOfferedItemName());
        holder.productCategory.setText("Category: " + (offer.getOfferedItemCategory()));
        holder.productLocation.setText("Location: " + (offer.getOfferedItemLocation()));
        Log.d("DEBUG_ADAPTER", "Location: " + offer.getOfferedItemLocation());
        holder.productDescription.setText("Description: " + (offer.getOfferedItemDescription()));

        holder.targetItemName.setText("For: " + offer.getTargetItemName());
        holder.targetItemDescription.setText("Description: " + (offer.getTargetItemDescription()));
        holder.targetItemCategory.setText("Category: " + (offer.getTargetItemCategory()));
        holder.targetItemLocation.setText("Location: " +(offer.getTargetItemLocation()));
        holder.status.setText("Status: " + offer.getStatus());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.offer_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.respondToOfferSpinner.setAdapter(adapter);
        holder.respondToOfferSpinner.setContentDescription("OfferSpinner-" + position);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OfferDetailsActivity.class);
            intent.putExtra("offerId", offer.getId());
            context.startActivity(intent);
        });

        int defaultPos = adapter.getPosition(offer.getStatus());
        holder.respondToOfferSpinner.setSelection(defaultPos);
        holder.respondToOfferSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                if (!selectedStatus.equals(offer.getStatus())){
                    offer.setStatus(selectedStatus);
                    updateOfferStatus(offer);
                    Toast.makeText(context, "Offer " + selectedStatus, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateOfferStatus(Offer offer) {
        if (offer.getId() != null){
            offers.child(offer.getId()).child("status").setValue(offer.getStatus());
        }
    }

    public void sendNotification(String receiverEmail, String message){
        DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications");
        String notificationId = notification.push().getKey();
        HashMap<String, Object> notificationData = new HashMap<>();
        notificationData.put("receiverEmail", receiverEmail);
        notificationData.put("message", message);
        notificationData.put("time", System.currentTimeMillis());
        if (notificationId != null){
            notification.child(notificationId).setValue(notificationData).addOnCompleteListener(task->{
                if (task.isSuccessful()){
                    Log.d("DEBUG_FIREBASE", "Notification sent: " + receiverEmail);
                }else{
                    Log.e("DEBUG_FIREBASE", "Failed sending notification to receiver", task.getException());
                }
            });
        }

    }
    public void updateOfferStatus(String offerId, String status){
        if (offerId == null || offerId.isEmpty()){
            Log.e("DEBUG_FIREBASE", "OfferId is null");
            Toast.makeText(context, "No offerId found", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference offers = FirebaseDatabase.getInstance().getReference("Offers");

        offers.child(offerId).child("status").setValue(status).addOnCompleteListener(task-> {
            if (task.isSuccessful()) {
                Log.d("DEBUG_FIREBASE", "Offer " + offerId + "updated: " + status);
                Toast.makeText(context, "Offer is: " + status, Toast.LENGTH_SHORT).show();
            } else {
                Log.e("DEBUG_FIREBASE", "Failed updating status", task.getException());
                Toast.makeText(context, "Failed updating offer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class ReceivedOfferViewHolder extends RecyclerView.ViewHolder {
        Spinner respondToOfferSpinner;
        TextView offerId, productName, status, productCategory, productLocation, productDescription, targetItemName, targetItemDescription, targetItemCategory, targetItemLocation;
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

            respondToOfferSpinner = itemView.findViewById(R.id.respondToOfferSpinner);
        }
    }
}
