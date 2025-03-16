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
    private AtomicBoolean result = new AtomicBoolean(false);

    public SubmitHelper(DatabaseReference databaseReference, FirebaseAuth mAuth) {
        this.databaseReference = databaseReference;
        this.mAuth = mAuth;
    }

    public AtomicBoolean submitOffer(String name, String category, String description, String location, String currentUserEmail,
                            String targetProductId, String targetProductName, String targetProductCategory,
                            String targetProductDescription, String targetProductLocation) {

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location)) {
            Log.e("SubmitHelper", "All fields are required.");
            return result;
        }

        databaseReference.orderByChild("receiverEmail").equalTo(currentUserEmail).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("FirebaseError", "Query failed", task.getException());
                return;
            }

            boolean alreadySubmitted = false;
            if (task.getResult().exists()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String existingTargetId = snapshot.child("targetItemId").getValue(String.class);
                    if (existingTargetId != null && existingTargetId.equals(targetProductId)) {
                        alreadySubmitted = true;
                        break;
                    }
                }
            }

            if (alreadySubmitted) {
                Log.e("SubmitHelper", "Offer already exists for this product.");
                return;
            }

            submitNewOffer(name, category, description, location, currentUserEmail,
                    targetProductId, targetProductName, targetProductCategory, targetProductDescription, targetProductLocation);
        });

        return result;
    }

    private void submitNewOffer(String name, String category, String description, String location, String currentUserEmail,
                                String targetProductId, String targetProductName, String targetProductCategory,
                                String targetProductDescription, String targetProductLocation) {

        String OfferId = databaseReference.push().getKey();
        Map<String, Object> offerData = new HashMap<>();
        offerData.put("receiverEmail", currentUserEmail);
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
                result.set(true);
            } else {
                Log.e("SubmitHelper", "Failed to offer exchange");
            }
        });
    }
}
