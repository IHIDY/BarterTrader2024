package com.example.group8_bartertrader;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class CredentialsValidator {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public CredentialsValidator() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean isEmptyEmail(String email) {
        return email.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regex = "[a-zA-Z0-9._%+-]" + //local username
                "+@[a-zA-Z0-9.-]" + // @domain
                "+\\.[a-zA-Z]{2,6}$"; // top level domain: .com, .ca, etc
        Pattern p = Pattern.compile(regex);

        return p.matcher(email).matches();
    }

    public boolean isEmptyPassword(String pass) {
        return pass.trim().isEmpty();
    }

    public boolean isValidPass(String pass) {
        if (pass == null || pass.isEmpty()) {
            return false;
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{6,30}$";
        Pattern p = Pattern.compile(regex);

        return p.matcher(pass).matches();
    }

    public boolean isValidRole(String role) {
        return (role != null) && (!role.equals("Select your role"));
    }

    public boolean isFnameEmpty(String fname) {
        return fname.trim().isEmpty();
    }

    public boolean isLnameEmpty(String lname) {
        return lname.trim().isEmpty();
    }
}
