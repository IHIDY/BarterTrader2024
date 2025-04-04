package com.example.group8_bartertrader.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialsValidator {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public CredentialsValidator() {
//        if (!unitTestRunning()) {
//            this.mAuth = FirebaseAuth.getInstance();
//        }
    }

    private boolean unitTestRunning() {
        return "true".equals(System.getProperty("isUnitTest"));
    }

    public boolean isEmptyEmail(String email) {
        return email.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String regex = "[a-zA-Z0-9._%+-]" + //local username
                "+@[a-zA-Z0-9.-]" + // @domain
                "+\\.[a-zA-Z]{2,6}$"; // top level domain: .com, .ca, etc
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public boolean isEmptyPass(String pass) {
        return pass.trim().isEmpty();
    }

    public boolean isValidPass(String pass) {
        if (pass == null || pass.trim().isEmpty()) {
            return false;
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{6,30}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pass);

        return m.matches();
    }

    public boolean isValidRole(String role) {
        return role.equals("Provider") || role.equals("Receiver");
    }

    public boolean isFnameEmpty(String fname) {
        return fname == null || fname.trim().isEmpty();
    }

    public boolean isLnameEmpty(String lname) {
        return lname == null || lname.trim().isEmpty();
    }
}
