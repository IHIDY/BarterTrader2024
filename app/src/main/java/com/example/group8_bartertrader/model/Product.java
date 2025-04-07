package com.example.group8_bartertrader.model;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Product implements Serializable {
    private String id;
    private String email;
    private String name;
    private String description;
    private String category;
    private boolean isAvailable;
    private String location;
    private String condition;
    private long postDate;

    // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    public Product() {}

    // Constructor to initialize all fields

    /**
     * constructor to initialize all fields
     * @param id
     * @param email
     * @param name
     * @param description
     * @param category
     * @param isAvailable
     * @param location
     * @param condition
     * @param postDate
     */
    public Product(String id, String email, String name, String description, String category,
                   boolean isAvailable, String location, String condition, long postDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.description = description;
        this.category = category;
        this.isAvailable = isAvailable;
        this.location = location;
        this.condition = condition;
        this.postDate = postDate;
    }

    /**
     * product constructor for when certain fields arent submitted
     * @param id
     * @param name
     * @param description
     * @param category
     * @param location
     * @param isAvailable
     */
    public Product(String id, String name, String description, String category, String location, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
        this.isAvailable = isAvailable;
    }


    // Getters and Setters for all fields

    /**
     * id getter
     * @return
     */
    public String getId() { return id; }

    /**
     * id setter
     * @param id
     */
    public void setId(String id) { this.id = id; }

    /**
     * email getter
     * @return
     */
    public String getEmail() { return email; }

    /**
     * email setter
     * @param email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * name getter
     * @return
     */
    public String getName() { return name; }

    /**
     * name setter
     * @param name
     */
    public void setName(String name) { this.name = name; }

    /**
     * description getter
     * @return
     */
    public String getDescription() { return description; }

    /**
     * description setter
     * @param description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * category getter
     * @return
     */
    public String getCategory() { return category; }

    /**
     * category setter
     * @param category
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * available getter
     * @return
     */
    public boolean isAvailable() { return isAvailable; }

    /**
     * available setter
     * @param available
     */
    public void setAvailable(boolean available) { isAvailable = available; }

    /**
     * location getter
     * @return
     */
    public String getLocation() { return location; }

    /**
     * location setter
     * @param location
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * condition getter
     * @return
     */
    public String getCondition() { return condition; }

    /**
     * condition setter
     * @param condition
     */
    public void setCondition(String condition) { this.condition = condition; }

    /**
     * posted date getter
     * @return
     */
    public long getPostDate() { return postDate; }

    /**
     * posted date setter
     * @param postDate
     */
    public void setPostDate(long postDate) { this.postDate = postDate; }

    /**
     * formatted posted date getter
     * @return
     */
    public String getFormattedPostDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(postDate)); // Convert long to formatted string
    }
}
