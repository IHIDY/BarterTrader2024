package com.example.group8_bartertrader.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String pass;
    private Map<String, Boolean> preferredCategories = new HashMap<>();
    private Map<String, Boolean> preferredLocations = new HashMap<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String pass, String role, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.pass = pass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Exclude
    public Set<String> getPreferredCategoriesSet() {
        return preferredCategories != null ? preferredCategories.keySet() : new HashSet<>();
    }

    public Map<String, Boolean> getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(Map<String, Boolean> preferredCategories) {
        this.preferredCategories = preferredCategories;
    }

    @Exclude
    public Set<String> getPreferredLocationsSet() {
        return preferredLocations != null ? preferredLocations.keySet() : new HashSet<>();
    }

    public Map<String, Boolean> getPreferredLocations() {
        return preferredLocations;
    }

    public void setPreferredLocations(Map<String, Boolean> preferredLocations) {
        this.preferredLocations = preferredLocations;
    }
}