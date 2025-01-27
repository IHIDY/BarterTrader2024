//Reference Link = https://www.geeksforgeeks.org/user-authentication-using-firebase-in-android/ -->
//Author = Ella Morais, B00926808

//importing necessary packages and libraries
package com.example.group8_bartertrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
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

    private EditText fNameView; //first name
    private EditText lNameView; //last name
    private EditText emailTextView; //email
    private EditText passwordTextView; //password
    private Button btn; //registration button
    private FirebaseAuth mAuth; //authentication
    private Spinner role; //role selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        //get all user input into usable variables
        fNameView = (findViewById(R.id.fName));
        lNameView = (findViewById(R.id.lName));
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.pass);
        btn = findViewById(R.id.regBtn);
        role = findViewById(R.id.roleSelect);

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
    }

    public void errorMessage(String x, String y) {
        if (TextUtils.isEmpty(x)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter your " + y + "!",
                    Toast.LENGTH_LONG).show();

            return;
        }
    }

    //creating a new user
    private void regNewUser() {

        //getting all user input as strings
        String email = emailTextView.getText().toString();
        String pass = passwordTextView.getText().toString();
        String fName = fNameView.getText().toString();
        String lName = lNameView.getText().toString();
        String roleSelect = role.getSelectedItem().toString();

        //validate non empty input
        errorMessage(email, "Email");
        errorMessage(pass, "Pass");
        errorMessage(fName, "First Name");
        errorMessage(lName, "Last name");

        //ensure user selected a role
        if (roleSelect.equals("Select your Role")) {
            Toast.makeText(getApplicationContext(),
                    "Please select your Role!",
                    Toast.LENGTH_LONG).show();
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
    }
}