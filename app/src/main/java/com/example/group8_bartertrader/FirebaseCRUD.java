package com.example.group8_bartertrader;

import android.net.Credentials;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class FirebaseCRUD {
    private final FirebaseDatabase database;

    private DatabaseReference emailRef;
    private DatabaseReference passRef;
    private DatabaseReference roleRef;
    private DatabaseReference fnameRef;
    private DatabaseReference lnameRef;

    private String extEmail;
    private String extPass;
    private String extRole;
    private String extFname;
    private String extLname;

    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;

        this.emailRef = database.getReference("emailAddress");
        this.passRef = database.getReference("password");
        this.roleRef = database.getReference("role");
        this.fnameRef = database.getReference("fName");
        this.lnameRef = database.getReference("lName");

        this.setEmailListener();
        this.setPasswordListener();
        this.setRoleListener();
        this.setFnameListener();
        this.setLnameListener();
    }

    public void registerUser(String email, String pass, String role, String fName, String lName) {
        setEmail(email);
        setPassword(pass);
        setRole(role);
        setFname(fName);
        setLname(lName);
    }

    protected DatabaseReference getEmailRef() {
        return this.database.getReference("emailAddress");
    }

    protected DatabaseReference getPassRef() {
        return this.database.getReference("password");
    }

    protected DatabaseReference getRoleRef() {
        return this.database.getReference("role");
    }

    protected DatabaseReference getFnameRef() {
        return this.database.getReference("fName");
    }

    protected DatabaseReference getLname() {
        return this.database.getReference("lName");
    }

    public void setEmail(String emailAddress) {
        this.emailRef.setValue(emailAddress);
    }

    public void setPassword(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pass.getBytes());
            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            this.passRef.setValue(hex.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRole(String role) {
        this.roleRef.setValue(role);
    }

    public void setFname(String fName) {
        this.roleRef.setValue(fName);
    }

    public void setLname(String lName) {
        this.roleRef.setValue(lName);
    }

    protected void setEmailListener() {
        this.emailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extEmail = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setPasswordListener() {
        this.passRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extPass = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setRoleListener() {
        this.roleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extRole = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setFnameListener() {
        this.fnameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extFname = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setLnameListener() {
        this.lnameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extLname = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void initializeDatabaseRefs() {
        this.emailRef = getEmailRef();
        this.passRef = getPassRef();
        this.roleRef = getRoleRef();
        this.fnameRef = getFnameRef();
        this.lnameRef = getLname();
    }

    protected void initializeDatabaseRefListeners() {
        this.setEmailListener();
        this.setPasswordListener();
        this.setRoleListener();
        this.setFnameListener();
        this.setLnameListener();
    }

    public String getExtractedEmailAddress() {
        return extEmail;
    }

    public String getExtractedPassword() {
        return extPass;
    }

    public String getExtractedRole() {
        return extRole;
    }

    public String getExtractedFname() {
        return extFname;
    }

    public String getExtractedLname() {
        return extLname;
    }
}
