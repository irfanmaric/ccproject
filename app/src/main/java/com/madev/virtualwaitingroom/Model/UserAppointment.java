package com.madev.virtualwaitingroom.Model;

public class UserAppointment {
    private String branchName;
    private String date;
    private String time;
    private String status;

    public UserAppointment() { }

    public UserAppointment(String branchName, String date, String time, String status) {
        this.branchName = branchName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
