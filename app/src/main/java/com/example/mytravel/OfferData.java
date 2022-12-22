package com.example.mytravel;

public class OfferData {

    private final String title;
    private final String imageID;
    private final String dateOut;
    private final String dateIn;
    private final String offerID;
    private final String description;
    private final int price;
    private final int rooms;
    private final int availableRooms;

    public OfferData(String title, String imageID, int price, int rooms, String dateOut, String dateIn, String offerID, int availableRooms, String description) {
        this.title = title;
        this.imageID = imageID;
        this.price = price;
        this.rooms = rooms;
        this.dateOut = dateOut;
        this.dateIn = dateIn;
        this.offerID = offerID;
        this.availableRooms = availableRooms;
        this.description = description;
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

    public String getDateOut() {
        return dateOut;
    }

    public String getDateIn() {
        return dateIn;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getRooms() {
        return rooms;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}
