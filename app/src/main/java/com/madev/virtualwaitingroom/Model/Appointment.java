package com.madev.virtualwaitingroom.Model;

public class Appointment {
    private String timeframe;
    private String isAvaliable;

    public Appointment() {
    }

    public Appointment(String timeframe, String isAvaliable) {
        this.timeframe = timeframe;
        this.isAvaliable = isAvaliable;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public String getIsAvaliable() {
        return isAvaliable;
    }

    public void setIsAvaliable(String isAvaliable) {
        this.isAvaliable = isAvaliable;
    }
}
