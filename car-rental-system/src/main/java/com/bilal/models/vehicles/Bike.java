package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Bike extends Vehicle{

    private int engineCC;
    private boolean helmetIncluded;
    private static int bikeCount= 0;
    
    //constructor
    public Bike(String id, String make, String model, double rate,int engineCC, boolean hemlet){
        super(id,make,model,rate);
        this.engineCC = engineCC;
        this.helmetIncluded = hemlet;
    }

    //getter
    public int getEngineCC(){
        return engineCC;
    }
    public boolean getHelmetIncluded(){
        return helmetIncluded;
    }
    public static int getBikeCount(){ return bikeCount; } 

    //seter
    public void setEngineCC(int enginecc){
        this.engineCC = enginecc;
    }
    public void setHelmetIncluded(boolean helmet){
        this.helmetIncluded = helmet;
    }
}
