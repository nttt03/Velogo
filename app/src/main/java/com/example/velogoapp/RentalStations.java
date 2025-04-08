package com.example.velogoapp;

import java.io.Serializable;

public class RentalStations implements Serializable {
    private String StationID;
    private String StationName;
    private String Location;
    private String Capacity;
    private String CreatedAt;

    public RentalStations() {
    }

    public RentalStations(String stationID, String stationName, String location, String capacity, String createdAt) {
        this.StationID = stationID;
        this.StationName = stationName;
        this.Location = location;
        this.Capacity = capacity;
        this.CreatedAt = createdAt;
    }

    // Getter và Setter cho StationID
    public String getStationID() {
        return StationID;
    }

    public void setStationID(String stationID) {
        StationID = stationID;
    }

    // Getter và Setter cho StationName
    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    // Getter và Setter cho Location
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    // Getter và Setter cho Capacity
    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    // Getter và Setter cho CreatedAt
    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
