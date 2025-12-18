package com.bilal.models;

import com.bilal.models.Payment.CardPayment;
import com.bilal.models.users.*;
import com.bilal.models.vehicles.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
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


    
    /* registers a new car.
     * throws exception if the ID already exist
     */

    public void registerCar(String id, String make, String model, double rate,int doors, String transmissionType) throws Exception{
        //checks that the inputs cannot be null
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }
        if(make == null || make.trim().isEmpty()){
            throw new IllegalArgumentException("Make cannot be empty.");
        }
        if(model == null || model.trim().isEmpty()){
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if(rate <= 0){
            throw new IllegalArgumentException("Rate must be positive.");
        }
        
        //this will check if the id is already linkd to other vehicel
        if(vehicleRepo.getById(id) != null){                                    //this will try to get a vehicle with same id if there is a vehicel with same id then throws an exception bcz id exists already
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }

        //will only create the Car object if the id is unique
        Car newCar = new Car(id, make, model, rate, doors,transmissionType);

        //at the end it will store it to the arraylist in vehicle repository
        vehicleRepo.add(newCar);
        saveData();                 //saves to the file
    }

    /*
     * registers a new Bike.
     * throws exception if the ID already exist
     * works ont he same logic as with car 
    */
        //for the Bike
    public void registerBike(String id, String make, String model, double rate, int engineCC, boolean helmet) throws Exception{
        //checks that the inputs cannot be null
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }
        if(make == null || make.trim().isEmpty()){
            throw new IllegalArgumentException("Make cannot be empty.");
        }
        if(model == null || model.trim().isEmpty()){
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if(rate <= 0){
            throw new IllegalArgumentException("Rate must be positive.");
        }
        
        if(vehicleRepo.getById(id) != null){            //if same id bike spoted , throws expetion
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }
        //only creates a bike if no same id is found
        Bike newBike = new Bike(id, make, model, rate, engineCC, helmet);
        vehicleRepo.add(newBike);   //adds to the vehicle repo
        saveData();                 //saves to the file
    }
    
    // for the BUS (same logic is pasted for bus as for bike and car)
    public void registerBus(String id, String make, String model, double rate,int seatCapacity, boolean hasAC) throws Exception{
        //checks that the inputs cannot be null
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }
        if(make == null || make.trim().isEmpty()){
            throw new IllegalArgumentException("Make cannot be empty.");
        }
        if(model == null || model.trim().isEmpty()){
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if(rate <= 0){
            throw new IllegalArgumentException("Rate must be positive.");
        }
        
        //this will check if the id is already linkd to other vehicel
        if(vehicleRepo.getById(id) != null){                                    //this will try to get a vehicle with same id if there is a vehicel with same id then throws an exception bcz id exists already
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }

        //will only create the BUS object if the id is unique
        Bus newBus = new Bus(id, make, model, rate, seatCapacity, hasAC);

        //at the end it will store it to the arraylist in vehicle repository
        vehicleRepo.add(newBus);
        saveData();                         //saves to the file
    }

    //for van (same logic as beofre)
    public void registerVan(String id, String make, String model, double rate,double cargoCapacityKg) throws Exception{
        //checks that the inputs cannot be null
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }
        if(make == null || make.trim().isEmpty()){
            throw new IllegalArgumentException("Make cannot be empty.");
        }
        if(model == null || model.trim().isEmpty()){
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if(rate <= 0){
            throw new IllegalArgumentException("Rate must be positive.");
        }
        
        //this will check if the id is already linkd to other vehicel
        if(vehicleRepo.getById(id) != null){                                    //this will try to get a vehicle with same id if there is a vehicel with same id then throws an exception bcz id exists already
            throw new IllegalArgumentException("Vehicle ID '" + id + "' is already taken.");
        }

        //will only create the VAN object if the id is unique
        Van newVan = new Van(id, make, model, rate, cargoCapacityKg);

        //at the end it will store it to the arraylist in vehicle repository
        vehicleRepo.add(newVan);
        saveData();                             //saves to the file
    }

    //=================================CUSTOMER MAKING AND VALIDATION===========-----------------------=
    /*
     * registers a new customer
     * throws ecxeption if ID or email,phone is taken
     */

    public void registerCustomer(String id, String name, String email, String phone, String password, String lic, String addr) throws Exception {
        
        //checks that the input is valid and not empty or unwanted
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("User ID cannot be empty.");
        }
        //name cannot be null
        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        // email cannot be null
        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        //will check that email must have @ sgin
        if(!email.contains("@")){
            throw new IllegalArgumentException("Invalid email format.");
        }
        //will ceheck that phone shouldnot be null
        if(phone == null || phone.trim().isEmpty()){
            throw new IllegalArgumentException("Phone cannot be empty.");
        }
        //it will check if the password is longer than 6 cahracters
        if(password == null || password.length()<=6){
            throw new IllegalArgumentException("Password Must be longer than 6 characters!");
        }

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
        saveData();                 ///saves to the file
    }

    //3.------------delete vehicle(with confirmation)-----------------------
    public boolean deleteVehicle(String vehicleId, boolean forceDelete) throws Exception{
        Vehicle v = vehicleRepo.getById(vehicleId);     //will get the vehicle with the id
        
        //vehicle must exist and cannot be null      
        if(v == null){
            throw new IllegalArgumentException("Vehicle not found.");
        }
        
        //vehicle must not be currently rented or else cannot delete now
        if(!v.getAvailibility()){               //checks the availability of the vehicle
            throw new IllegalArgumentException("Cannot delete because Vehicle is currently rented out.");
        }
        
        //collecst all rental records for this vehicle
        ArrayList<RentalRecord> vehicleRecords = new ArrayList<>();
        for(RentalRecord r : rentalRepo.getAll()){                      //checks the records one by one from the arraylist
            if(r.getRentedVehicle().getVehicleId().equals(vehicleId)){  //if the vehicle is found in any record meaning it has a rental history
                vehicleRecords.add(r);                                   //ADD to the list
            }
        }
        
        //if has history and user didn't confirm, returns false (GUI will show alert)
        if (!vehicleRecords.isEmpty() && !forceDelete){
            return false;  //signals GUI to show confirmation dialogue
        }
        
        //If forceDelete = true or it has no history, proceed with deletion
        
        //deletes all the rental records associated with the vehicle
        for (RentalRecord r : vehicleRecords) {
            //remove from customers' history
            r.getCustomer().removeRentalRecord(r);
            //remove from rental repository
            rentalRepo.delete(r);
        }
        
        //delete the vehicle
        vehicleRepo.delete(v);
        saveData();                         //save to file
        return true;                        //returns true
    }


    //2. ==========================RENTAL LOGIC======================

    public void rentVehicle(String customerId, String vehicleId, LocalDate rentalDate, int days, Payable payment) throws Exception{
        
                //Input validations cheks
            //customer id cant be null
        if(customerId == null || customerId.trim().isEmpty()){
            throw new IllegalArgumentException("Customer ID cannot be empty.");
        }
        //vehicle id cant be null.
        if(vehicleId == null || vehicleId.trim().isEmpty()){
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }//payment cant be null.
        if(payment == null){
            throw new IllegalArgumentException("Payment method cannot be null.");
        }
        //we check that the rental date cannot be before today's date;
        if(rentalDate==null || rentalDate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Rental Date Cannot be in the past!");
        }
        //rental days cannot be 0 or negative have to be atleast 1
        if(days<=0){
            throw new IllegalArgumentException("Rental Days must be atleast 1 !");
        }

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
        
        //estimated cost;
        double estimatedCost = vehicle.calculateRentalCost(days);

        //process the payment
        boolean isPaid = payment.processPayment(estimatedCost);
        if( !isPaid ){
            //if the payment fails, no car is given
            throw new IllegalArgumentException("Transaction Failed:  "+payment.getPaymentDetails());
        }
        
        //generates a simple unique id for the record(like; R-User-Vehicle-Date)
        String rentalId = "R-" + System.currentTimeMillis(); //this will generate an id LIke R- _here the time in miliseconds from 1970 , 1st jan_

        // The constructor of RentalRecord handles:
        // a. setting vehicle.isAvailable = flase
        // b, adding record to customer's history
        RentalRecord record = new RentalRecord(rentalId, customer, vehicle, rentalDate, returnDate);

        //we got the cash, now we will mark the record as paid.
        String method = (payment instanceof CardPayment) ? "Card" : "Cash";     //ternary operator (short of if else) true = card, false = cash
        record.markAsPaid(method, payment.getPaymentDetails());

        //saves the renteal record
        rentalRepo.add(record);
        saveData();                     //saves to the file
    }

    public void returnVehicle(String rentalId) throws Exception{
        
        //checks that the rental id is not null.
        if(rentalId == null || rentalId.trim().isEmpty()){
            throw new IllegalArgumentException("Rental ID cannot be empty.");
        }

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
        
        saveData();                             //saves to the file 
    }

    //3.================================================FILE HANDLING===============================
    
    public void saveData(){    
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

        } catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e){
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    //==================================4, ACCESSORS(for the GUI)========================

    public VehicleRepository getVehicleRepo(){ return vehicleRepo;}
    public UserRepository getUserRepo(){ return userRepo;}
    public RentalRecordRepository getRentalRepo(){return rentalRepo; }


    //========================================5. STATS ==========================

    public int getTotalVehicles(){
        //this will tell the total number of vehicles we have
        return vehicleRepo.getAll().size();
    }
        //this is for the total unmber of users we have
    public int getTotalUsers(){
        return userRepo.getAll().size();
    }
        //this is for the total rentalrecords we have
    public int getTotalRentals(){
        return rentalRepo.getAll().size();
    }


    //6.    ================== ADMIN FEATURES ==================
    
    //1.------0Set Discount----------------

    public void setVehicleDiscount(String vehicleId, double percentage) throws Exception{
        Vehicle v = vehicleRepo.getById(vehicleId);                     //gets vehicel by id
        if (v == null) throw new IllegalArgumentException("Vehicle not found.");    //if no vehicle found, will throw expceiton
        
        v.applyDiscount(percentage);                                // Updates currentRate 
        saveData();                                                 // Saves the new price to file
    }

    //2.---------------Add Maintenance------------------
    public void addMaintenance(String vehicleId, String description, LocalDate date, double cost) throws Exception {
        Vehicle v = vehicleRepo.getById(vehicleId);                 //will get vehilce by id.
        if (v == null) throw new IllegalArgumentException("Vehicle not found.");    //throws exception if no vehicel found with that id
        
        v.addMaintenanceRecord(description, date, cost);            //will add maintencnance record to the desired vehicel
        saveData();                                                 //saves to the file
    }

}