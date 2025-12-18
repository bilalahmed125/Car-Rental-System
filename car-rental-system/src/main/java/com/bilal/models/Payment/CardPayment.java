package com.bilal.models.Payment;
import com.bilal.models.Payable;

import java.time.LocalDate;

public class CardPayment implements Payable{
    
    @SuppressWarnings("unused")
    private String cardNumber;
    private String cardHolderName;
    private LocalDate expiryDate;
    private boolean paymentStatus = false;

    public CardPayment(String cardNumber, String cardHolderName, LocalDate expiryDate){
        if(!isNumeric(cardNumber)){
            throw new IllegalArgumentException("Error: INValid Card Number.");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    //methods:
    private double temporaryAmount = 0;
    @Override
    public boolean processPayment(double amount){
        LocalDate today = LocalDate.now();          //stores today's date (from computer)
        this.temporaryAmount = amount;          //stores the amount temporarily for further use     
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount!");
        }   
        if(!expiryDate.isBefore(today)){           //checks if the card is valid or not (not expired)
            // System.out.println("Payment Success : "+ amount);   
            paymentStatus = true;                   //updates the payment status for furhter use
            return true;                
        }
        else{
            throw new IllegalArgumentException("Card is expired! Payment failed");
        }
    }
    @Override
    public String getPaymentDetails(){
        if(paymentStatus){                      //prints the message based on the paymentstatus 
           return "Payment amount: " + temporaryAmount + " | Card Holder Name: " +  cardHolderName 
                    + " | Payment Done Via Card" ;
            
        }
        else{
            return "Payment of Amount: " + temporaryAmount + " Failed!  | CardHolder Name: "+cardHolderName ;
        }
    }
    //checks if the cardNumber shouldnot hold any numbers in it;
    public boolean isNumeric(String str){
    for(char c : str.toCharArray()){                //it will first convert string to characters array, then check each cahracter one by one
        if(!Character.isDigit(c)){                  //if any character is not digit(meaning non nubmeric) then reutnr false
            return false;
        }
    }
        return true;                            //if no nonnumber found then returns true.
    }
}
