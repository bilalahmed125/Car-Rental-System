package com.bilal.models;

//withuot these, we wont be able to make the objects of teh desried classes
import com.bilal.models.users.Customer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalRecord implements Serializable{
    
    //will be using static strings to define the status
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_COMPLETED= "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_OVERDUE = "OVERDUE";

    private String rentalId;                //The id generated, for the record
    private LocalDate rentalDate;           //the date on which user rented the vehicel
    private LocalDate returnDate;           //the return date user commited to retrun
    private LocalDate actualReturnDate;     //the return date when the user actually returned the vehicle
    private double totalCost;               //cost of rent.
    private double lateFee;                 // the late fees.
    private String status;                  

    private boolean isPaid;                 //payment done or not
    private String paymentMethod;          //card or cash
    private String paymentDetails;          //details of the payment

    private Vehicle rentedVehicle;          //will aggregate vehicle (pass vehicle obj). 
    private Customer customer;              //will aggreate customer (pass customer obj).
    
    public RentalRecord(String rentalId, Customer customer, Vehicle vehicle , LocalDate rentalDate, LocalDate returnDate){
        if(customer==null || vehicle == null){
            throw new IllegalArgumentException("Customer and Vehicle cannot be null!");
        }

        if(rentalDate.isAfter(returnDate)){
            throw new IllegalArgumentException("Rental date cannot be after return date!");
        }

        this.rentalId = rentalId;
        this.customer = customer;
        this.rentedVehicle = vehicle;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.status = STATUS_ACTIVE;
        this.lateFee = 0;
        this.isPaid = false;            //default is false and will update manually when needed.

        //this will calcualte the rental days and teh cost
        int days = (int) ChronoUnit.DAYS.between(rentalDate,returnDate);
        if(days<1) days = 1;        //min 1days

        this.totalCost = vehicle.calculateRentalCost(days);

        // this will make the vehicel available = false.
        this.rentedVehicle.rent();

        //this line means that we will pass teh current object to the customer.
        customer.addRentalRecord(this);
        
    }
    
                    //methods
    
    //this method is used by system to mark the record as paid, after the payment has processed sucessfuly.
    public void markAsPaid(String method, String details) {
        this.isPaid = true;
        this.paymentMethod = method;
        this.paymentDetails = details;
    }
    //method for payment handling
    // public boolean processPayment(Payable payment){
    //     if(isPaid){         //meaning the payment is already done
    //         return false;
    //     }
        
    //     boolean success = payment.processPayment(totalCost);
        
    //     if(success){
    //         this.isPaid = true;
    //         this.paymentDetails = payment.getPaymentDetails();

    //         //Determine payment method from the details string
    //         if(paymentDetails.contains("Card")){
    //             this.paymentMethod = "Card";
    //         }
    //         else if(paymentDetails.contains("Cash")){
    //             this.paymentMethod ="Cash";
    //         }
    //     }
    //     return success;
    // }

    //Return the vehicel (date)
    public void returnVehicle(LocalDate actualReturn){
        this.actualReturnDate = actualReturn;
        this.status = STATUS_COMPLETED;
        
        //vecicle avaialbel for rent again
        rentedVehicle.returnVehicle();
        
        //calculate late fees if retunred late
        if(actualReturn.isAfter(returnDate)){
            int lateDays = (int) ChronoUnit.DAYS.between(returnDate,actualReturn);      //this gets the days in between the two dates
            this.lateFee = lateDays * rentedVehicle.getBaseRatePerDay() * 1.5;          //this will calculate the fine amount. (50% fine)
            this.totalCost += lateFee;                                                  //we will then add fine to the totalCost
        }
    }

    //Cancel the rental
    public void cancelRental(){
        this.status = STATUS_CANCELLED;
        rentedVehicle.returnVehicle();
        customer.removeRentalRecord(this);
    }

    //checks if rental is overdue
    public boolean checkOverDue(){
        if(status.equals(STATUS_ACTIVE) && LocalDate.now().isAfter(returnDate)){    //checks if the status is active and the comited retrun date has passed
            this.status = STATUS_OVERDUE;                                           //sets the status as overdue
            return true;                                                            
        }
        return false;
    }


        // Getters
    public String getRentalId(){ return rentalId; }
    public LocalDate getRentalDate(){ return rentalDate; }
    public LocalDate getReturnDate(){ return returnDate; }
    public LocalDate getActualReturnDate(){ return actualReturnDate; }
    public double getTotalCost(){ return totalCost; }
    public double getLateFees(){ return lateFee; }
    public String getStatus(){ return status; }
    public Vehicle getRentedVehicle(){ return rentedVehicle; }
    public Customer getCustomer(){ return customer; }
    public boolean isPaid(){ return isPaid; }
    public String getPaymentMethod(){ return paymentMethod; }
    public String getPaymentDetails(){ return paymentDetails; }

    //calculates teh total days, the vehicle was rented for
    public int getRentalDays(){
        LocalDate endDate = actualReturnDate != null ? actualReturnDate : returnDate;       //ternary (short if else) if true then returns the firs value after ? and returns second if flase.
        return (int) ChronoUnit.DAYS.between(rentalDate, endDate);                          // will return the total days the vehicle was rented for.
    }

}
