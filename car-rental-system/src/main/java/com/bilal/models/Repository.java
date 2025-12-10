package com.bilal.models;

import java.util.ArrayList;

public interface Repository<T> {
    //to add 
    void add(T item);

    //to update
    void update(T item);

    //to delete 
    void delete(T item);

    //get by ID
    void getById(String id);

    //get all
    ArrayList<T> getAll();
}
