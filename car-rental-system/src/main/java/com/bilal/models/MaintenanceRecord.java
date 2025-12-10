package com.bilal.models;

public class MaintenanceRecord {
    private String description;
    private String date;

    //constructor
    public MaintenanceRecord(String description, String date){
        this.description = description;
        this.date = date;
    }

    //getters
    public String getDescription(){ return description; }
    public String getDate(){ return date; }

}
