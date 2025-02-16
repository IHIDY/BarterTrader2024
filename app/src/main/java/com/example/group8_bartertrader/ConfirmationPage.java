package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.Arrays;
import java.util.List;

public class ConfirmationPage extends AppCompatActivity {

    private Spinner roleSpinner;
    private String selectedRole = "";
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
        roleSpinner = findViewById(R.id.roleSelectSpinner);
        emailEditText = findViewById(R.id.confirmEmailTextView);

        Log.d("UI_SETUP", "UI components initialized");

        // Define role options
        List<String> roleOptions = Arrays.asList("Select your role", "Provider", "Receiver");

        // Set up Spinner Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        Log.d("SPINNER_SETUP", "Role spinner initialized with options");

        // Handle Spinner selection
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();
                if (!selectedRole.equals("Select your role")) {
                    Log.d("ROLE_SELECTION", "Selected Role: " + selectedRole);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("ROLE_SELECTION", "No role selected");
            }
        });

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

        if (selectedRole.equals("Select your role")) {
            Log.w("INPUT_VALIDATION", "No role selected");
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("DATABASE_QUERY", "Checking if email exists in database: " + email);

        // Query the Firebase Realtime Database to check if the email exists
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email exists, update the role
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                Log.d("DATABASE_QUERY", "Email found in database: " + email);
                                updateRole(userSnapshot.getKey(), selectedRole);
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
                    Toast.makeText(this, "Role updated successfully!", Toast.LENGTH_SHORT).show();

                    // Redirect user based on their role
                    if (newRole.equals("Provider")) {
                        Log.d("NAVIGATION", "Redirecting to ProviderDash");
                        startActivity(new Intent(ConfirmationPage.this, ProviderDash.class));
                    } else if (newRole.equals("Receiver")) {
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
