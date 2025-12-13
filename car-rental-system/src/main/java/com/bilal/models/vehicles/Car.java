package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Car extends Vehicle{
    
    private int numberOfDoors;
    private String transmissionType;
    private static int carCount= 0;

    //constructor
    public Car(String id, String make, String model, double rate, int doors,String transmissionType){
        super(id,make,model,rate);
        this.numberOfDoors = doors;
        this.transmissionType = transmissionType;
    }

    //Getter
    public int getNumberOfDoors(){return numberOfDoors;}
    public String getTransmissionType(){ return transmissionType; }
    public static int getCarCount(){ return carCount; } 
    
    //setter
    public void setNumberOfDoors(int numberOfDoors){
        this.numberOfDoors = numberOfDoors;
    }
    public void setTransmissionType(String transmissionType){
        this.transmissionType = transmissionType;
    }
}
