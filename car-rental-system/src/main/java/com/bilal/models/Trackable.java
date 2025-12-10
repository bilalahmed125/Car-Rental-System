package com.bilal.models;

public interface Trackable {
    
        //methods (all abstract)
    //will get the current location ofthe vehicle
    String getCurrentLocation();
    //updates the current location
    void updateLocation(String location);
    
}
