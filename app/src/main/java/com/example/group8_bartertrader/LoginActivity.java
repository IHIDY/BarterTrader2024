package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView; //email
    private EditText passwordTextView; //password
    private FirebaseAuth mAuth; //authentication
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.pass);
        mAuth = FirebaseAuth.getInstance();


        Button loginBtn = findViewById(R.id.loginBtn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "LOGIN BUTTON CLICKED");
                loginUser();
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d("USER DETAILS", "There is a current user");
        }
    }
    public boolean errorMessage(String input, String fieldName) {
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "Please enter your " + fieldName + "!", Toast.LENGTH_LONG).show();
            return false;  // Return false if field is empty
        }
        return true;  // Return true if field is filled
    }



    public void loginUser() {
        // getting all user login input as strings
        String email = emailTextView.getText().toString();
        String pass = passwordTextView.getText().toString();


        if (!errorMessage(email, "Email") || !errorMessage(pass, "Password")) {
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(),
                    "Enter Valid Email",
                    Toast.LENGTH_LONG).show();
        }


        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Sign In Email Success");
                    Toast.makeText(LoginActivity.this, "Successful Login.",
                            Toast.LENGTH_SHORT).show();
                    // Redirect to the dashboard page.
                     Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }
}