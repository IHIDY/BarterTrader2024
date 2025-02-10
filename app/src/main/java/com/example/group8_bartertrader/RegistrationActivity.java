//Reference Link = https://www.geeksforgeeks.org/user-authentication-using-firebase-in-android/ -->
//Author = Ella Morais, B00926808

//importing necessary packages and libraries
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fNameView; //first name
    private EditText lNameView; //last name
    private EditText emailTextView; //email
    private EditText passwordTextView; //password
    private Button btn; //registration button
    private Button loginBtn; // Login Button
    private FirebaseAuth mAuth; //authentication
    private Spinner role; //role selection
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //get all user input into usable variables
        fNameView = (findViewById(R.id.fName));
        lNameView = (findViewById(R.id.lName));
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.pass);
        btn = findViewById(R.id.regBtn);
        role = findViewById(R.id.roleSelect);

        loginBtn = findViewById(R.id.loginBtn);

        //dropdown list for role selection
        String[] items = new String[]{"Select your Role", "Provider", "Receiver"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                items);
        role.setAdapter(adapter);

        //when user registers/tries to
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regNewUser();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);    }});
    }

    //checking valid email
    public static int isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return 1; //email not input
        }

        String regex = "[a-zA-Z0-9._%+-]" + //local username
                "+@[a-zA-Z0-9.-]" + // @domain
                "+\\.[a-zA-Z]{2,6}$"; // top level domain: .com, .ca, etc

        if (email.matches(regex)) {
            return 0; //email matches structure
        } else {
            return 2; //email does not match structure
        }
    }

    public static int isValidPass(String pass) {
        if (pass == null || pass.isEmpty()) {
            return 1; //no password entered
        }
        if (pass.length() < 6) {
            return 2; //password too short
        }
        if (pass.length() > 4096) {
            return 3; //password too long
        }

        //at least one letter
        //at least one uppercase letter
        //at least one number
        //at least one special char out of these: @ # $ % ^ & + = !
        //length at least 6 chars
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$";

        if (pass.matches(regex)) {
            return 0;  //the password does follow the necessary structure
        } else {
            return 4; //password does not follow the correct structure
        }
    }

    //check if role is selected
    public static boolean isValidRole(String role) {
        return (role != null) && (!role.equals("Select your role"));
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //creating a new user
    private void regNewUser() {

        //getting all user input as strings
        String email = emailTextView.getText().toString().trim();
        String pass = passwordTextView.getText().toString().trim();
        String fName = fNameView.getText().toString().trim();
        String lName = lNameView.getText().toString().trim();
        String roleSelect = role.getSelectedItem().toString().trim();

        //email validation and message sender
        if (isValidEmail(email) == 1) {
            showToast(" Please enter your email!");
            return;
        } else if (isValidEmail(email) == 2) {
            showToast(" Please enter a valid email!");
            return;
        }

        //password validation and message sender
        if (isValidPass(pass) == 1) {
            showToast(" Please enter your password!");
            return;
        } else if (isValidPass(pass) == 2) {
            showToast(" Password must be longer than 6 characters!");
            return;
        } else if (isValidPass(pass) == 3) {
            showToast(" Password must be shorter than 4096 characters!");
            return;
        } else if (isValidPass(pass) == 4) {
            showToast(" Password must have " +
                    "\n1 uppercase character" +
                    "\n1 lowercase character" +
                    "\n1 number" +
                    "\n1 special character (@ # $ % ^ & + = !)");
            return;
        }

        //name validation and message sender
        if (TextUtils.isEmpty(fName)) {
            showToast("Enter your first name!");
            return;
        }

        if (TextUtils.isEmpty(lName)) {
            showToast("Enter your last name!");
            return;
        }

        //role validation and message sender
        if (!isValidRole(roleSelect)) {
            showToast("Select your role!");
            return;
        }

        //create user with email and password
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if registration is successful show success message
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent
                                    = new Intent(RegistrationActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else { //if registration fails show failure message
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!"
                                    + "Please try again later",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // Generate a random userId
        String userId = generateRandomUserId();

        // Save user data to Firebase Realtime Database
        saveUserDataToDatabase(userId, fName, lName, email, roleSelect);
    }
    private String generateRandomUserId() {
        // Define the range for the random number (e.g., 100000 to 999999)
        int min = 10000000;
        int max = 99999999;

        // Generate a random number within the range
        int randomNumber = (int) (Math.random() * (max - min + 1)) + min;

        // Convert the number to a String and return it
        return String.valueOf(randomNumber);
    }
    private void saveUserDataToDatabase(String userId, String fName, String lName, String email, String role) {
        // Create a User object or use a HashMap to store the data
        User user = new User(fName, lName, email, role);

        // Save the user data under the "users" node with the user's UID as the key
        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success", "User Saved to Database");
//                            Toast.makeText(getApplicationContext(), "User data saved to database!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Error", "User not saved to Database");
//                            Toast.makeText(getApplicationContext(), "Failed to save user data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}