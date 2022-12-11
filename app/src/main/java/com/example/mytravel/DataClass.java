package com.example.mytravel;

public class DataClass {

    private String title;
    private String imageID;
    private String dateOut;
    private String dateIn;
    private String offerID;
    private int price;
    private int rooms;
    private int availableRooms;

    public DataClass(String title, String imageID, int price, int rooms,  String dateOut, String dateIn, String offerID, int availableRooms) {
        this.title = title;
        this.imageID = imageID;
        this.price = price;
        this.rooms = rooms;
        this.dateOut = dateOut;
        this.dateIn = dateIn;
        this.offerID = offerID;
        this.availableRooms = availableRooms;
    }

    public String getTitle() {
        return title;
    }

    public String getImageID() {
        return imageID;
    }

    public String getOfferID() {
        return offerID;
    }

    public int getPrice() {
        return price;
    }

    public String getDateOut() {
        return dateOut;
    }

    public String getDateIn() {
        return dateIn;
    }

    public int getRooms() {
        return rooms;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}
