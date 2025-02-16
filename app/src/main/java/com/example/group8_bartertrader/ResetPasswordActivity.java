package com.example.group8_bartertrader;
import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;

import com.example.group8_bartertrader.R;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button submitEmailButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        submitEmailButton = findViewById(R.id.submitEmailButton);

        // Set click listener for submit button
        submitEmailButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
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
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                if (emailExists) {
                    Intent intent = new Intent(ResetPasswordActivity.this, ResetPasswordFormActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Error checking email", Toast.LENGTH_SHORT).show();
            }
        });

    }
}