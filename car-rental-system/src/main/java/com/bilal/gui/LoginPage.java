package com.bilal.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginPage{
    
    private SceneManager sceneManager;              //scene manager is what will manage all the scenes (login,signup or home page etc... )
    private BorderPane root;                        // the pane i will be using is border pane because it resizes with the SCene and allows us to place content in strructured regions like center, top, bottom, etc
    
    public LoginPage(SceneManager sceneManager){    //Constructor   
        this.sceneManager = sceneManager;           //aggregates scenemanager in it
        createView();                               //calls a method (this method is in LoginPage)
    }
    
    private void createView(){                      //createView method (will create the scene for us(the view))
        root = new BorderPane();                    //creates the border pane here(this pane allows centring the control , like in middle of the screen)
                                                    //setStyle allows us to apply CSS code, to javafx node  
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(83, 77, 254, 1), #ebbb34ff);");   
                                                    //allows gradient too :) 
        //the login box containing the form
        VBox loginBox = new VBox(20);           //20 spacing between the controls                                  
        loginBox.setMaxWidth(500);              //the size of the login box (width (chorai) )
        loginBox.setMaxHeight(500);             //the size of the login box (height (lambai) )
        loginBox.setPadding(new Insets(40));    //padding of the box (padding)
        loginBox.setAlignment(Pos.CENTER);      //this will centre all the controls in centre in the VBOX only
                                                //styling the login box thats in the centre (styling) 
        loginBox.setStyle("-fx-background-color: white; -fx-background-radius: 15 ;");
                                                //will set the backgound color to white and the round teh corners of the box

        //---===LOGIN BOX CONTROLS========---
        
        Label lblTitle = new Label("CAR RENTAL LOGIN");             //the title we wil show on the top
        lblTitle.setFont(Font.font("",FontWeight.BOLD,30));         //setting font type (first emtpy string is font name), fontWeight, size of font
        lblTitle.setStyle("-fx-text-fill: rgba(20, 18, 18, 1);");      //here we are styling the title text by filling the text with color color

        Label lblSubtitle = new Label("Please Login to Continue");      //the label with text under teh title 
        lblSubtitle.setFont(Font.font("", 14));                    //this will again set the font style (font type, size)
        lblSubtitle.setStyle("-fx-text-fill: #666;");           //styling the font by setting the color to light greyish sort of

        TextField txtId = new TextField();                      //Text field for the user to enter the id
        txtId.setPromptText("User ID (e.g. admin or C-001)");   //this sets the shadow sort of thing in the text field , we dont use this in constructor of text field beacuse when we run the prorgam the text will be selected and present in the textfeild as a text and not shadow
        txtId.setPrefHeight(40);                                //the height of the text field it self (if increase the text fiel will get biger)                            

        PasswordField txtPass = new PasswordField();            //Passswrod field , for user to enter the password.
        txtPass.setPromptText("Password");                      //This wil again show a shadow in the passowrd field,   
        txtPass.setPrefHeight(40);                              //Height of passowrds field, if we play with it the height of passowrd field box will increase or decrease

        Button btnLogin = new Button("LOGIN");                  //Button (LOGIN) Button
        btnLogin.setPrefWidth(320);                             //Button's width (Chorai kitni uski ye set huwa)
        btnLogin.setPrefHeight(45);                             //Buttons's height (Uski height set ki idar)
                                                                //this is the styling of Button, Button's color, Button's Text color, Text stlying
        btnLogin.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                                                                //cursos hand means, jab mouse pointer buttton par ayy ga to wo hand wala pointer ban jay ga

        Label lblMessage = new Label();                         //this is important , as when the user will enter a wrong passwrod this label will apear.                    
        lblMessage.setStyle("-fx-text-fill: red;");             //the color of the text in that label will be read, (warning/error) effect

        //===========================ACTIONS=================

        //when button is clicked asks SceneManager to verify with the system
        btnLogin.setOnAction(e -> {                     //catching event and giving implementation
            String id = txtId.getText();                //gets the text from the textfield (USER ID)
            String pass = txtPass.getText();            //Gets the password form the passorwdField

            if(sceneManager.login(id, pass)){          //This will now verify the details using the backend of system SceneManager->User(Password)+UserRepo(User id)
                //login successful
                sceneManager.showHomePage();          //shows DashBoard if the login is sueccessful 
            }
            else{                                      //login failed so show the label wanrnig 
                lblMessage.setText("Invalid User ID or Password!");     //the label(line 61) will be set to the following text
                //this is extra effect
                txtId.setStyle("-fx-border-color: red;");           //this will set the color of the login fields(userid and passwrod) to red (borders)
                txtPass.setStyle("-fx-border-color: red;");         
            }
        });
        
        // String stringLine = "___________________________________";
        // HBox line = new HBox(stringLine);

        //create a new acount if dont have one(Signup) 
        Button btnSignup = new Button("No account? Create one here.");
                            //backgound of button is transparent , and the text color of button is light bluish, and when mouse cursor hovers above it becomes a hand
        btnSignup.setStyle("-fx-background-color: transparent ; -fx-text-fill: rgba(55, 70, 186, 1)  ; -fx-cursor: hand;");     //will take to signup page 
        btnSignup.setOnAction(e -> sceneManager.showSignupPage());    //cathicng the eventaction of the button and redirecting towards the signup page  


        //adding everything to the box
        loginBox.getChildren().addAll(lblTitle, lblSubtitle, new Separator() , txtId, txtPass, btnLogin, lblMessage, btnSignup );       //separator is line sort of thing that separates the above and below sections on the login box
        
        root.setCenter(loginBox);               //the reason why we used this...Allows us to centre the control automatically  
        
        //---------BACK BUTTON---------------------
        Button btnBack = new Button("<- Back");
        btnBack.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-cursor: hand;");
        btnBack.setOnAction(e -> sceneManager.returnToPreviousScene());

        HBox topBar = new HBox(btnBack);            //hbox will have this button 
        topBar.setPadding(new Insets(15));          //hbox's padding from the sides
        topBar.setAlignment(Pos.CENTER_LEFT);       //position is centered left (veritcally centre and horizontally left)
        root.setTop(topBar);                        //on the top of the border pane
    }
    
    public BorderPane getView(){            //returns the whole borderPane with all the required contorls
        return root;
    }

}


