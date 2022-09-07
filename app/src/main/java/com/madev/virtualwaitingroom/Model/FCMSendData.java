package com.madev.virtualwaitingroom.Model;

import java.util.Map;

public class FCMSendData {
    public String to;
    public Notification notification;

    public FCMSendData(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
