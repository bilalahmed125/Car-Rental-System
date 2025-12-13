package com.bilal.models;

import com.bilal.models.users.*;
import com.bilal.models.vehicles.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class CarRentalSystem{

    //-----repos(our storage)-------
    private VehicleRepository vehicleRepo;
    private UserRepository userRepo;
    private RentalRecordRepository rentalRepo;

    //constructor
    public CarRentalSystem(){
        //compose the repositries
        this.vehicleRepo = new VehicleRepository();
        this.userRepo = new UserRepository();
        this.rentalRepo = new RentalRecordRepository();

        //trying to  load data immediatly on startup
        loadData();
    }


    //1. ===============Registration logic==============


    /*
     * registers a new car.
     * throws exception if the ID already exist
     */

    public void registerCar(String id, String make, String model, double rate,int doors, String transmissionType) throws Exception{
        //this will check if the id is already linkd to other vehicel
        if(vehicleRepo.getById(id) != null){                                    //this will try to get a vehicle with same id if there is a vehicel with same id then throws an exception bcz id exists already
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }

        //will only create the Car object if the id is unique
        Car newCar = new Car(id, make, model, rate, doors,transmissionType);

        //at the end it will store it to the arraylist in vehicle repository
        vehicleRepo.add(newCar);
        saveData(); //autosave
    }

    /*
     * registers a new Bike.
     * throws exception if the ID already exist
     * works ont he same logic as with car 
    */
        //for the Bike
    public void registerBike(String id, String make, String model, double rate, int engineCC, boolean helmet) throws Exception{
        if(vehicleRepo.getById(id) != null){            //if same id bike spoted , throws expetion
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }
        //only creates a bike if no same id is found
        Bike newBike = new Bike(id, make, model, rate, engineCC, helmet);
        vehicleRepo.add(newBike);   //adds to the vehicle repo
        saveData();                 //saves
    }
    
    // for the BUS (same logic is pasted for bus as for bike and car)
    public void registerBus(String id, String make, String model, double rate,int seatCapacity, boolean hasAC) throws Exception{
        //this will check if the id is already linkd to other vehicel
        if(vehicleRepo.getById(id) != null){                                    //this will try to get a vehicle with same id if there is a vehicel with same id then throws an exception bcz id exists already
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }

        //will only create the BUS object if the id is unique
        Bus newBus = new Bus(id, make, model, rate, seatCapacity, hasAC);

        //at the end it will store it to the arraylist in vehicle repository
        vehicleRepo.add(newBus);
        saveData(); //autosave
    }

    //for van (same logic as beofre)
    public void registerVan(String id, String make, String model, double rate,double cargoCapacityKg) throws Exception{
        //this will check if the id is already linkd to other vehicel
        if(vehicleRepo.getById(id) != null){                                    //this will try to get a vehicle with same id if there is a vehicel with same id then throws an exception bcz id exists already
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }

        //will only create the VAN object if the id is unique
        Van newVan = new Van(id, make, model, rate, cargoCapacityKg);

        //at the end it will store it to the arraylist in vehicle repository
        vehicleRepo.add(newVan);
        saveData(); //autosave
    }

    //=================================CUSTOMER MAKING AND VALIDATION===========-----------------------=
    /*
     * registers a new customer
     * throws ecxeption if ID or email,phone is taken
     */

    public void registerCustomer(String id, String name, String email, String phone, String password, String lic, String addr) throws Exception {
        // checks the mathing id or emial
        if(userRepo.getById(id) != null){                               //if user with same id exists , throw exception
            throw new IllegalArgumentException("User ID '" + id + "' is already registered.");
        }
        if(userRepo.isEmailTaken(email)){                               //if user with same email exists, thwros exception
            throw new IllegalArgumentException("The email '" + email + "' is already in use.");
        }
        if(userRepo.isPhoneTaken(phone)){                           //if user has enterd phone number already taken , throw expetion
            throw new IllegalArgumentException("This Phone '"+phone+"' is already in use.");
        }

        //creates a customer if has unique email,id and phone
        Customer newCust = new Customer(id, name, email, phone, password, lic, addr);

        //stores in arraylist in userrepo
        userRepo.add(newCust);
        saveData();     ///saves
    }


    //2. ==========================RENTAL LOGIC======================


    public void rentVehicle(String customerId, String vehicleId, LocalDate rentalDate, int days) throws Exception{
        //we will get the obejcts first using the ids
        User user = userRepo.getById(customerId);
        Vehicle vehicle = vehicleRepo.getById(vehicleId);

        //we will maek sure they are not null 
        if(user == null || !(user instanceof Customer)){                    //will check and make sure that user is not null + user is user only not admin
            throw new IllegalArgumentException("Customer not found or invalid User ID.");       //throws exception if any of the above is true
        }
        if(vehicle == null){                                            //will do the same for vehicle.
            throw new IllegalArgumentException("Vehicle not found.");   //thwors exception if vehicle is not found.
        }

        //then we will chcek the availibilty, of the vehicel    
        if(!vehicle.getAvailibility()){                                 //if vehicle is not available throw exception
            throw new IllegalArgumentException("Vehicle '" + vehicle.getModel() + "' is currently rented out.");    
        }

        //if everything is sound, then we wil process with the renting
        Customer customer =(Customer) user;
        LocalDate returnDate = rentalDate.plusDays(days); 
        //generates a simple unique id for the record(like; R-User-Vehicle-Date)
        String rentalId = "R-" + System.currentTimeMillis(); //this will generate an id LIke R- _here the time in miliseconds from 1970 , 1st jan_

        // The Constructor of RentalRecord handles:
        // - Setting vehicle.isAvailable = false
        // - Adding record to Customer's history
        RentalRecord record = new RentalRecord(rentalId, customer, vehicle, rentalDate, returnDate);

        //saves the renteal record
        rentalRepo.add(record);
        saveData();
    }

    public void returnVehicle(String rentalId) throws Exception{
        //will get the rental record
        RentalRecord record = rentalRepo.getById(rentalId);

        if(record == null){                         //if there is no matching rental record, then throws exception 
            throw new IllegalArgumentException("Rental Record not found.");
        }
        
        if(record.getStatus().equals("COMPLETED")){              //if the rental has already been completed (returnd already)
            throw new IllegalArgumentException("This rental is already completed.");
        }

        //after the checks, this will return if clear
        record.returnVehicle(LocalDate.now());      // Sets status, calculates fees, frees car
        
        saveData(); 
    }

    //3.================================================FILE HANDLING===============================
    
    private void saveData(){    
        try{
            //create the file and wrtigin stream
            FileOutputStream fos = new FileOutputStream("car_rental_data.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            //now we will write the lists to the file.
            oos.writeObject(vehicleRepo.getAll());        //thsi will return the list of vehicel from VehicleRepository.
            oos.writeObject(userRepo.getAll());           //this will return the list of Users from the UserRepository.
            oos.writeObject(rentalRepo.getAll());         //this will reutn the renatl record from the REntalRecordRepository.
            
            //closeing is must
            oos.close();
            fos.close();

        }catch(IOException e){
            System.out.println("SAVING DATA IN FILE FAILED!!");
        }   
    }


    @SuppressWarnings("unchecked")          //this is what compiler used for quick fix, before this it was showing typecast safety thing
    private void loadData(){
        File file = new File("car_rental_data.dat");        //this will just check the file and store it inside file... wont open it in any mode or something....
        if (!file.exists()) return;                         // Stop if file doesn't exist

        try{
            //Creating a stream here
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ///read objects(order must match saveData)
            List<Vehicle> vList = (List<Vehicle>) ois.readObject();     //read the whole list of object from the file.
            List<User> uList = (List<User>) ois.readObject();           //read the whole list of object from the flle
            List<RentalRecord> rList = (List<RentalRecord>) ois.readObject();   //same as above

            //populate the repos
            for (Vehicle v : vList) vehicleRepo.add(v);     //this will write all the data read from the files into the actual arraylists in our program
            for (User u : uList) userRepo.add(u);           // saem as above
            for (RentalRecord r : rList) rentalRepo.add(r); // same as above

            //close is a must.
            ois.close();
            fis.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    //4, ACCESSORS(for the GUI)

    public VehicleRepository getVehicleRepo(){ return vehicleRepo;}
    public UserRepository getUserRepo(){ return userRepo;}
    public RentalRecordRepository getRentalRepo(){return rentalRepo; }
}