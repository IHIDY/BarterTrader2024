//Reference Link = https://www.geeksforgeeks.org/user-authentication-using-firebase-in-android/ -->
//Author = Ella Morais, B00926808

package com.example.group8_bartertrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView;
    private EditText passwordTextView;
    private EditText firstNameTextView;
    private EditText lastNameTextView;
    private Button btn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef; //current db reference
    private Spinner role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        //initialize firebase authentication
        mAuth = FirebaseAuth.getInstance();
        //initialize database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.pass);
        firstNameTextView = findViewById(R.id.firstName);
        lastNameTextView = findViewById(R.id.lastName);

        btn = findViewById(R.id.regBtn);
        role = findViewById(R.id.roleSelect);

        //Role spinner setup
        String[] items = new String[]{"Select your Role", "Provider", "Reciever"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        role.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regNewUser();
            }
        });
    }

    //Checks if a field is empty
    public static boolean isEmpty(String text){
        return text == null || text.trim().isEmpty();
    }

    //Method to validate email using regex
    public static boolean isValidEmail(String email){
        if (email == null || email.isEmpty()){
            return false;
        }
        String regex = "[a-zA-Z0-9._%+-]" + //local username
                "+@[a-zA-Z0-9.-]" + // @domain
                "+\\.[a-zA-Z]{2,6}$"; // top level domain: .com, .ca, etc
        return email.matches(regex);
    }

    //Method to validate password using regex, follows firebase requirements
    public static boolean isValidPass(String pass){
        if (pass == null || pass.length() < 6 || pass.length() > 4096) {
            return false;
        }
        //at least one letter
        //at least one uppercase letter
        //at least one number
        //at least one special char out of these: @ # $ % ^ & + = !
        //length at least 6 chars
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$";
        return pass.matches(regex);
    }

    //Check if role is selected
    public static boolean isValidRole(String role){
        return role != null && !role.equals("Select your role");
    }

    //Displays toast message
    //Added to clean up regNewUser code
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //Method to register a new user
    private void regNewUser() {

        String email = emailTextView.getText().toString().trim();
        String pass = passwordTextView.getText().toString().trim();
        String firstName = firstNameTextView.getText().toString().trim();
        String lastName = lastNameTextView.getText().toString().trim();
        String roleSelect = role.getSelectedItem().toString();

        if (!isValidEmail(email)) {
            showToast("Enter a valid email");
            return;
        }

        if (!isValidPass(pass)) {
            showToast("*Password must be: Longer than 6 digits At least one uppercase At least one lowercase At least one Number At least one special character");
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            showToast("Enter your first name");
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            showToast("Enter your last name");
            return;
        }

        if (!isValidRole(roleSelect)) {
            showToast("Select your role");
            return;
        }
        //Firebase authentication for email and password
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get the userID from the firebase
                String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                //create a new user with the userID details
                User user = new User(firstName, lastName, email, roleSelect);
                //save the new user to the firebase
                databaseRef.child(userId).setValue(user).addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        showToast("Registration successful!");
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        finish(); //So users cannot nav back to registration page
                    } else {
                        showToast("Failed to save users data");
                    }
                });
            } else {
                showToast("Registration failed! " + task.getException().getMessage()); //Shows error message
            }
        });
    }
    //stores user data in firebase
    public static class User {
        public String firstName, lastName, email, role;

        //constructor
        public User() {} //has to be empty

        public User(String firstName, String lastName, String email, String role) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.role = role;
        }

    }
}