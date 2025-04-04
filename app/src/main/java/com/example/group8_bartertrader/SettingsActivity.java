package com.example.group8_bartertrader;

// Import necessary packages
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.notification.NotificationActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button logoutButton = findViewById(R.id.LogOutButton);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        Button changeRoleBtn = findViewById(R.id.changeRoleBtn);
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
            Intent intent = new Intent(SettingsActivity.this, ConfirmationPage.class);
            startActivity(intent);
        });

        // Set a click listener for the logout button
        logoutButton.setOnClickListener(view -> showLogoutConfirmation());

        notificationPreferencesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        resetPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
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
