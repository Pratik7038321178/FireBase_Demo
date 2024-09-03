package com.c2w.admin;

public class Appointment {
    private String id;
    private String user;
    private String date;
    private String time;
    private String service;
    private String company;
    private String model;
    private String number;
    private String mobile;
    private String status; // New field

    public Appointment(String id, String user, String date, String time, String service, String company, String model, String number,String mobile, String status) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.time = time;
        this.service = service;
        this.company = company;
        this.model = model;
        this.number = number;
        this.mobile=mobile;
        this.status = status;
    }

    // Getters and setters for all fields including status
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
