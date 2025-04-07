package com.example.group8_bartertrader.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialsValidator {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    /**
     * credentials constructor
     */
    public CredentialsValidator() {
//        if (!unitTestRunning()) {
//            this.mAuth = FirebaseAuth.getInstance();
//        }
    }

    /**
     * when a unit test is running
     * @return
     */
    private boolean unitTestRunning() {
        return "true".equals(System.getProperty("isUnitTest"));
    }

    /**
     * checks if the email is empty
     * @param email
     * @return
     */
    public boolean isEmptyEmail(String email) {
        return email.trim().isEmpty();
    }

    /**
     * checks if the email is valid
     * @param email
     * @return
     */
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

    /**
     * checks for an empty password
     * @param pass
     * @return
     */
    public boolean isEmptyPass(String pass) {
        return pass.trim().isEmpty();
    }

    /**
     * checks if the password is valid
     * @param pass
     * @return
     */
    public boolean isValidPass(String pass) {
        if (pass == null || pass.trim().isEmpty()) {
            return false;
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{6,30}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pass);

        return m.matches();
    }

    /**
     * checks if the role is selected
     * @param role
     * @return
     */
    public boolean isValidRole(String role) {
        return role.equals("Provider") || role.equals("Receiver");
    }

    /**
     * checks if the first name is empty
     * @param fname
     * @return
     */
    public boolean isFnameEmpty(String fname) {
        return fname == null || fname.trim().isEmpty();
    }

    /**
     * checks if the last name is empty
     * @param lname
     * @return
     */
    public boolean isLnameEmpty(String lname) {
        return lname == null || lname.trim().isEmpty();
    }
}
