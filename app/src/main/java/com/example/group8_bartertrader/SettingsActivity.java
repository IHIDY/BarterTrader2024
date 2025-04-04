package com.example.group8_bartertrader;

import android.app.AlertDialog;
// Import necessary packages
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.notification.NotificationActivity;
import com.example.group8_bartertrader.model.PreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PreferencesManager preferencesManager;
    private TextView preferencesSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase Auth and PreferencesManager
        mAuth = FirebaseAuth.getInstance();
        preferencesManager = PreferencesManager.getInstance(this);

        // Initialize views
        Button logoutButton = findViewById(R.id.LogOutButton);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        Button changeRoleBtn = findViewById(R.id.changeRoleBtn);
        Button editPreferencesBtn = findViewById(R.id.editPreferencesBtn);
        Button returnToDashBtn = findViewById(R.id.returnToDashBtn);
        preferencesSummary = findViewById(R.id.preferencesSummary); // Removed local variable declaration

        // Set up role spinner items
        Button notificationPreferencesBtn = findViewById(R.id.notificationPrefBtn);

        List<String> items = new ArrayList<>();
        items.add("Select a role");
        items.add("Provider");
        items.add("Receiver");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        changeRoleBtn.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, ConfirmationPage.class));
        });

        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Set a click listener for the logout button
        logoutButton.setOnClickListener(view -> showLogoutConfirmation());

        notificationPreferencesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
            startActivity(intent);
            finish();
        });
        returnToDashBtn.setOnClickListener(view -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // Get user role from Firebase Database instead of static User class
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String role = snapshot.child("role").getValue(String.class);
                        if ("Receiver".equals(role)) {
                            startActivity(new Intent(SettingsActivity.this, ReceiverDash.class));
                        } else if ("Provider".equals(role)) {
                            startActivity(new Intent(SettingsActivity.this, ProviderDash.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SettingsActivity.this, "Failed to get user role", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // User not logged in, redirect to login
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
        resetPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ResetPasswordActivity.class));
            finish();
        });

        editPreferencesBtn.setOnClickListener(v -> showEditPreferencesDialog());

        loadAndDisplayPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndDisplayPreferences();
    }

    private void loadAndDisplayPreferences() {
        if (preferencesManager != null && preferencesSummary != null) {
            preferencesManager.loadPreferencesFromFirebase((categories, locations) -> {
                runOnUiThread(() -> {
                    if (categories.isEmpty() && locations.isEmpty()) {
                        preferencesSummary.setText("No preferences set");
                    } else {
                        String summary = "Categories: " + TextUtils.join(", ", categories) +
                                "\nLocations: " + TextUtils.join(", ", locations);
                        preferencesSummary.setText(summary);
                    }
                });
            });
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    mAuth.signOut();
                    Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}

    private void showEditPreferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_preferences, null);

        EditText categoriesInput = dialogView.findViewById(R.id.categoriesInput);
        EditText locationsInput = dialogView.findViewById(R.id.locationsInput);

        if (preferencesManager != null) {
            Set<String> categories = preferencesManager.getPreferredCategories();
            Set<String> locations = preferencesManager.getPreferredLocations();

            categoriesInput.setText(TextUtils.join(", ", categories));
            locationsInput.setText(TextUtils.join(", ", locations));
        }

        builder.setView(dialogView)
                .setTitle("Edit Preferences")
                .setPositiveButton("Save", (dialog, which) -> {
                    Set<String> newCategories = new HashSet<>(
                            Arrays.asList(categoriesInput.getText().toString().split("\\s*,\\s*"))
                    );
                    Set<String> newLocations = new HashSet<>(
                            Arrays.asList(locationsInput.getText().toString().split("\\s*,\\s*"))
                    );

                    if (preferencesManager != null) {
                        preferencesManager.savePreferredCategories(newCategories);
                        preferencesManager.savePreferredLocations(newLocations);
                        loadAndDisplayPreferences();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}