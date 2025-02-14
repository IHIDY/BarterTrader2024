package com.example.group8_bartertrader;
import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;

import com.example.group8_bartertrader.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class ResetPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button submitEmailButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // Ensure Firebase Auth is initialized correctly
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        submitEmailButton = findViewById(R.id.submitEmailButton);

        submitEmailButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ResetPasswordActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ResetPasswordActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            checkEmailExists(email);
        });
    }


    private void checkEmailExists(String email) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> signInMethods = task.getResult().getSignInMethods();
                        if (signInMethods != null && !signInMethods.isEmpty()) {
                            // Email exists in Firebase Authentication
                            Intent intent = new Intent(ResetPasswordActivity.this, ResetPasswordFormActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            // Email does NOT exist
                            Toast.makeText(ResetPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error in fetching sign-in methods
                        Toast.makeText(ResetPasswordActivity.this, "Error checking email", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle specific errors (e.g., network issues)
                    Toast.makeText(ResetPasswordActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}