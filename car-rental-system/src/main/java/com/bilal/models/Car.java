package com.bilal.models;

public class Car extends Vehicle{
    
    private int numberOfDoors;

    public Car(String id, String make, String model, double rate, int doors){
        super(id,make,model,rate);
        this.numberOfDoors = doors;
    }

    @Override
    public double calculateRentalCost(int days){
        return getBaseRatePerDay() * days;
    }

    @Override
    public String getCurrentLocation(){
        return this.getCurrentLocation();
    }
    
    @Override
    public void updateLocation(String gps){
        setCurrentLocation(gps);
    }
}
