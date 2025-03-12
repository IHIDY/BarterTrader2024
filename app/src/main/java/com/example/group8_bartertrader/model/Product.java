package com.example.group8_bartertrader.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Product {
    private String id;
    private String email;
    private String name;
    private String description;
    private String category;
    private boolean isAvailable;
    private String latLngLocation;
    private String location;
    private String condition;
    private long postDate;

    // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    public Product() {}

    // Constructor to initialize all fields
    public Product(String id, String email, String name, String description, String category,
                   boolean isAvailable, String latLngLocation, String location, String condition, long postDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.description = description;
        this.category = category;
        this.isAvailable = isAvailable;
        this.latLngLocation = latLngLocation;
        this.location = location;
        this.condition = condition;
        this.postDate = postDate;
    }

    // Getters and Setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getLatLngLocation() { return latLngLocation; }
    public void setLatLngLocation(String latLngLocation) { this.latLngLocation = latLngLocation; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public long getPostDate() { return postDate; }
    public void setPostDate(long postDate) { this.postDate = postDate; }

    public String getFormattedPostDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(postDate)); // Convert long to formatted string
    }
}
