package com.example.group8_bartertrader;

import android.app.AlertDialog;
// Import necessary packages
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.model.Preferences;
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

// Import necessary packages
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.notification.NotificationActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PreferencesManager preferencesManager;
    private TextView preferencesSummary;
    private String role;
    /**
     * Initializes the activity, sets up the UI, handles Firebase authentication,
     * and sets listeners for various buttons.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        preferencesManager = PreferencesManager.getInstance(this);

        Button logoutButton = findViewById(R.id.LogOutButton);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        Button changeRoleBtn = findViewById(R.id.changeRoleBtn);
        Button returnToDashBtn = findViewById(R.id.returnToDashBtn);
//        Button notificationPreferencesBtn = findViewById(R.id.notificationPrefBtn);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(currentUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    role = snapshot.child("role").getValue(String.class);

                    if ("Receiver".equals(role)) {
                        setupReceiverUI();
                    } else if ("Provider".equals(role)) {
                        setupProviderUI();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SettingsActivity.this, "Failed to get user role", Toast.LENGTH_SHORT).show();
                }
            });
        }

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
            Intent intent = new Intent(SettingsActivity.this, ConfirmationPage.class);
            startActivity(intent);
        });

        // Set a click listener for the logout button
        logoutButton.setOnClickListener(view -> showLogoutConfirmation());

//        notificationPreferencesBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
//            startActivity(intent);
//            finish();
//        });

        returnToDashBtn.setOnClickListener(view -> {
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
            Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
    /**
     * Loads and displays the user's preferences from Firebase and updates the UI.
     */
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
    /**
     * Sets up the UI for the "Receiver" role, displaying preferences and enabling
     * the option to edit preferences.
     */
    private void setupReceiverUI() {
        preferencesSummary = findViewById(R.id.preferencesSummary);
        Button editPreferencesBtn = findViewById(R.id.editPreferencesBtn);

        if (editPreferencesBtn != null) {
            editPreferencesBtn.setVisibility(View.VISIBLE);
            editPreferencesBtn.setOnClickListener(v -> showEditPreferencesDialog());
        }

        loadAndDisplayPreferences();
    }
    /**
     * Sets up the UI for the "Provider" role, hiding the edit preferences button
     * and displaying a message indicating that preferences are unavailable.
     */
    private void setupProviderUI() {
        Button editPreferencesBtn = findViewById(R.id.editPreferencesBtn);
        preferencesSummary = findViewById(R.id.preferencesSummary);

        if (editPreferencesBtn != null) {
            editPreferencesBtn.setVisibility(View.GONE);
        }

        if (preferencesSummary != null) {
            preferencesSummary.setText("Preferences are not available as provider");
        }
    }

    /**
     * Displays a confirmation dialog when the user attempts to log out.
     */
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
    /**
     * Displays a dialog for the user to edit their preferences, allowing them to
     * input new categories and locations.
     */
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

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

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
                    // Get the text from the input fields
                    String newCategory = categoriesInput.getText().toString().trim(); // Extract single string for category
                    String newLocation = locationsInput.getText().toString().trim(); // Extract single string for location

                    // Call the method to save preferences to Firebase with single strings
                    savePreferencesToFirebase(email, newCategory, newLocation);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    /**
     * Saves the user's preferences to Firebase, including the selected category and location.
     *
     * @param email The user's email address.
     * @param preferredCategory The preferred category set by the user.
     * @param preferredLocation The preferred location set by the user.
     */
    private void savePreferencesToFirebase(String email, String preferredCategory, String preferredLocation) {
        DatabaseReference preferencesRef = FirebaseDatabase.getInstance().getReference("Preferences")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Preferences preferences = new Preferences(email, preferredCategory, preferredLocation); // Pass category and location as strings

        // Save to Firebase with updated keys: preferredCategory and preferredLocation
        preferencesRef.setValue(preferences)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Data saved successfully
                        Toast.makeText(SettingsActivity.this, "Preferences saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error saving data
                        Toast.makeText(SettingsActivity.this, "Failed to save preferences!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
