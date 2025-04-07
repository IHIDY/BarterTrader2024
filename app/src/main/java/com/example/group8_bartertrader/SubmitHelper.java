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
/**
 * Helper class for submitting offers to the Firebase database.
 */
public class SubmitHelper {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    /**
     * Constructor to initialize the SubmitHelper with a database reference and FirebaseAuth instance.
     *
     * @param databaseReference The reference to the Firebase database.
     * @param mAuth The Firebase authentication instance.
     */
    public SubmitHelper(DatabaseReference databaseReference, FirebaseAuth mAuth) {
        this.databaseReference = databaseReference;
        this.mAuth = mAuth;
    }
    /**
     * Submits a new offer if all fields are valid and the offer doesn't already exist.
     *
     * @param name The name of the offered product.
     * @param category The category of the offered product.
     * @param description The description of the offered product.
     * @param location The location of the offered product.
     * @param currentUserEmail The email of the current user submitting the offer.
     * @param targetProductId The ID of the target product.
     * @param targetProductName The name of the target product.
     * @param targetProductCategory The category of the target product.
     * @param targetProductDescription The description of the target product.
     * @param targetProductLocation The location of the target product.
     * @param targetProviderEmail The email of the product provider.
     * @param callback The callback to handle the result of the offer submission.
     */
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
    /**
     * Submits a new offer to the Firebase database.
     *
     * @param name The name of the offered product.
     * @param category The category of the offered product.
     * @param description The description of the offered product.
     * @param location The location of the offered product.
     * @param currentUserEmail The email of the current user submitting the offer.
     * @param targetProductId The ID of the target product.
     * @param targetProductName The name of the target product.
     * @param targetProductCategory The category of the target product.
     * @param targetProductDescription The description of the target product.
     * @param targetProductLocation The location of the target product.
     * @param targetProviderEmail The email of the product provider.
     * @param callback The callback to handle the result of the offer submission.
     */
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

    /**
     * Interface for handling the result of the offer submission.
     */
    public interface SubmitCallback {
        void onSubmissionResult(boolean success);
    }

}
