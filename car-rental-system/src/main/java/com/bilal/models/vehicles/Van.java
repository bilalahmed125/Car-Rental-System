package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Van extends Vehicle{

    private double cargoCapacityKg;

    //constructor
    public Van(String id, String make, String model, double rate, double cargoCapacityKg){
        super(id,make,model,rate);
        this.cargoCapacityKg = cargoCapacityKg;
    }

    //Getter
    public double getCargoCapacityKg(){return cargoCapacityKg;}

    //setter
    public void setCargoCapacityKg(double cargoCapacityKg){
        this.cargoCapacityKg = cargoCapacityKg;
    }
}
