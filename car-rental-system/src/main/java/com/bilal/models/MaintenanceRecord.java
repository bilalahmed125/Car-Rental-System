package com.bilal.models;

public class MaintenanceRecord {
    private String description;
    private String date;
    private double cost;

    //constructor
    public MaintenanceRecord(String description, String date, double cost){
        this.description = description;
        this.date = date;
    }

    //getters
    public String getDescription(){ return description; }
    public String getDate(){ return date; }
    public double getCost(){return cost;}
    
    //setters
    public void setDescription(String des){this.description = des;}
    public void setDate(String date){this.date = date;}
    public void setCost(double cost){this.cost = cost;}

    //Methods
    public String getDetails(){
        return "Date: " + date + " | Description: " + description + " | Cost: "+cost; 
    }
}
