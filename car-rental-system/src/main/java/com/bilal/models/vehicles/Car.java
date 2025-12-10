package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Car extends Vehicle{
    
    private int numberOfDoors;

    //constructor
    public Car(String id, String make, String model, double rate, int doors){
        super(id,make,model,rate);
        this.numberOfDoors = doors;
    }

    //Getter
    public int getNumberOfDoors(){return numberOfDoors;}

    //setter
    public void setNumberOfDoors(int numberOfDoors){
        this.numberOfDoors = numberOfDoors;
    }
}
