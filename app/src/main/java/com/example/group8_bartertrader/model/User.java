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

    /**
     * constructor for user
     * @param email
     * @param pass
     * @param role
     * @param firstName
     * @param lastName
     */
    public User(String email, String pass, String role, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.pass = pass;
    }

    /**
     * first name getter
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * first name setter
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * last name getter
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * last name setter
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * email getter
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * email setter
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * role getter
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     * role setter
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * password getter
     * @return
     */
    public String getPass() {
        return pass;
    }

    /**
     * password setter
     * @param pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /** preferred category setter
     *
     * @return
     */
    @Exclude
    public Set<String> getPreferredCategoriesSet() {
        return preferredCategories != null ? preferredCategories.keySet() : new HashSet<>();
    }

    /**
     * preferred categorie getter
     * @return
     */
    public Map<String, Boolean> getPreferredCategories() {
        return preferredCategories;
    }

    /**
     * preferred category setter
     * @param preferredCategories
     */
    public void setPreferredCategories(Map<String, Boolean> preferredCategories) {
        this.preferredCategories = preferredCategories;
    }

    /**
     * preferred location setter
     * @return
     */
    @Exclude
    public Set<String> getPreferredLocationsSet() {
        return preferredLocations != null ? preferredLocations.keySet() : new HashSet<>();
    }

    /**
     * preferred location getter
     * @return
     */
    public Map<String, Boolean> getPreferredLocations() {
        return preferredLocations;
    }

    /**
     * preferred location setter
     * @param preferredLocations
     */
    public void setPreferredLocations(Map<String, Boolean> preferredLocations) {
        this.preferredLocations = preferredLocations;
    }
}