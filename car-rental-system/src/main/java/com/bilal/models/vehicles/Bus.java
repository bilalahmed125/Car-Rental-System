package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Bus extends Vehicle{

    private int seatCapacity;
    private boolean hasAC;
    private static int busCount = 0;

    //constructor
    public Bus(String id, String make, String model, double rate, int seatCapacity, boolean hasAc){
        super(id,make,model,rate);
        this.seatCapacity = seatCapacity;
        this.hasAC = hasAc;
    }

    //Getter
    public int getSeatCapacity(){ return seatCapacity; }
    public boolean getHasAC(){ return hasAC; }
    public static int getBusCount(){ return busCount; } 

    //setter
    public void setSeatCapacity(int seatCapacity){
        this.seatCapacity = seatCapacity;
    }
    public void setHasAC(boolean hasAC){ this.hasAC = hasAC; }

}
