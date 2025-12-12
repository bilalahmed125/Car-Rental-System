package com.bilal.models;

public abstract class User{
    private String name;
    private String email;
    private String userId;
    private String phone;
    private String password;

    //constructor
    public User(String userId, String name, String email, String phone, String password){
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.phone = phone;
    }
    
    //setters
    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    //no user id becasue the rental record saves according to userId, so any id change could lead to seroious issues. 
    
    //getters
    public String getName(){ return name;}
    public String getEmail(){ return email; }
    public String getUserId(){ return userId;}
    public String getPassword(){return password;} 
    public String getPhone(){return phone; }

    //methods
    public boolean updatePassword(String pass){
        //will only set password if the length of the pasword is >6
        if(pass.length()>6){ 
            this.password = pass;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean login(String pass){
        //returns true if passowrd matches else false.
        return (password.equals(pass)) ? true : false;
    }

}
