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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
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

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        submitEmailButton = findViewById(R.id.submitEmailButton);

        submitEmailButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter an email", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Invalid email format", Snackbar.LENGTH_SHORT).show();
                return;
            }

            checkEmailExists(email);
        });
    }


    /*
    * using signInWithEmailAndPassword, use the invalid user exception to return email not registered,
    * else continue and reset password*/


    private void checkEmailExists(String email) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, "dummyPassword123")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Email exists, proceed to reset password
                        Intent intent = new Intent(ResetPasswordActivity.this, ResetPasswordFormActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        //Email does NOT exist
//                        Toast.makeText(ResetPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Email not registered", Snackbar.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Email exists but incorrect password → Still means email is registered
                        Intent intent = new Intent(ResetPasswordActivity.this, ResetPasswordFormActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        //Other errors (network issues, etc.)
//                        Toast.makeText(ResetPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }




}