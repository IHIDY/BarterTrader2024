/**
 * Author: Junfa Li
 */
package com.example.group8_bartertrader;

// import package needed
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    // Firebase authentication instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find the logout button in the layout
        Button logoutButton = findViewById(R.id.LogOutButton);

        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        Button changeRoleBtn = findViewById(R.id.changeRoleBtn);

//        Spinner mySpinner = findViewById(R.id.mySpinner);

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

//        mySpinner.setAdapter(adapter);

        changeRoleBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ConfirmationPage.class);
            startActivity(intent);
        });


        // Set a click listener for the logout button
        logoutButton.setOnClickListener(view -> {
            // Confirm the logout action
            mAuth.signOut(); // Log out the user
            Toast.makeText(SettingsActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();

//           Redirect to the login activity
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish the current activity
        });

        resetPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
