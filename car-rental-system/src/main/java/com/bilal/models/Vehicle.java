package com.bilal.models;

import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Vehicle implements Rentable,Serializable{

    private String vehicleId;
    private String make;
    private String model;
    private boolean isAvailable;
    private double baseRatePerDay;

    //Discount Fields
    private double currentRate; 
    private boolean isDiscountActive;

    //composing Maintenance record(just a ref here)
    private ArrayList<MaintenanceRecord> maintenanceHistory ;

    // constructor 
    public Vehicle(String id, String make, String model, double rate){
        this.vehicleId = id;
        this.make = make;
        this.model = model;
        this.baseRatePerDay = rate;
        this.currentRate = rate;
        this.isAvailable = true;                            //defautl true
        this.maintenanceHistory = new ArrayList<>();        //array list creted here
    }

            //setter geetters
    //setters
    public void setMake(String make){ this.make = make; }
    public void setModel(String model){ this.model = model;}
    public void setBaseRatePerDay(double rate){this.baseRatePerDay = rate;}
    public void setIsAvailable(boolean avl ){ this.isAvailable = avl; }
    

    //getters
    public String getVehicleId(){ return vehicleId; }
    public String getMake(){ return make; }
    public String getModel(){ return model; }
    public double getBaseRatePerDay(){ return baseRatePerDay; }
    public boolean getAvailibility(){ return isAvailable; }
    public double getCurrentRate(){ return currentRate; }
    public boolean getIsDiscountAvailable(){ return isDiscountActive; }

    //methods
    public void addMaintenanceRecord(String description, LocalDate date,double cost){
        //creating object of maintenance recrod 
        MaintenanceRecord record = new MaintenanceRecord(description,date,cost);
        //passed the object to the ArrayLISt
        this.maintenanceHistory.add(record);        
    }

    // DICOUNT can be applied on a certain vehicel
    public void applyDiscount(double percent){
        if(percent > 0 && percent <= 100){
            double discountAmount = baseRatePerDay * (percent / 100.0);
            this.currentRate = baseRatePerDay - discountAmount;
            this.isDiscountActive = true;
        }
    }

    //Reset the discount to normal price
    public void resetPrice(){
        this.currentRate = baseRatePerDay;
        this.isDiscountActive = false;
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
    public double calculateRentalCost(int days){
        return currentRate * days;
    } 
    
    /*toString() is a method from Object calss, and we basically manipulate how the print msg will be wehn we try to sout(Vehicle/Car), 
    we can customize the printed msg instad of address to what we return from this method */
    @Override
    public String toString(){
        return make+ " "+ model+ " ("+ (isAvailable ? "Available" : "Rented")+ ")"; //ternary if else is availble true the first msg prints else the second.
    }

}