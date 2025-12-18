package com.bilal.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.bilal.models.CarRentalSystem;

public class SignupPage{

    private SceneManager sceneManager;     //SceneManager is used to switch between pages(login, signup, dashboard etc..)
    private CarRentalSystem system;        //Main system, used here to register a new customer
    private BorderPane root;               //BorderPane used as root because it resizes with Scene and allows structured layout(Especailly center in our case)

    //constructor takes SceneManager and System (for registration logic)
    public SignupPage(SceneManager sceneManager, CarRentalSystem system){
        this.sceneManager = sceneManager;       //store reference of SceneManager
        this.system = system;                   //store reference of CarRentalSystem
        createView();                            //creates the signup page(createView is a method of this class)
    }

    private void createView(){            //this method builds the complete signup screen
        root = new BorderPane();            //root layout for the page
        
        //setting same gradient background as login page for UI consistency
        //setStyle allows us to apply CSS directly on JavaFX node
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87e9cdff, #c764d2ff);");

        //the signup box (in vbox) form
        VBox signupBox = new VBox(15);                     //15 vertical spacing between controls
        signupBox.setMaxWidth(500);                      //the width of signup box(chorai)
        signupBox.setMaxHeight(800);                    //the height of signup box(unchai ya lambai) more because more details to enter
        signupBox.setPadding(new Insets(30));       //spacing from borders
        signupBox.setAlignment(Pos.CENTER);         //centers all controls inside VBox
        signupBox.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                                           //white background with rounded corners of singup box

        //----titel ----

        Label lblTitle = new Label("CREATE ACCOUNT");                 //main title of signup page
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 24));        //bold font + 24 size of font
        lblTitle.setStyle("-fx-text-fill: rgba(15, 14, 14, 1);");   //color of title text 

        Label lblSubtitle = new Label("SignUp to Car Rental System");  //sub heading
        lblSubtitle.setFont(Font.font("", 12));                    //smaller font size
        lblSubtitle.setStyle("-fx-text-fill: #666;");               //light grey color

        //----fields/textfields and pasfields-----
        
        //made an helper method createStyledTextField() to avoid repeating same code again and again
        //i will jsut pass it the prompt and it will create and reutnr me a text field 
        TextField txtId = createStyledTextField("User ID (Unique)");          //user id
        TextField txtName = createStyledTextField("Full Name");               //customer name
        TextField txtEmail = createStyledTextField("Email Address");          //email
        TextField txtPhone = createStyledTextField("Phone Number");           //phone number
        TextField txtLicense = createStyledTextField("Driving License Number");//license number
        TextField txtAddress = createStyledTextField("Home Address");          //home address

        //Password fields
        PasswordField txtPass = new PasswordField();       //password input field
        txtPass.setPromptText("Password");                 //placeholder text
        txtPass.setPrefHeight(40);                         //height of the fied itself (agar bara kia to passwrod field wala box bara hojay ga)

        PasswordField txtConfirmPass = new PasswordField();           //confirm password field
        txtConfirmPass.setPromptText("Confirm Password");             //place holder / shadow text in pf  
        txtConfirmPass.setPrefHeight(40);                             //the height of passowrd field


        //=====================BUTTONS===============================

        Button btnRegister = new Button("REGISTER"); //register button
        btnRegister.setPrefWidth(440);               //button width     (Button kitna lamba hai (horizontally))
        btnRegister.setPrefHeight(45);               //button height    (button kitna bara hai(vertically))
        btnRegister.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;-fx-font-weight: bold ;-fx-cursor: hand;"); //style is similar to login button

        //go back to login page if already have an account 
        Button btnlogin = new Button("Already have an Account? Click here.");
                            //backgound of button is transparent , and the text color of button is light bluish, and when mouse cursor hovers above it becomes a hand
        btnlogin.setStyle("-fx-background-color: transparent ; -fx-text-fill: rgba(55, 70, 186, 1)  ; -fx-cursor: hand;");     //will take to signup page 
        btnlogin.setOnAction(e -> sceneManager.showLoginPage());    //cathicng the eventaction of the button and redirecting towards the login page  
        
        //when clicked, SceneManager switches back to Login page

        //Label to show error or success messages
        Label lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: red;"); 

        //==================ACTIONS==================

        btnRegister.setOnAction(e -> {   //runs when REGISTER button is clicked

            //get all values entered by the user
            String id = txtId.getText();
            String name = txtName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();
            String license = txtLicense.getText();
            String addr = txtAddress.getText();
            String pass = txtPass.getText();
            String confirm = txtConfirmPass.getText();

            //gui page par validation(basic checks before backend)
            if(pass.isEmpty() || confirm.isEmpty()){           //if passord field is empty or confirm password field is empty then this will set the message to that and in red color
                lblMessage.setText("Passwords cannot be empty!");
                return;                                 //stop execution if invalid
            }

            if(!pass.equals(confirm)){              //if the passwrod and confirm password doesnot match then this wil hapen
                lblMessage.setText("Passwords do not match!");
                txtPass.setStyle("-fx-border-color: red;");             //wil paint the border red of both passowrd and confirm password fields
                txtConfirmPass.setStyle("-fx-border-color: red;");
                return;
            }

            //system(backend) registration using CarRentalSystem
            try{
                system.registerCustomer(id, name, email, phone, pass, license, addr);
                //if registration is successful

                lblMessage.setStyle("-fx-text-fill: green;");                       //if registtion is successful then it will print a that message in green color
                lblMessage.setText("Registration Successful! Please Login.");
                                                                                    //we will stay on this page so user can read success message

            } catch (Exception ex){
                                //if backend throws error (e.g. ID already exists or email or phone etc..)
                lblMessage.setStyle("-fx-text-fill: red;");             //the message will be printed in red color
                lblMessage.setText("Error: " + ex.getMessage());        //the message will be of the exception message that we catched
            }
        });

        //now adding all the controls to the signup vbox
        signupBox.getChildren().addAll(
            lblTitle, lblSubtitle, new Separator(),
            txtId, txtName, txtEmail, txtPhone, txtLicense, txtAddress,
            txtPass, txtConfirmPass,
            lblMessage, btnRegister, btnlogin
        );

        //placing the signup box in the center of BorderPane
        //BorderPane handles resizing automatically
        root.setCenter(signupBox);

        //---------BACK BUTTON--------------------- (copied as is from loginpage)
        Button btnBack = new Button("<- Back");
        btnBack.setStyle("-fx-font-weight: bold ;-fx-background-color: transparent; -fx-text-fill: black; -fx-cursor: hand;");
        btnBack.setOnAction(e -> sceneManager.returnToPreviousScene());

        HBox topBar = new HBox(btnBack);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);

        root.setTop(topBar);

    }

    //Helper method to create same styled text fields
    //with this we can easily add new textfields too
    private TextField createStyledTextField(String prompt){
        TextField tf = new TextField();
        tf.setPromptText(prompt);   //placeholder text
        tf.setPrefHeight(40);       //exact same height for all fields
        return tf;                  //returns the text field object
    }

    public BorderPane getView(){
        return root;            //returns the complete signup ui
    }
}
