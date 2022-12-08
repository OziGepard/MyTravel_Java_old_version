package com.example.mytravel;

import android.graphics.Bitmap;

public class DataClass {

    private String description;
    private Bitmap image;
    private int price;


    public DataClass(String description, Bitmap image, int price) {
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }
}
