package com.bilal.models;

import com.bilal.models.vehicles.*; // important to use car,bus.van,bike
import java.util.ArrayList;

public class VehicleRepository implements Repository<Vehicle>{
    
    //this will hold every vehicle, for us... 
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    ///-----------------------methods-------------------------- 
    @Override
    public void add(Vehicle item){
        vehicles.add(item);
    }

    @Override
    public void update(Vehicle item){           
        for(int i = 0; i < vehicles.size(); i++){       //will run teh loop form 0 to size of arraylist, and check each of the arraylist's objects (stored) id
            if(vehicles.get(i).getVehicleId().equals(item.getVehicleId())){     //if the Vehicle's id matches with the item 
                vehicles.set(i, item);                                          // it will replace the vehicle with it.
                return;                                                         //stops the method if done
            }
        }
    }

    @Override
    public void delete(Vehicle item){
        vehicles.remove(item);
    }

    @Override
    public Vehicle getById(String id){
        for(Vehicle v : vehicles){
            if(v.getVehicleId().equalsIgnoreCase(id)){
                return v;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Vehicle> getAll(){
        return new ArrayList<>(vehicles); //this will return a copy of the array list
    }

    // ---This will tell how many and vehicles(childs) are avaialbel, rented, and in total---
    public String getFleetStatistics(){
        int carTotal = 0, carAvail = 0, carRented = 0;      //for cars
        int busTotal = 0, busAvail = 0, busRented = 0;      //fro bus
        int vanTotal = 0, vanAvail = 0, vanRented = 0;      //for vans
        int bikeTotal = 0, bikeAvail = 0, bikeRented = 0;   //for bikes

        for(Vehicle v : vehicles){
            boolean isFree = v.getAvailibility();

            if(v instanceof Car){                  //this checks that what is the object in reality?
                carTotal++;
                if(isFree) carAvail++;           //isFree tells that car is available else rented
                else carRented++;
            } 
            else if(v instanceof Bus){
                busTotal++;
                if(isFree) busAvail++;
                else busRented++;
            }
            else if(v instanceof Van){
                vanTotal++;
                if(isFree) vanAvail++; 
                else vanRented++;
            }
            else if(v instanceof Bike){
                bikeTotal++;
                if(isFree) bikeAvail++;
                else bikeRented++;
            }
        }
        //this will then return the fleet report(total(cars,bus,bikes,vans) , rented , and available)
        return "\t---FLEET REPORT---\n" +
               "Cars : Total " + carTotal + " | Available " + carAvail + " | Rented " + carRented + "\n" +
               "Buses: Total " + busTotal + " | Available " + busAvail + " | Rented " + busRented + "\n" +
               "Vans : Total " + vanTotal + " | Available " + vanAvail + " | Rented " + vanRented + "\n" +
               "Bikes: Total " + bikeTotal + " | Available " + bikeAvail + " | Rented " + bikeRented + "\n";
    }
}