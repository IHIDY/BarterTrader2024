//Reference Link = https://www.geeksforgeeks.org/user-authentication-using-firebase-in-android/ -->
//Author = Ella Morais, B00926808

//importing necessary packages and libraries
package com.example.group8_bartertrader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn; // Login Button
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    CredentialsValidator cred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        this.loadRoleSpin();
        this.setRegBtn();
        this.setLogBtn();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        this.cred = new CredentialsValidator();
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

    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.post(()-> statusLabel.setText(message.trim()));
    }

    protected void saveCreds(String uid, String email, String pass, String role, String fname, String lname) {
        User user = new User(email, pass, role, fname, lname);

        databaseReference.child(uid).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success", "User Saved to Database");
                            Toast.makeText(getApplicationContext(), "User data saved to database!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Error", "User not saved to database");
                            Toast.makeText(getApplicationContext(), "User data failed to save!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    //creating a new user
    @Override
    public void onClick(View view) {
        //if login was clicked instead
        if (view.getId() == R.id.loginBtn) {
            move2Login();
            return;
        }

        //getting all user input as strings
        String email = getEmail();
        String pass = getPassword();
        String role = getRole();
        String fName = getFname();
        String lName = getLname();

        if (email.isEmpty()) {
            setStatusMessage(getResources().getString(R.string.EMPTY_EMAIL_ADDRESS));
            showToast(getResources().getString(R.string.EMPTY_EMAIL_ADDRESS));
        } else if (!cred.isValidEmail(email)) {
            setStatusMessage(getResources().getString(R.string.INVALID_EMAIL_ADDRESS));
            showToast(getResources().getString(R.string.INVALID_EMAIL_ADDRESS));
        } else if (pass.isEmpty()) {
            setStatusMessage(getResources().getString(R.string.EMPTY_PASSWORD));
            showToast(getResources().getString(R.string.EMPTY_PASSWORD));
        } else if (!cred.isValidPass(pass)) {
            setStatusMessage(getResources().getString(R.string.INVALID_PASSWORD));
            showToast(getResources().getString(R.string.INVALID_PASSWORD));
        } else if (!cred.isValidRole(role) || role.equals("Select your Role")) {
            setStatusMessage(getResources().getString(R.string.INVALID_ROLE));
            showToast(getResources().getString(R.string.INVALID_ROLE));
        } else if (cred.isFnameEmpty(fName)) {
            setStatusMessage(getResources().getString(R.string.EMPTY_FNAME));
            showToast(getResources().getString(R.string.EMPTY_FNAME));
        } else if (cred.isLnameEmpty(lName)) {
            setStatusMessage(getResources().getString(R.string.EMPTY_LNAME));
            showToast(getResources().getString(R.string.EMPTY_LNAME));
        } else if (!isNetworkAvailable()) {
            setStatusMessage(getResources().getString(R.string.NO_NETWORK));
            showToast("No internet connection. Please check your network.");
            return;
        } else {
            setStatusMessage(getResources().getString(R.string.REGISTRATION_SUCCESSFUL));
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showToast("Registration Successful!");

                                String uid = task.getResult().getUser().getUid();
                                saveCreds(uid, email, pass, role, fName, lName);

                                move2Login();
                            } else {
                                showToast("Registration Failed! - User already exists!\n");
                                setStatusMessage(getResources().getString(R.string.REGISTRATION_FAILED));
                            }
                        }
                    });
        }

//        if (!isNetworkAvailable()) {
//            setStatusMessage(getResources().getString(R.string.NO_NETWORK));
//            showToast("No internet connection. Please check your network.");
//            return;
//        }
    }
}