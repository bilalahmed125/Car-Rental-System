package com.bilal.models;

import java.util.ArrayList;

public abstract class Vehicle implements Rentable,Trackable{

    private String vehicleId;
    private String make;
    private String model;
    private boolean isAvailable;
    private double baseRatePerDay;
    private String currentLocation;

    //composing Maintenance record(just a ref here)
    private ArrayList<MaintenanceRecord> maintenanceHistory ;

    // constructor 
    public Vehicle(String id, String make, String model, double rate){
        this.vehicleId = id;
        this.make = make;
        this.model = model;
        this.baseRatePerDay = rate;
        this.isAvailable = true;                            //defautl true
        this.maintenanceHistory = new ArrayList<>();        //array list creted here
    }

            //setter geetters
    //setters
    public void setVehicleId(String id){ this.vehicleId = id; }
    public void setMake(String make){ this.make = make; }
    public void setModel(String model){ this.model = model;}
    public void setBaseRatePerDay(double rate){this.baseRatePerDay = rate;}
    public void setIsAvilable(boolean avl ){ this.isAvailable = avl; }
    public void setCurrentLocation(String gps){ this.currentLocation = gps;}

    //getters
    public String getVehicleId(){ return vehicleId; }
    public String getMake(){ return make; }
    public String getModel(){ return model; }
    public double getBaseRatePerDay(){ return baseRatePerDay; }
    public boolean getAvailibility(){ return isAvailable; }
    public String getCurrentLocation(){return currentLocation;} 


    //methods
    public void addMaintenanceRecord(String description, String date){
        //creating object of maintenance recrod 
        MaintenanceRecord record = new MaintenanceRecord(description,date);
        //passed the object to the ArrayLISt
        this.maintenanceHistory.add(record);        
    }

    @Override
    public boolean isAvailableForRental(){
        return isAvailable;
    }
    @Override
    public void rent(){
        if(isAvailable){
            this.isAvailable = false;
        }
    }
    @Override
    public void returnVehicle(){
        this.isAvailable = true;
    }

    @Override
    public String toString(){
        return make+ " "+ model+ " ("+ (isAvailable ? "Available" : "Rented")+ ")";
    }

}