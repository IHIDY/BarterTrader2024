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

    public UserCRUD() {
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    // Interface for callbacks
    public interface LoginCallback {
        void onSuccess(String role);
        void onFailure(String message);
    }
    // Interface for callback on user registration result
    public interface RegisterCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    // Login Method
    public void loginUser(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
    private void getUserDetails(String email, LoginCallback callback) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailure("Database error: " + databaseError.getMessage());
                    }
                });
    }

    // Save user details to the database
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
