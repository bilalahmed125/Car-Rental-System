package com.bilal.models;

public interface Payable {
    
            //abstract methods
    //processes the paymetn (true if payment successful and flase otherwise)
    boolean processPayment(double amount);
    //will return the payment details
    String getPaymentDetails();

}
