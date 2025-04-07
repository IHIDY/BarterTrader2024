package com.example.group8_bartertrader.model;

public class Preferences {
    private String email;
    private String preferredCategory;  // Category as a single string
    private String preferredLocation;

    // Constructor

    /**
     * preferences constructor
     * @param email
     * @param preferredCategory
     * @param preferredLocation
     */
    public Preferences(String email, String preferredCategory, String preferredLocation) {
        this.email = email;
        this.preferredCategory = preferredCategory;
        this.preferredLocation = preferredLocation;
    }

    // Getters and Setters

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
     * category getter
     * @return
     */
    public String getPreferredCategory() {
        return preferredCategory;
    }

    /**
     * category setter
     * @param preferredCategory
     */
    public void setPreferredCategory(String preferredCategory) {
        this.preferredCategory = preferredCategory;
    }

    /**
     * location getter
     * @return
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * location setter
     * @param preferredLocation
     */
    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }
}
