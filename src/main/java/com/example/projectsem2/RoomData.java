package com.example.projectsem2;

public class RoomData {
    private String roomNumber;
    private String roomType;
    private String status;
    private Integer price;


    public RoomData(String roomNumber, String roomType, String status, Integer price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
        this.price = price;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getStatus() {
        return status;
    }

    public Integer getPrice() {
        return price;
    }
}
