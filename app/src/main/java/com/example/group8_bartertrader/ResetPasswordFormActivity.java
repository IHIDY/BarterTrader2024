package com.example.group8_bartertrader;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordFormActivity extends AppCompatActivity {
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button resetPasswordButton;

    /**
     * Called when the activity is created. Initializes the UI elements and sets up listeners for password reset.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password_form_page);

        // Initialize UI elements
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
//                Toast.makeText(ResetPasswordFormActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Please fill in both fields", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
//                Toast.makeText(ResetPasswordFormActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Password must be at least 6 characters", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
//                Toast.makeText(ResetPasswordFormActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                return;
            }

            updatePassword(newPassword);
        });
    }

    /**
     * Updates the user's password if the new password is valid and confirmed correctly.
     * If the update is successful, the user is redirected to the login activity.
     * @param newPassword The new password entered by the user.
     */
    private void updatePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
//                    Toast.makeText(ResetPasswordFormActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Password updated successfully", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPasswordFormActivity.this, LoginActivity.class));
                    finish();
                } else {
//                    Toast.makeText(ResetPasswordFormActivity.this, "Error updating password", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Error updating password", Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
//            Toast.makeText(this, "No authenticated user found. Please log in again.", Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView().getRootView(), "No authenticated user found. Please log in again.", Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
