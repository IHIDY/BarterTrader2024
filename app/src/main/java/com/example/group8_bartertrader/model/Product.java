package com.example.group8_bartertrader.model;

import android.util.Log;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private String category;
    private boolean isAvailable;
    private String latLngLocation;
    private String location;


    // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    public Product() {}

    // Constructor to initialize all fields
    public Product(String id, String name, String description, String category, boolean isAvailable, String latLngLocation, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.isAvailable = isAvailable;
        this.latLngLocation = latLngLocation;
        this.location = location;

    }

    // Getters and Setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public boolean isAvailable() {
        return isAvailable;
    }


    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLatLngLocation() {
        return latLngLocation;
    }

    public void setLatLngLocation(String latLngLocation) {
        this.latLngLocation = latLngLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
