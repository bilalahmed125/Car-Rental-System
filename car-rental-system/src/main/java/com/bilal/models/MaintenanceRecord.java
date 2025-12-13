package com.bilal.models;

import java.io.Serializable;
import java.time.LocalDate; 

public class MaintenanceRecord implements Serializable{
    private String description;
    private LocalDate date;
    private double cost;

    //constructor
    public MaintenanceRecord(String description, LocalDate date, double cost){
        this.description = description;
        this.date = date;
    }

    //getters
    public String getDescription(){ return description; }
    public LocalDate getDate(){ return date; }
    public double getCost(){return cost;}
    
    //setters
    public void setDescription(String des){this.description = des;}
    public void setDate(LocalDate date){this.date = date;}
    public void setCost(double cost){this.cost = cost;}

    //Methods
    public String getDetails(){
        return "Date: " + date + " | Description: " + description + " | Cost: "+cost; 
    }
}
