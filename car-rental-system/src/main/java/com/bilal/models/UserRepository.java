package com.bilal.models;

import com.bilal.models.users.Customer;
import com.bilal.models.users.Admin;

import java.util.ArrayList;

public class UserRepository implements Repository<User>{


    //this will store all the users...
    private ArrayList<User> users = new ArrayList<>();


    //--------------------------methods-----------------------
    @Override
    public void add(User item){
        users.add(item);
    }

    @Override
    public void update(User item){
        for(int i = 0; i < users.size(); i++){      //this will run teh loop from 0 to size of arraylist.
            if(users.get(i).getUserId().equals(item.getUserId())){  //will get id from each of the arraylist's user and match with the id of the passed parameter User (item)
                users.set(i, item);                 //if the id is mathced, replaces the old user with the newer oone...
                return;                             //when done, will stop the method
            }
        }
    }

    @Override
    public void delete(User item){
        users.remove(item);
    }

    @Override
    public User getById(String id){
        for(User u : users){                   
            if(u.getUserId().equalsIgnoreCase(id)){
                return u;
            }
        }
        return null;                    // Not found
    }
    
    //method for registration(checks if the email is taken)
    public boolean isEmailTaken(String email){
        for(User u : users){
            if(u.getEmail().equalsIgnoreCase(email)){          //checsk the email of each user and matches it
                return true;                                    //if email gets matched it will return true
            }
        }
        return false;                                           //if no such email is taken , will return flase
    }

    //method for registration (checks if the phone number is taken)
    public boolean isPhoneTaken(String phone){
        for(User u : users){
            if(u.getPhone().equalsIgnoreCase(phone)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<User> getAll(){
        return new ArrayList<>(users);                      //returns a copy of the arraylist
    }

    //-----userStats----
    public String getUserStatistics(){
        int customerCount = 0;
        int adminCount = 0;

        for(User u : users){
            if(u instanceof Customer){              //checks which object is in the parent's reference
                customerCount++;
            }
            else if(u instanceof Admin){
                adminCount++;
            }
        }
        return "System Users: Total " + users.size()+ 
               " (Customers: " + customerCount + " | Admins: " + adminCount + ")";
    }
}