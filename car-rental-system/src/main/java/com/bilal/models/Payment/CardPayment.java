package com.bilal.models.Payment;
import com.bilal.models.Payable;

import java.time.LocalDate;

public class CardPayment implements Payable{
    
    private String cardNumber;
    private String cardHolderName;
    private LocalDate expiryDate;
    private boolean paymentStatus = false;

    public CardPayment(String cardNumber, String cardHolderName, LocalDate expiryDate){
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
        if (!expiryDate.isBefore(today)){           //checks if the card is valid or not (not expired)
            System.out.println("Payment Success : "+ amount);   
            paymentStatus = true;                   //updates the payment status for furhter use
            return true;                
        }
        else{
            System.out.println("Card is expired! Payment failed");
            return false;
        }
    }
    @Override
    public String getPaymentDetails(){
        if(paymentStatus){                      //prints the message based on the paymentstatus 
           return "Payment amount: " + temporaryAmount + " | Card Holder Name: " +  cardHolderName 
                    + " | Payment Done Via Card" ;
            
        }
        else{
            return "Payment of Amount: " + temporaryAmount + " | Failed!  | CardHolder Name: "+cardHolderName ;
        }
    }

}
