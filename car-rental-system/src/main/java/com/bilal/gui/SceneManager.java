package com.bilal.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import com.bilal.models.CarRentalSystem;
import com.bilal.models.User;
import com.bilal.models.users.Admin;
import com.bilal.models.users.Customer;

public class SceneManager{
    
    private final Stage primaryStage;
    private final CarRentalSystem system; //this is the system(backend)
    private User currentUser;             //tracks who is currently logged in
    private Scene previousScene;            //stores the previous scene, if needed will throw it to continue exactly from where the user left

    public SceneManager(Stage primaryStage, CarRentalSystem system){
        this.primaryStage = primaryStage;
        this.system = system;
    }

                    //=----------CONNECTING GUI TO BACKEND----------------
    
    //this method is called by the Login Page. 
    //it uses the backend logic(userRepo.getById) to verify the deatils(userid and passwrod)
    public boolean login(String userId, String password){
        //checks if user exists in our system(UserRepository)
        User user = system.getUserRepo().getById(userId);       //tries to get the user by id, if found user = that user, else user = null;
        
        //checks if user exists, then checks password using the User.java's method
        if(user != null && user.login(password)){                      //user shouldnot be null and passowrd verification using the method of user.java
            this.currentUser = user;                                //reemembers this user, if login is successful
            return true;
        }
        return false;                                               //login failed
    }

    public void logout(){
        this.currentUser = null;                    //current user is now null, meaning no one is logged in
        showHomePage();
        //showLoginPage();                            //go back to login
    }

                //===================switching screens=====================

    public void showLoginPage(){
        previousScene = primaryStage.getScene();                                //gets the curernt scene and stores it
        LoginPage login = new LoginPage(this);                              //we will pass 'this' manager to the page
        primaryStage.setScene(new Scene(login.getView(), 1200, 800));       //this will set the scene to the new scene which will have the loginpage(borderpane) object because getView of login returns root(border pane)
        primaryStage.setTitle("Car Rental System - Login");                 //this will set the title of the stage to this
    }
    public void showSignupPage(){                                           
        SignupPage signup = new SignupPage(this, system);                   //we pass 'this'(SceneManager) and 'system'(backend) to the signup page
        primaryStage.setScene(new Scene(signup.getView(), 1200, 800));      //this will set the scene to a new scene with the signup page object of Borderpane(containing all the signup stuff) bcz getView returns root(bornder pane)
        primaryStage.setTitle("Car Rental System - Create Account");        //this sets the title of the stage to this. 
    }

    //home page calling
    public void showHomePage(){
        //we pass 'this' and 'system' so the homepage can get vehicles data
        HomePage home = new HomePage(this, system);             //creates an object of homepage here
        primaryStage.setScene(new Scene(home.getView(), 1200, 800));        //thsi will create a new scene that will have the homepage's root
        primaryStage.setTitle("Car Rental System - Home");                  //this sets the stage title to carrentalsystem home
    }

    public boolean isLoggedIn(){                                                //checks if the user is logged in the system or not
        return currentUser != null;                                             //if currentuser is not null then reutrns true else false
    }

    //(we will add Admin/Customer dashboard methods here later)
    public void showDashboard(){
        if(currentUser instanceof Admin){
            System.out.println("Redirecting to Admin Dashboard...");
            // will call this showAdminDashboard();
        }
        else if(currentUser instanceof Customer){
            System.out.println("Redirecting to Customer Dashboard...");
            //will call this showCustomerDashboard();
        }
    }

    //----------GETTERS------------------
    public User getCurrentUser(){ return currentUser; }
    public boolean isAdmin(){ return currentUser instanceof Admin; }
    public void returnToPreviousScene(){
        if(previousScene!=null){
            primaryStage.setScene(previousScene);
            previousScene = null;
        }
        else{
            showHomePage();
        }
    }

}