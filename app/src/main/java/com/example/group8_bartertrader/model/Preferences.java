package com.example.group8_bartertrader.model;

public class Preferences {
    private String email;
    private String preferredCategory;  // Category as a single string
    private String preferredLocation;

    // Constructor
    public Preferences(String email, String preferredCategory, String preferredLocation) {
        this.email = email;
        this.preferredCategory = preferredCategory;
        this.preferredLocation = preferredLocation;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredCategory() {
        return preferredCategory;
    }

    public void setPreferredCategory(String preferredCategory) {
        this.preferredCategory = preferredCategory;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }
}
