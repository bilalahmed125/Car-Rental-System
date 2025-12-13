package com.bilal.models;

import java.util.ArrayList;

public class RentalRecordRepository implements Repository<RentalRecord>{

    // this is the whole storage for rentalrecords which will hold all the rental records
    private ArrayList<RentalRecord> records = new ArrayList<>();

    ///-----------------------methods-------------------------- 
    @Override
    public void add(RentalRecord item){
        records.add(item);
    }

    @Override
    public void update(RentalRecord item){
        for(int i = 0; i < records.size(); i++){        //will run teh loop form 0 to size of arraylist, and check each of the arraylist's objects(stored) id
            if(records.get(i).getRentalId().equals(item.getRentalId())){    //if the Vehicle's id matches with the item 
                records.set(i, item);                                       // it will replace the vehicle with it
                return;                                                 //stops the method if done
            }
        }
    }
    @Override
    public void delete(RentalRecord item){
        records.remove(item);
    }

    @Override
    public RentalRecord getById(String id){
        for(RentalRecord r : records){
            if(r.getRentalId().equalsIgnoreCase(id)){
                return r;
            }
        }
        return null;
    }

    @Override
    public ArrayList<RentalRecord> getAll(){
        return new ArrayList<>(records);
    }

    //----SPECIAIL FILTERS(best for admin)-----

    //Get all currently active rentals
    public ArrayList<RentalRecord> getActiveRentals(){
        ArrayList<RentalRecord> active = new ArrayList<>();
        for(RentalRecord r : records){
            if(r.getStatus().equals("ACTIVE")){        //if the renatal status is active
                active.add(r);                          //then add to the arraylist created
            }
        }
        return active;                                  //returns the array list
    }

    //Get all overdue rentals(Late cars)
    public ArrayList<RentalRecord> getOverdueRentals(){
        ArrayList<RentalRecord> overdue = new ArrayList<>();
        for(RentalRecord r : records){
            r.checkOverDue();                       //this method is inside rentalRecord and it will set overdue to those which are overdue

            if(r.getStatus().equals("OVERDUE")){   //this will check the ones that are already overdue
                overdue.add(r);                     //then add to the arraylist
            }
        }
        return overdue;                             //returns the arraylist
    }
}