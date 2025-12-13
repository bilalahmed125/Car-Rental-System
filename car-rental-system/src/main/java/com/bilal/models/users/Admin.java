package com.bilal.models.users;

import com.bilal.models.User;
import com.bilal.models.UserRepository;
import com.bilal.models.Vehicle;
import com.bilal.models.VehicleRepository;
import com.bilal.models.RentalRecord;

public class Admin extends User{
        
    private String role;
    private static int adminCount = 0;

    //constructor
    public Admin(String userId, String name, String email, String phone, String password, String role){
        super(userId,name,email,phone,password);
        this.role = role;
        adminCount++;           //increments total number of admins        
    }

    //Getter Setters
    public String getRole(){ return role; }
    public void setRole(String role){this.role = role;}
    public static int getAdminCount(){ return adminCount; }

    //------------VEHICLE MANAGEMENT BY ADMIN------------

    //ADd Vehicel
    public void addVehicle(VehicleRepository vrepo , Vehicle v){
        vrepo.add(v);
    }
    
    //Delete Vehicle
    public boolean removeVehicle(VehicleRepository vrepo , String vehicleId){
        Vehicle v = vrepo.getById(vehicleId);
        if( v != null && v.getAvailibility()){          //checks if the vehicle is not rented + shouldnot be null.
            vrepo.delete(v);
            return true;
        }
        return false;                                    //cannot delete if rented or null
    }

    //sets discounts (on vehicle rents)
    public void setDiscount(Vehicle v, double percent){
        v.applyDiscount(percent);
    }
    //removes discount (ont he vehicel), resets to the original price
    public void removeDiscount(Vehicle v){
        v.resetPrice();
    }
    //

    //ban user (if user is cancelling to many orders or any other reason)
    public void deleteUser(UserRepository repo, String userId) throws Exception
    {
        User u = repo.getById(userId);

        if(u instanceof Customer){
            Customer c = (Customer) u;

            for(RentalRecord r : c.getRentalHistory()){     //checks all the rental history(arraylist) from the customer one by one
                if(r.getStatus().equals("ACTIVE")){         //if any rentalRecrod has an acitve staus
                    throw new Exception("Cannot Delete Customer: They have an active Rental!");
                }
            }
        }
        repo.delete(u);     //deletes the user if he has no active rental.
    }

    //get the active rental of a customer
    public RentalRecord getActiveRental(Customer c){
        for(RentalRecord r : c.getRentalHistory()){
            if(r.getStatus().equals("ACTIVE")){
                return r;         //found an active renatal
            }
        }
        return null;            //found no active rental
    }



}
