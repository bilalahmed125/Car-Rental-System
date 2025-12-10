package com.bilal.models.vehicles;

import com.bilal.models.Vehicle;

public class Bike extends Vehicle{

    private int engineCC;
    private boolean helmetIncluded;
    
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
    //seter
    public void setEngineCC(int enginecc){
        this.engineCC = enginecc;
    }
    public void setHelmetIncluded(boolean helmet){
        this.helmetIncluded = helmet;
    }
}
