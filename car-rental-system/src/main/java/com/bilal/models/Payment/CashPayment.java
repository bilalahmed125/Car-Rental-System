package com.bilal.models.Payment;

import com.bilal.models.Payable;

public class CashPayment implements Payable{
    private String receiptNumber;
    private double cashGiven;
    private double change = 0;
    private boolean paymentStatus = false;

    //constructor
    public CashPayment(String receiptNumber, double cashGiven){
        this.receiptNumber = receiptNumber;
        this.cashGiven = cashGiven;
    }

    //method:
    private double temporaryAmount = 0;
    @Override
    public boolean processPayment(double amount){
        temporaryAmount = amount;                       //stores amount for further use
        if (amount <= 0) {
            System.out.println("Invalid payment amount!");
            return false;
        }
        if(cashGiven >= amount){                            //checks if the given amount can cover the expenses
            System.out.println("Payment Successful: " + amount);    
            change = cashGiven - amount;                    //calculates and stores cahnge amount
            return paymentStatus = true;                    //returns true
        }
        else{
            System.out.println("Cash not enough! Payment failed! ");        //if the givencash is less then amount 
            return paymentStatus = false;                                   //returns false
        }
    }
    @Override
    public String getPaymentDetails(){
        if(paymentStatus)                   //checks payment status 
            return "Payment Amount: "+ temporaryAmount + " | Chnage amount:  "+change + " | ReceiptNumber : "+ receiptNumber + "| Payed via Cash!"  ;
        else
            return "Payment of Amount: " +temporaryAmount + " Failed! | ReceiptNumber: "+ receiptNumber;
    }
}
