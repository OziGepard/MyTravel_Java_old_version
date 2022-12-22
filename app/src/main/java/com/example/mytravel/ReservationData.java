package com.example.mytravel;

public class ReservationData {

    private final String title;
    private final String reservationID;
    private final String isConfirmed;
    private final int rooms;
    private final int price;

    public ReservationData(String title, int rooms, int price, String reservationID, String isConfirmed) {
        this.title = title;
        this.rooms = rooms;
        this.price = price;
        this.reservationID = reservationID;
        this.isConfirmed = isConfirmed;
    }

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public String getTitle() {
        return title;
    }

    public String getReservationID() {
        return reservationID;
    }

    public int getRooms() {
        return rooms;
    }

    public int getPrice() {
        return price;
    }

}
