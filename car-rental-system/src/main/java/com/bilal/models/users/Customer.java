package com.bilal.models.users;

import java.util.ArrayList;

import com.bilal.models.RentalRecord;
import com.bilal.models.User;

public class Customer extends User{
    
    private String licenseNumber;
    private String address;
    private static int customerCount = 0;               //static counter
    private ArrayList<RentalRecord> rentalHistory;      //reference for aggregation 

    public Customer(String userId, String name, String email, String phone, String password, String licenseNumber, String address){
        super(userId,name,email,phone,password);
        this.licenseNumber = licenseNumber;
        this.address = address;
        this.rentalHistory = new ArrayList<>();
        customerCount++;
    }

    //Getters:
    public String getLicenseNumber(){ return licenseNumber; }
    public String getAddress(){ return address; }
    public static int getCustomerCount(){ return customerCount;}

    //Setters;
    public void setLicenseNumber(String licenseNumber){ this.licenseNumber = licenseNumber; }
    public void setAddress( String address ){ this.address = address; }

                    //methods:
    public void addRentalRecord(RentalRecord record){
        this.rentalHistory.add(record);
    }
    public void removeRentalRecord(RentalRecord recrod){
        this.rentalHistory.remove(recrod);
    }
    //returns a copy of ARrayList;
    public ArrayList<RentalRecord> getRentalHistory(){
        return new ArrayList<>(rentalHistory);
    }

}
