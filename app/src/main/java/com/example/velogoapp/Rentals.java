package com.example.velogoapp;

import java.io.Serializable;

public class Rentals implements Serializable {

    private String rentalID; // ID của giao dịch thuê (INTEGER PRIMARY KEY AUTOINCREMENT)
    private String userID; // ID của người dùng (INTEGER NOT NULL, liên kết với Users)
    private String bicycleID; // ID của xe đạp (INTEGER NOT NULL, liên kết với Bicycles)
    private String stationID; // ID của trạm (INTEGER NOT NULL, liên kết với RentalStations)
    private String rentalStart; // Thời gian bắt đầu thuê (DATETIME NOT NULL)
    private String rentalEnd; // Thời gian kết thúc thuê (DATETIME, có thể null)
    private String totalCost; // Tổng chi phí (REAL, có thể null)
    private String status; // Trạng thái của giao dịch (TEXT: 'Đang thực hiện', 'Hoàn thành', 'Đã hủy')
    private String bicycleName;

    public String getBicycleName() {
        return bicycleName;
    }

    public void setBicycleName(String bicycleName) {
        this.bicycleName = bicycleName;
    }

    // Constructor
    public Rentals(String bicycleID, String bicycleName, String rentalID, String userID, String stationID, String rentalStart,
                   String rentalEnd, String totalCost, String status) {
        this.bicycleID = bicycleID;
        this.bicycleName = bicycleName;
        this.rentalID = rentalID;
        this.userID = userID;
        this.stationID = stationID;
        this.rentalStart = rentalStart;
        this.rentalEnd = rentalEnd;
        this.totalCost = totalCost;
        this.status = status;

    }

    // Getter và Setter
    public String getRentalID() {
        return rentalID;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBicycleID() {
        return bicycleID;
    }

    public void setBicycleID(String bicycleID) {
        this.bicycleID = bicycleID;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(String rentalStart) {
        this.rentalStart = rentalStart;
    }

    public String getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(String rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString() để hiển thị thông tin đối tượng Rentals
    @Override
    public String toString() {
        return "Rentals{" +
                "rentalID='" + rentalID + '\'' +
                ", userID='" + userID + '\'' +
                ", bicycleID='" + bicycleID + '\'' +
                ", stationID='" + stationID + '\'' +
                ", rentalStart='" + rentalStart + '\'' +
                ", rentalEnd='" + rentalEnd + '\'' +
                ", totalCost='" + totalCost + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
