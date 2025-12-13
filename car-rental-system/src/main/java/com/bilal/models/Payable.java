package com.bilal.models;

import java.io.Serializable;

public interface Payable extends Serializable{
    
            //abstract methods
    //processes the paymetn (true if payment successful and flase otherwise)
    boolean processPayment(double amount);
    //will return the payment details
    String getPaymentDetails();

}
