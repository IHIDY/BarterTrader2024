package com.example.group8_bartertrader.CRUD;

import androidx.annotation.NonNull;

import com.example.group8_bartertrader.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserCRUD {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    /**
     * constructor for user
     */
    public UserCRUD() {
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    // Interface for callbacks

    /**
     * interface for callbacks
     */
    public interface LoginCallback {
        /**
         * when successful
         * @param role
         */
        void onSuccess(String role);

        /**
         * when failed
         * @param message
         */
        void onFailure(String message);
    }
    // Interface for callback on user registration result

    /**
     * interface for registration result
     */
    public interface RegisterCallback {
        /**
         * on success
         * @param message
         */
        void onSuccess(String message);

        /**
         * on failure
         * @param error
         */
        void onFailure(String error);
    }

    // Login Method

    /**
     * logs in the user
     * @param email
     * @param password
     * @param callback
     */
    public void loginUser(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    /**
                     * when complete
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user == null) {
                                callback.onFailure("User authentication failed.");
                                return;
                            }
                            getUserDetails(user.getEmail(), callback);
                        } else {
                            callback.onFailure("Authentication failed.");
                        }
                    }
                });
    }

    // Retrieve User Details

    /**
     * gets the user details
     * @param email
     * @param callback
     */
    private void getUserDetails(String email, LoginCallback callback) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    /**
                     * when data changes
                     * @param dataSnapshot The current data at the location
                     */
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String role = snapshot.child("role").getValue(String.class);
                                callback.onSuccess(role);
                            }
                        } else {
                            callback.onFailure("User data not found.");
                        }
                    }

                    /**
                     * when cancelled
                     * @param databaseError A description of the error that occurred
                     */
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailure("Database error: " + databaseError.getMessage());
                    }
                });
    }

    // Save user details to the database

    /**
     * saves the user to the database
      * @param uid
     * @param email
     * @param password
     * @param role
     * @param fname
     * @param lname
     * @param callback
     */
    private void saveUserToDatabase(String uid, String email, String password, String role, String fname, String lname, final RegisterCallback callback) {
        User user = new User(email, password, role, fname, lname);
        databaseReference.child(uid).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("User data saved to database!");
                    } else {
                        callback.onFailure("Failed to save user data.");
                    }
                });
    }

    // Register and save user

    /**
     * registers the user
     * @param email
     * @param password
     * @param role
     * @param fname
     * @param lname
     * @param callback
     */
    public void registerUser(String email, String password, String role, String fname, String lname, final RegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            saveUserToDatabase(uid, email, password, role, fname, lname, callback);
                        } else {
                            callback.onFailure("Registration Failed! - User already exists!");
                        }
                    }
                });
    }
}
