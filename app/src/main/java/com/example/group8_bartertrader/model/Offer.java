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

    /**
     * constructor for an offer
     * @param id
     * @param providerEmail
     * @param receiverEmail
     * @param offeredItemName
     * @param targetItemName
     * @param offeredItemCategory
     * @param targetItemCategory
     * @param offeredItemLocation
     * @param targetItemLocation
     * @param offeredItemDescription
     * @param targetItemDescription
     * @param targetItemId
     * @param status
     */
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

    /**
     * id getter
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * id setter
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * provider email getter
     * @return
     */
    public String getProviderEmail() {
        return providerEmail;
    }

    /**
     * provider email setter
     * @param providerEmail
     */
    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    /**
     * receiver email getter
     * @return
     */
    public String getReceiverEmail() {
        return receiverEmail;
    }

    /**
     * receiver email setter
     * @param receiverEmail
     */
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    /**
     * item name getter
     * @return
     */
    public String getOfferedItemName() {
        return offeredItemName;
    }

    /**
     * item name setter
     * @param offeredItemName
     */
    public void setOfferedItemName(String offeredItemName) {
        this.offeredItemName = offeredItemName;
    }

    /**
     * item name getter
     * @return
     */
    public String getTargetItemName() {
        return targetItemName;
    }

    /**
     * item name setter
     * @param targetItemName
     */
    public void setTargetItemName(String targetItemName) {
        this.targetItemName = targetItemName;
    }

    /**
     * category getter
     * @return
     */
    public String getOfferedItemCategory() {
        return offeredItemCategory;
    }

    /**
     * category setter
     * @param offeredItemCategory
     */
    public void setOfferedItemCategory(String offeredItemCategory) {
        this.offeredItemCategory = offeredItemCategory;
    }

    /**
     * category getter
     * @return
     */
    public String getTargetItemCategory() {
        return targetItemCategory;
    }

    /**
     * category setter
     * @param targetItemCategory
     */
    public void setTargetItemCategory(String targetItemCategory) {
        this.targetItemCategory = targetItemCategory;
    }

    /**
     * location getter
     * @return
     */
    public String getOfferedItemLocation() {
        return offeredItemLocation;
    }

    /**
     * location setter
     * @param offeredItemLocation
     */
    public void setOfferedItemLocation(String offeredItemLocation) {
        this.offeredItemLocation = offeredItemLocation;
    }

    /**
     * location getter
     * @return
     */
    public String getTargetItemLocation() {
        return targetItemLocation;
    }

    /**
     * location setter
     * @param targetItemLocation
     */
    public void setTargetItemLocation(String targetItemLocation) {
        this.targetItemLocation = targetItemLocation;
    }

    /**
     * description getter
     * @return
     */
    public String getOfferedItemDescription() {
        return offeredItemDescription;
    }

    /**
     * description setter
     * @param offeredItemDescription
     */
    public void setOfferedItemDescription(String offeredItemDescription) {
        this.offeredItemDescription = offeredItemDescription;
    }

    /**
     * description getter
     * @return
     */
    public String getTargetItemDescription() {
        return targetItemDescription;
    }

    /**
     * description setter
     * @param targetItemDescription
     */
    public void setTargetItemDescription(String targetItemDescription) {
        this.targetItemDescription = targetItemDescription;
    }

    /**
     * id getter
     * @return
     */
    public String getTargetItemId() {
        return targetItemId;
    }

    /**
     * id setter
     * @param targetItemId
     */
    public void setTargetItemId(String targetItemId) {
        this.targetItemId = targetItemId;
    }

    /**
     * status getter
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * status setter
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
