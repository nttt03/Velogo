package com.example.velogoapp;

import java.io.Serializable;

public class Bicycles implements Serializable {

    private String bicycleID;
    private String image;
    private String modelName;
    private String type;
    private String status;
    private String rentalPricePerHour;
    private String createdAt;

    // Getter và Setter
    public String getBicycleID() {
        return bicycleID;
    }

    public void setBicycleID(String bicycleID) {
        this.bicycleID = bicycleID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRentalPricePerHour() {
        return rentalPricePerHour;
    }

    public void setRentalPricePerHour(String rentalPricePerHour) {
        this.rentalPricePerHour = rentalPricePerHour;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Constructor
    public Bicycles(String bicycleID, String image, String modelName, String type, String status, String rentalPricePerHour, String createdAt) {
        this.bicycleID = bicycleID;
        this.image = image;
        this.modelName = modelName;
        this.type = type;
        this.status = status;
        this.rentalPricePerHour = rentalPricePerHour;
        this.createdAt = createdAt;
    }
}
