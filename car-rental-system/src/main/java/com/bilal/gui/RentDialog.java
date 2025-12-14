package com.bilal.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.bilal.models.*;
import com.bilal.models.Payment.*;
import java.time.LocalDate;

public class RentDialog {

    private final Stage dialogStage;                        //dialogStage is a seperate window (popup) not a full screen scene
    private final CarRentalSystem system;                   //reference to backend system
    private final Vehicle vehicle;                          //vehicle which user selected to rent
    private final User customer;                            //logged in customer who is renting the vehicle
    
                //----------------CONTROLS--------------

    private DatePicker datePicker;      //date picker for selecting rental start date
    private TextField txtDays;          //text field to enter numbre of rental days
    private Label lblTotal;             //label to show live calculated total price
    private ComboBox<String> cmbPayment;        //combo box for selecting payment methods (card / cash) 
    private VBox paymentFieldsBox;                //this VBox will dynamically hold payment related fields(change fields for card payment vs cash)


    //--------PAYMENT FIELDS----------------

    //these are at calss level so we can access them in later logic
    private TextField txtCardNum;        //card number field
    private TextField txtCardHolder;     //card holder name
    private DatePicker dpCardExpiry;     //expiry date picker
    private TextField txtCashGiven;      //cash given by customer

    //constructor
    public RentDialog(CarRentalSystem system, Vehicle vehicle, User customer){
        this.system = system;        //backend reference
        this.vehicle = vehicle;      //selected vehicle
        this.customer = customer;    //logged in user

        dialogStage = new Stage();   //new window 
        dialogStage.initModality(Modality.APPLICATION_MODAL);           //this means that the new window opened will not allow user to interact with any other windows of the same process(like he cannot paly around the app without closing it)
        //APPLICATION_MODAL = user must close this dialog before going back, Modality.NONE = doesnt block the main app
        dialogStage.setTitle("Rent Vehicle");           //set the titel of the dialog to this
        
        createView();                //create the Dialog box and show
    }

    private void createView(){
        //root layout for dialog(VBOX)
        VBox root = new VBox(15);                //15 spacing between contrlos
        root.setPadding(new Insets(20));         //padding from all sides
        root.setPrefWidth(400);                 //fixed width dialog
        root.setStyle("-fx-background-color: white;");              //the backgound color is white

        //---------------- HEADER-----------------------
        
        //shows which vehicle is being rented
        Label lblTitle = new Label("RENTING: " + vehicle.getMake() + " " + vehicle.getModel());         //vehicle details using vehicle calss
        lblTitle.setFont(Font.font("", FontWeight.EXTRA_BOLD, 22));                                      //setting font to bold and size 22       
        lblTitle.setStyle("-fx-text-fill: #2c3e50;");                                                  //setting font color 

        //shows daily rental rate
        Label lblRate = new Label("Daily Rate: $" + vehicle.getCurrentRate());              //getting the daily reate of the vehicle
        lblRate.setStyle("-fx-text-fill: #9e5ae1ff;");                                        //setting the font color 

        //----------------RENTAL DETAILS--------------------------------
        
        //start date picker (default is today)
        datePicker = new DatePicker(LocalDate.now());                   //datepicker (LocalDate.now()) makes today as defualt date..
        datePicker.setPromptText("Pick Start Date");                    //place holder 
        datePicker.setPrefWidth(Double.MAX_VALUE);                      //the width will be maximum(the space it will get, it will take)

        //enter number of rental days                       
        txtDays = new TextField();                                           //creaets a text field that wil input the nubmer of days user want to rent the car
        txtDays.setPromptText("Number of Days (e.g, 3)");                  //Place holder

        //-----------------------------LIVE PRICE CALCULATION---------------------------
        
        //label that updates automatically when days change
        lblTotal = new Label("Total Cost: $0.00");                  //label of that tells the cost
        lblTotal.setFont(Font.font("", FontWeight.BOLD, 18));  //font is in bold and szie 18
        lblTotal.setStyle("-fx-text-fill: #27ae60;");       //the color of text is greenish

        //listener whenever days text changes, recalculate price
        txtDays.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());  //as soon as the days are changed, the values are observed and the method is called


        // ---------------- PAYMENT METHOD ----------------------
        
        cmbPayment = new ComboBox<>();
        cmbPayment.getItems().addAll("Credit Card", "Cash"); //(adding) available methods
        cmbPayment.setValue("Credit Card");                  //default selection 
        cmbPayment.setPrefWidth(Double.MAX_VALUE);           //this will make our combo box to stretch as much as possible accoring to the space it has 

        //this box will change content based on selected payment method
        paymentFieldsBox = new VBox(10);                    

        //initial load for credit card fields
        updatePaymentFields("Credit Card");     //method called which will update the fields according to the payemnt method...

        //when user changes payment method, update fields dynamically
        cmbPayment.setOnAction(e -> updatePaymentFields(cmbPayment.getValue()));        //the value from combo box is fed to the method which updats the fileds


        //----------------------- CONFIRM BUTTN--------------------
        
        Button btnConfirm = new Button("CONFIRM PAYMENT");          //button to confirm payment
        btnConfirm.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");    //buttons style ; setted color, text color to white, text to bold and cursor to hand
        btnConfirm.setPrefWidth(Double.MAX_VALUE);              //again this will make this button strecth as much sapce as it can
        btnConfirm.setPrefHeight(40);                           //the button's height
        
        //when clicked, vailidate and process rental
        btnConfirm.setOnAction(e -> handleConfirm());             //when button is clicked the handleConfirm method is called

        //adding everything to root layout in proper order
        root.getChildren().addAll(                          
            lblTitle, lblRate, new Separator(),
            new Label("Start Date:"), datePicker,
            new Label("Days:"), txtDays,
            lblTotal, new Separator(),
            new Label("Payment Method:"), cmbPayment,
            paymentFieldsBox, 
            new Separator(), btnConfirm
        );

        //creating scene and attaching it to dialog stage
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
    }

    private void updatePaymentFields(String method){
        //clear old fields before adding new ones
        paymentFieldsBox.getChildren().clear();
        
        if(method.equals("Credit Card")){               //if combo box has creditcard selected then this will be truq
            //-----------CREDIT CARD FIELDS----------------
            
            txtCardNum = new TextField();                                       //field for card NUmber
            txtCardNum.setPromptText("Card Number (e.g. 1234-5678)");           //placeholder
            
            txtCardHolder = new TextField();                                    //field for cardholder name
            txtCardHolder.setPromptText("Card Holder Name");                    //placeholder
            
            //expiry date picker
            dpCardExpiry = new DatePicker();            //datepicket 
            dpCardExpiry.setPromptText("Expiry Date");  //placeholder
            dpCardExpiry.setPrefWidth(Double.MAX_VALUE);    //can take as much space as possible
            
            paymentFieldsBox.getChildren().addAll(txtCardNum, txtCardHolder, dpCardExpiry);     //adds to the VBOX
            
        }else{
            // -------------CASH PAYMENT FIELDS --------
            
            txtCashGiven = new TextField();             //text field for cash
            txtCashGiven.setPromptText("Cash Given Amount"); //placeholdre
            
            //info message for cash users
            Label lblInfo = new Label("Please hand over cash at counter.");         //this message will be shown when the cash payment is selected 
            lblInfo.setStyle("-fx-text-fill: #e67e22;");            //color is oragnish
            
            paymentFieldsBox.getChildren().addAll(txtCashGiven, lblInfo);       //adds to the vbox
        }
    }

    private void calculateTotal(){              
        try{                                                
            int days = Integer.parseInt(txtDays.getText());         //days and then parse(Convert it to int from string) , parseInt means convert the string to int (it is an integer's methods)
            if(days > 0){                       //only if days is greater then 0
                double total = vehicle.calculateRentalCost(days);       //calculates the rentalcost using vehicle.java
                lblTotal.setText("Total Cost: $" + total);              //setting the text to totalcost + rentalcost  
            }
        }catch (NumberFormatException e){                       //if found an exception just make the label's text to 0,0 like user enters 3.a or 3a etc...
            //if invalid input, reset total
            lblTotal.setText("Total Cost: $0.00");
        }
    }

    private void handleConfirm(){
        try{
            //--------BASIC VALDATION-------------
            
            if(datePicker.getValue() == null){                          //if datepicker is null this message will be shown
                showAlert("Please select a rental start date.");    //calls a method whihc will  creaet  an alert of type error and shows and wait til user clicks ok
                return;                                                 //returns from the method
            }

            if(txtDays.getText().trim().isEmpty()){                     //this will chek the days entered and if empty will throw alert message 
                showAlert("Please enter number of days.");
                return;
            }
            
            int days = Integer.parseInt(txtDays.getText());           
            // double estimatedCost = vehicle.calculateRentalCost(days);

            Payable payment;                //a reference of payable

            //--------PAYMENT VALIDATION & OBJECT CREATION----------
            
            if(cmbPayment.getValue().equals("Credit Card")){            //credit card selected
                //validate card fields
                if(txtCardNum.getText().isEmpty() || txtCardHolder.getText().isEmpty()){    //cardNumber or cardHOlder shoudlnot be empty
                    showAlert("Please fill in all card details.");                      //if empty show alerty of tyep error
                    return;             
                }

                if(dpCardExpiry.getValue() == null){                                //if card's expiry is empty(null)
                    showAlert("Please select card expiry date.");               //thorw alert 
                    return;
                }

                //cradPayment object creation
                payment = new CardPayment(                  //upcasting CardPayent to Paymrnt
                    txtCardNum.getText(), 
                    txtCardHolder.getText(), 
                    dpCardExpiry.getValue()
                );

            }else{
                //validate cash input
                if(txtCashGiven.getText().isEmpty()){                   //if cash isnt given yet throw an alert
                    showAlert("Please enter the cash amount given.");
                    return;
                }

                double cash = Double.parseDouble(txtCashGiven.getText());                   //will parse the string to double
                payment = new CashPayment("REC-" + System.currentTimeMillis(), cash);       //will creaet a method of cash payment
            }

            //-----------CALL BACKEND SYSTEM--------
            //rentVehicle may throw exception if payment fails
            system.rentVehicle(
                customer.getUserId(),
                vehicle.getVehicleId(),
                datePicker.getValue(),
                days,
                payment
            );

            //success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Booking Successful! Enjoy your ride.");   //this message if of type information
            alert.showAndWait();        //alert will not go until user press ok
            dialogStage.close();        //when alreat is closed the dialog is too

        } catch (NumberFormatException e){                               //if the number of days or cash is not valid format like has alphabets or symbols etc.. this will throw exception and will be hadnled
            showAlert("Invalid number entered (Days or Cash)!");
        }catch (Exception e){
            //catches backend exceptions(expired card, payment failed, etc)
            showAlert("Rental Failed: " + e.getMessage());      
        }
    }

    private void showAlert(String msg){             //this is one of the msot importatn method
        //simple reusable error popup
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();            //shows alert and AlertyType is Error and also waits for user to click ok to continue
    }

    public void show(){
        //blocks parent window until dialog is closed
        dialogStage.showAndWait();              //this will open over parent's windoww
    }
}
