package com.example.group8_bartertrader;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SubmitHelper {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public SubmitHelper(DatabaseReference databaseReference, FirebaseAuth mAuth) {
        this.databaseReference = databaseReference;
        this.mAuth = mAuth;
    }

    public void submitOffer(String name, String category, String description, String location, String currentUserEmail,
                            String targetProductId, String targetProductName, String targetProductCategory,
                            String targetProductDescription, String targetProductLocation, String targetProviderEmail, SubmitCallback callback) {

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location)) {
            Log.e("SubmitHelper", "All fields are required.");
            return;
        }

        databaseReference.orderByChild("receiverEmail").equalTo(currentUserEmail).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("FirebaseError", "Query failed", task.getException());
                callback.onSubmissionResult(false);
                return;
            }

            boolean alreadySubmitted = false;
            if (task.getResult().exists()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String existingTargetId = snapshot.child("targetItemId").getValue(String.class);
                    String existingReceiverEmail = snapshot.child("receiverEmail").getValue(String.class);

                    // Check the receiverEmail and targetItemId
                    if (existingTargetId != null && existingTargetId.equals(targetProductId) &&
                            existingReceiverEmail != null && existingReceiverEmail.equals(currentUserEmail)) {
                        alreadySubmitted = true;
                        break;
                    }
                }
            }

            if (alreadySubmitted) {
                Log.e("SubmitHelper", "Offer already exists for this product.");
                callback.onSubmissionResult(false);
                return;
            }

            submitNewOffer(name, category, description, location, currentUserEmail,
                    targetProductId, targetProductName, targetProductCategory, targetProductDescription, targetProductLocation, targetProviderEmail, callback);
        });
    }

    private void submitNewOffer(String name, String category, String description, String location, String currentUserEmail,
                                String targetProductId, String targetProductName, String targetProductCategory,
                                String targetProductDescription, String targetProductLocation, String targetProviderEmail, SubmitCallback callback) {

        String OfferId = databaseReference.push().getKey();
        Map<String, Object> offerData = new HashMap<>();
        offerData.put("receiverEmail", currentUserEmail);
        offerData.put("providerEmail", targetProviderEmail);
        offerData.put("offeredItemName", name);
        offerData.put("offeredItemCategory", category);
        offerData.put("offeredItemLocation", location);
        offerData.put("offeredItemDescription", description);
        offerData.put("targetItemId", targetProductId);
        offerData.put("targetItemName", targetProductName);
        offerData.put("targetItemCategory", targetProductCategory);
        offerData.put("targetItemLocation", targetProductLocation);
        offerData.put("targetItemDescription", targetProductDescription);
        offerData.put("status", "pending");

        databaseReference.child(OfferId).setValue(offerData).addOnCompleteListener(submitTask -> {
            if (submitTask.isSuccessful()) {
                Log.d("SubmitHelper", "Product offered successfully");
                callback.onSubmissionResult(true);
            } else {
                Log.e("SubmitHelper", "Failed to offer exchange");
                callback.onSubmissionResult(false);
            }
        });
    }

    public interface SubmitCallback {
        void onSubmissionResult(boolean success);
    }

}
