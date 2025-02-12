//Reference Link = https://www.geeksforgeeks.org/user-authentication-using-firebase-in-android/ -->
//Author = Ella Morais, B00926808

//importing necessary packages and libraries
package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.group8_bartertrader.FirebaseCRUD;
import com.example.group8_bartertrader.R;
//add this import com.example.group8_bartertrader.PasswordUtility;
//add this import com.example.group8_bartertrader.CredentialValidator

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn; // Login Button
    FirebaseCRUD crud; //crud

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        this.loadRoleSpin();
        this.setRegBtn();
        this.setLogBtn();

        this.crud = new FirebaseCRUD(getDatabase());
    }

    protected FirebaseDatabase getDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://group-8-barter-trader-default-rtdb.firebaseio.com/");

        return database;
    }

    public void loadRoleSpin() {
        Spinner role = findViewById(R.id.roleSelect);

        String[] items = new String[]{"Select your Role", "Provider", "Receiver"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                items);

        role.setAdapter(adapter);
    }

    public void setRegBtn() {
        Button regBtn = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(this);
    }

    public void setLogBtn() {
        Button logBtn = findViewById(R.id.loginBtn);
        logBtn.setOnClickListener(this);
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

    protected String getEmail() {
        EditText emailBox = findViewById(R.id.email);
        return emailBox.getText().toString().trim();
    }

    protected String getPassword() {
        EditText passBox = findViewById(R.id.pass);
        return passBox.getText().toString().trim();
    }

    protected String getRole() {
        Spinner roleBox = findViewById(R.id.roleSelect);
        return roleBox.getSelectedItem().toString().trim();
    }

    protected String getFname() {
        EditText fnameBox = findViewById(R.id.fName);
        return fnameBox.getText().toString().trim();
    }

    protected String getLname() {
        EditText lnameBox = findViewById(R.id.lName);
        return lnameBox.getText().toString().trim();
    }

    protected void move2Login() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    protected void saveCred(String email, String pass, String role, String fname, String lname) {
        crud.registerUser(email, pass, role, fname, lname);
    }

    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }

    //creating a new user
    @Override
    public void onClick(View view) {
        //if login was clicked instead
        if (view.getId() == R.id.loginBtn) {
            move2Login();
        }

        //getting all user input as strings
        String email = getEmail();
        String pass = getPassword();
        String role = getRole();
        String fName = getFname();
        String lName = getLname();

        String errorMess = "";

        //if statements to check if credentials are valid INSERT HERE
        //utilize a new credentials page

        if (errorMess.isEmpty()) {
            saveCred(email, pass, role, fName, lName);
            move2Login();
        }
    }
}