package com.bilal.models;

public interface Rentable {
            //methods (all abstract)
    
    //will calculate the rental cost acc to days
    double calculateRentalCost(int days);
    //will check if the car is available for rent
    boolean isAvailableForRental();
    //will rent out the car
    void rent();
    //will get the car back
    void returnVehicle();
    
}
