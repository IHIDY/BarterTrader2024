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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                Log.d("DEBUG", "Attempting to sign in with email: " + email);

                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Sign In Successful.");
                    Toast.makeText(LoginActivity.this, "Successful Login.", Toast.LENGTH_SHORT).show();

                    // Get the authenticated user's email
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Log.e("ERROR", "User authentication failed, user object is null.");
                        return;
                    }

                    String currentUserEmail = user.getEmail();
                    Log.d("DEBUG", "Authenticated user email: " + currentUserEmail);

                    // Reference to the Users node in the database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    Log.d("DEBUG", "Querying database for user details...");

                    // Query the database for the user by email
                    databaseReference.orderByChild("email").equalTo(currentUserEmail)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d("DEBUG", "Database query completed. Checking results...");

                                    if (dataSnapshot.exists()) {
                                        Log.d("DEBUG", "User data found for email: " + currentUserEmail);

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            // Retrieve user role
                                            String role = snapshot.child("role").getValue(String.class);
                                            String firstName = snapshot.child("firstName").getValue(String.class);
                                            String lastName = snapshot.child("lastName").getValue(String.class);
                                            String pass = snapshot.child("pass").getValue(String.class);

                                            // Log user details
                                            Log.d("User Details", "Email: " + currentUserEmail);
                                            Log.d("User Details", "First Name: " + firstName);
                                            Log.d("User Details", "Last Name: " + lastName);
                                            Log.d("User Details", "Password: " + pass);
                                            Log.d("User Details", "Role: " + role);

                                            Toast.makeText(LoginActivity.this, "User details retrieved.", Toast.LENGTH_SHORT).show();

//

                                            // Determine redirection
                                            Intent intent;
                                            if (role.equals("Provider")) {
                                                Log.d("DEBUG", "User is a Provider. Redirecting to ProviderDash.");
                                                intent = new Intent(LoginActivity.this, ProviderDash.class);
                                            } else if (role.equals("Receiver")) {
                                                Log.d("DEBUG", "User is a Receiver. Redirecting to ReceiverDash.");
                                                intent = new Intent(LoginActivity.this, ReceiverDash.class);
                                            } else {
                                                Log.w("WARNING", "Unknown role: " + role);
                                                Toast.makeText(LoginActivity.this, "Invalid user role.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            startActivity(intent);
                                            Log.d("DEBUG", "Redirection successful. Closing LoginActivity.");
//                                            finish();
                                        }
                                    } else {
                                        Log.d("DEBUG", "No user data found for this email.");
                                        Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("ERROR", "Database error: " + databaseError.getMessage());
                                    Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Log.w("WARNING", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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