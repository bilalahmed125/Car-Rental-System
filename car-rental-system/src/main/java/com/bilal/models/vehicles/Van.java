package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Van extends Vehicle{

    private double cargoCapacityKg;
    private static int vanCount= 0;

    //constructor
    public Van(String id, String make, String model, double rate, double cargoCapacityKg){
        super(id,make,model,rate);
        this.cargoCapacityKg = cargoCapacityKg;
    }

    //Getter
    public double getCargoCapacityKg(){return cargoCapacityKg;}
    public static int getVanCount(){ return vanCount; } 

    //setter
    public void setCargoCapacityKg(double cargoCapacityKg){
        this.cargoCapacityKg = cargoCapacityKg;
    }
}
