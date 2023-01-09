package com.example.mytravel;

public class NotificationData {
    String title, message, email, isRead, date, notificationID;

    public NotificationData(String title, String message, String email, String isRead, String date, String notificationID) {
        this.title = title;
        this.message = message;
        this.email = email;
        this.isRead = isRead;
        this.date = date;
        this.notificationID = notificationID;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getIsRead() {
        return isRead;
    }

    public String getDate() {
        return date;
    }

    public String getNotificationID() {
        return notificationID;
    }
}
