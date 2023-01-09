package com.example.mytravel;

public class EmployeeData {
    private final String title;
    private final String ID;

    public EmployeeData(String title, String ID) {
        this.title = title;
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return ID;
    }
}
