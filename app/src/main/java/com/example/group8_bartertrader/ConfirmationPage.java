package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmationPage extends AppCompatActivity {

    private EditText emailEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation_page);

        Log.d("APP_LIFECYCLE", "Activity Created");

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Button confirmBtn = findViewById(R.id.confirmEmailBtn);
        emailEditText = findViewById(R.id.confirmEmailTextView);

        Log.d("UI_SETUP", "UI components initialized");

        // Handle Confirm Button Click
        confirmBtn.setOnClickListener(v -> {
            Log.d("BUTTON_CLICK", "Confirm button clicked");
            confirmRoleSelection();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("UI_SETUP", "Activity setup completed");
    }

    private void confirmRoleSelection() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Log.w("INPUT_VALIDATION", "Email field is empty");
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("DATABASE_QUERY", "Checking if email exists in database: " + email);

        // Query the Firebase Realtime Database to check if the email exists
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String currentRole = userSnapshot.child("role").getValue(String.class);
                                String newRole = (currentRole != null && currentRole.equals("Provider")) ? "Receiver" : "Provider";
                                Log.d("DATABASE_QUERY", "Email found. Current role: " + currentRole + ", Switching to: " + newRole);
                                updateRole(userSnapshot.getKey(), newRole);
                                return;
                            }
                        } else {
                            Log.w("DATABASE_QUERY", "Email not found in database: " + email);
                            Toast.makeText(ConfirmationPage.this, "Email not found in database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DATABASE_ERROR", "Error retrieving email: ", databaseError.toException());
                        Toast.makeText(ConfirmationPage.this, "Error checking email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateRole(String userId, String newRole) {
        Log.d("DATABASE_UPDATE", "Updating role for user ID: " + userId + " to " + newRole);

        databaseReference.child(userId).child("role").setValue(newRole)
                .addOnSuccessListener(aVoid -> {
                    Log.d("DATABASE_UPDATE", "Role updated successfully to: " + newRole);
                    Toast.makeText(this, "Role updated to " + newRole + "!", Toast.LENGTH_SHORT).show();

                    // Redirect user based on their new role
                    if (newRole.equals("Provider")) {
                        Log.d("NAVIGATION", "Redirecting to ProviderDash");
                        startActivity(new Intent(ConfirmationPage.this, ProviderDash.class));
                    } else {
                        Log.d("NAVIGATION", "Redirecting to ReceiverDash");
                        startActivity(new Intent(ConfirmationPage.this, ReceiverDash.class));
                    }

                    finish(); // Close the current activity
                })
                .addOnFailureListener(e -> {
                    Log.e("DATABASE_UPDATE", "Failed to update role: ", e);
                    Toast.makeText(this, "Error updating role", Toast.LENGTH_SHORT).show();
                });
    }
}
