package com.example.group8_bartertrader.model;

public class Offer {
    private transient String id;
    private String providerEmail;
    private String receiverEmail;

    private String offeredItemName;
    private String targetItemName;
    private String offeredItemCategory;
    private String targetItemCategory;
    private String offeredItemLocation;
    private String targetItemLocation;
    private String offeredItemDescription;
    private String targetItemDescription;

    private String targetItemId;
    private String status; // Offer status (pending, accepted, rejected)

    // Default constructor required for Firebase
    public Offer() {}

    // Constructor to initialize all fields
    public Offer(String id, String providerEmail, String receiverEmail,
                 String offeredItemName, String targetItemName,
                 String offeredItemCategory, String targetItemCategory,
                 String offeredItemLocation, String targetItemLocation,
                 String offeredItemDescription, String targetItemDescription,
                 String targetItemId,
                 String status) {
        this.id = id;
        this.providerEmail = providerEmail;
        this.receiverEmail = receiverEmail;
        this.offeredItemName = offeredItemName;
        this.targetItemName = targetItemName;
        this.offeredItemCategory = offeredItemCategory;
        this.targetItemCategory = targetItemCategory;
        this.offeredItemLocation = offeredItemLocation;
        this.targetItemLocation = targetItemLocation;
        this.offeredItemDescription = offeredItemDescription;
        this.targetItemDescription = targetItemDescription;
        this.targetItemId = targetItemId;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getOfferedItemName() {
        return offeredItemName;
    }

    public void setOfferedItemName(String offeredItemName) {
        this.offeredItemName = offeredItemName;
    }

    public String getTargetItemName() {
        return targetItemName;
    }

    public void setTargetItemName(String targetItemName) {
        this.targetItemName = targetItemName;
    }

    public String getOfferedItemCategory() {
        return offeredItemCategory;
    }

    public void setOfferedItemCategory(String offeredItemCategory) {
        this.offeredItemCategory = offeredItemCategory;
    }

    public String getTargetItemCategory() {
        return targetItemCategory;
    }

    public void setTargetItemCategory(String targetItemCategory) {
        this.targetItemCategory = targetItemCategory;
    }

    public String getOfferedItemLocation() {
        return offeredItemLocation;
    }

    public void setOfferedItemLocation(String offeredItemLocation) {
        this.offeredItemLocation = offeredItemLocation;
    }

    public String getTargetItemLocation() {
        return targetItemLocation;
    }

    public void setTargetItemLocation(String targetItemLocation) {
        this.targetItemLocation = targetItemLocation;
    }

    public String getOfferedItemDescription() {
        return offeredItemDescription;
    }

    public void setOfferedItemDescription(String offeredItemDescription) {
        this.offeredItemDescription = offeredItemDescription;
    }

    public String getTargetItemDescription() {
        return targetItemDescription;
    }

    public void setTargetItemDescription(String targetItemDescription) {
        this.targetItemDescription = targetItemDescription;
    }

    public String getTargetItemId() {
        return targetItemId;
    }

    public void setTargetItemId(String targetItemId) {
        this.targetItemId = targetItemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
