//Reference Link = https://www.geeksforgeeks.org/user-authentication-using-firebase-in-android/ -->
//Author = Ella Morais, B00926808

package com.example.group8_bartertrader;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView;
    private EditText passwordTextView;
    private Button btn;
    private FirebaseAuth mAuth;
    private Spinner role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.pass);
        btn = findViewById(R.id.regBtn);
        role = findViewById(R.id.roleSelect);

        String[] items = new String[]{"Select your Role", "Provider", "Reciever"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                items);
        role.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regNewUser();
            }
        });
    }

    public static boolean isEmpty(String text){
        return text == null || text.trim().isEmpty();
    }
    public static boolean isValidEmail(String email){
        if (email == null || email.isEmpty()){
            return false;
        }
        String regex = "[a-zA-Z0-9._%+-]" + //local username
                "+@[a-zA-Z0-9.-]" + // @domain
                "+\\.[a-zA-Z]{2,6}$"; // top level domain (.com, .ca, etc)
        return email.matches(regex);
    }

    public static boolean isValidPass(String pass){
        return !isEmpty(pass) && pass.length() >= 6;
    }

    public static boolean isValidRole(String role){
        return role != null && !role.equals("Select your role");
    }

    private void regNewUser() {

        String email = emailTextView.getText().toString();
        String pass = passwordTextView.getText().toString();
        String roleSelect = role.getSelectedItem().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a valid Email!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            Toast.makeText(getApplicationContext(),
                    "Passwords must be at least 6 characters",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (roleSelect.equals("Select your Role")) {
            Toast.makeText(getApplicationContext(),
                    "Please select a valid Role!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent
                                    = new Intent(RegistrationActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish(); //So users cannot nav back to registration page
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed! "
                                    + task.getException().getMessage(),Toast.LENGTH_LONG).show(); //Shows error message
                        }
                    }
                });
    }
}