package com.bilal.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.bilal.models.*;
import com.bilal.models.vehicles.*;
import java.util.ArrayList;

public class HomePage{
    
    private SceneManager sceneManager;                  //manager objects reference (links backend and gui)
    private CarRentalSystem system;                     //the backend of system
    private BorderPane root;                            //the pane (border pane) becasue its resizes automatically accoriding to the window and has structured left right or centre....
    
    private FlowPane vehicleGrid;                       //FlowPane is also a pane (used to store vehicle cards/tiles) but this one has a plus piont that it can wrap when the width runs out  
    
    private String selectedCategory = "ALL";                //tracks current cateogry filter (Default is ALL)


    //CONSRUCTTOR
    public HomePage(SceneManager sceneManager, CarRentalSystem system){
        this.sceneManager = sceneManager;               //gets the scenemanger
        this.system = system;                           //gets the system   
        createView();                                   //creates teh homepage view(this method is inside the same calss)
    }

    private void createView(){
        root = new BorderPane();                             //main container pane(will have all the elements of homepge)
        root.setStyle("-fx-background-color: #f4f4f4;");    // Light grey background for the whole page (white is too bright)

        //1. TOP BAR (Navigation)
        root.setTop(createTopBar());            //adds the top navgation bar

        //2. LEFT SIDE BAR (Categary Filters)
        root.setLeft(createSideBar());          //adds the category bar on the left (car only , bike only etc...)

        //3. CENTER (The Scrollable of Cars Cards)
        ScrollPane scrollPane = new ScrollPane();               //kukay hamay kafi zyada vehicles show hongin to unko scroll pane may dal dia takay sirf unn cars kay cards ko scroll kar skay nakay puray scene ko
        scrollPane.setFitToWidth(true);                             //this will make the content inside the scroll pane stretch to the same witdh as scroll pane so it doent have any weird loking gaps or uneven content cards
        scrollPane.setStyle(/*"-fx-background: transparent;*/ "-fx-background-color: transparent;");         //scroll pane will be tarnasparent so that our BORDERPANE's(main pane) styling is visible
                                                                                                         
        vehicleGrid = new FlowPane();                           //creates the flowpane which will dynamically hold the vehicle cards
        vehicleGrid.setPadding(new Insets(20));                 //padding
        vehicleGrid.setHgap(20);                                //horizontal gap between tiles
        vehicleGrid.setVgap(20);                                //vertical gap between tiles
        
        scrollPane.setContent(vehicleGrid);                     //plcaes the vehiclegrid (Flowpane) inside the scrollPane
        root.setCenter(scrollPane);                             //places the scrollpane insdie the border pane (centered)

        refreshVehicles();                                      //intially loads all the vehicles
    }

        //1.------------TOP BAR------------------------
    
    private HBox createTopBar(){
        HBox topBar = new HBox(20);                             //spacing between the controls 
        topBar.setPadding(new Insets(15, 30, 15, 30));          //trbl appraoch (Top , Right, Bottom , LEft) , will not let the content touch the edgess 
        topBar.setAlignment(Pos.CENTER_RIGHT);                  //thsi will make the childs of Hbox vertically centre and to horizontally to the right
        topBar.setStyle("-fx-background-color: #2c3e50;");     //Dark Bluish header

        Label lblTitle = new Label("CAR RENTAL SYSTEM");             //sets the title to this.
        lblTitle.setStyle("-fx-text-fill: white; ");                //fill the color of teh title to white
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 20));       //sets the font to bold and size 20
        
        //spacer to push title to left and buttons to right
        Region spacer = new Region();                           //just like [title][    ][butons] the space in the mid is a spacer
        HBox.setHgrow(spacer, Priority.ALWAYS);                 //setHgrow is static method of HBOX , and tells how to treat the spacer
                                                                //tells the spacer how much to grow to keep the childrens to desired side and always tells it to always grow when empty horizontal space is found
        //dynamic buttons this Logic is to check if user is logged in
        HBox authButtons = new HBox(10);                        //new Hbox to change if logged in or not logged in
        
       if(sceneManager.isLoggedIn()){
            //IF LOGGED UP then Show "Dashboard" and "Logout' buttons
            Button btnDash = new Button("My Dashboard");            //dashboard button
            btnDash.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");     //color of button is greenish, text color is white and the cursor behaviour is hand when above it
            btnDash.setOnAction(e -> sceneManager.showDashboard());         //controlling eventaction..... by calling a method.
            
            Button btnLogout = new Button("Logout");                        //logout button
            btnLogout.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-cursor: hand;");    //color of loguot button is red with white text and cursor wil be hand when above it
            btnLogout.setOnAction(e -> sceneManager.logout());              //logs out when pressed

            authButtons.getChildren().addAll(btnDash, btnLogout);          //buttons placed in the HBox to show when logged in 
        }
        else{
            //IF NOT LOGGED IN then Show "Login" and "Sign Up" buttons
            Button btnLogin = new Button("Login");                          //Login button
            Button btnSignup = new Button("Sign Up");                       //signup button
            String btnStyle = "-fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand;";        //both buttons will have the same style so i wrote the style in string and will pass this for both buttons setStyle
            btnLogin.setStyle(btnStyle);                //same style as in string 
            btnSignup.setStyle(btnStyle);               //same style

            btnLogin.setOnAction(e -> sceneManager.showLoginPage());        //eventcathed and showLoginPage() method is called which will show login poage
            btnSignup.setOnAction(e -> sceneManager.showSignupPage());      //evenis catched and will showSignupPage() is called whicih will show the signuppage.

            authButtons.getChildren().addAll(btnLogin, btnSignup);          //will add these buttons to Hbox authbuttons if not logged in
        }

        topBar.getChildren().addAll(lblTitle, spacer, authButtons);         //in the top bar(HBox) we will place the Title and the buttons(dynamic), + the spacer(Must).
                                //[title] [          ][buttons]  is what it will look like in the top bar
        return topBar;                                                      //returns topBar(HBox object)
    }

    //2.--------------------SIDE BAR(Categories or Filters)-------------------
    
    private VBox createSideBar(){                       //method that returns a Vbox object
        VBox sideBar = new VBox(10);                    //creates a VBOX object with a spacing of 10
        sideBar.setPadding(new Insets(20));             //the padding is set to 20
        sideBar.setPrefWidth(150);                      //the width of the VBox is 150 pixles (kitni chori/moti vbox hogi)
        sideBar.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 0 1 0 0;");    //this will setStyle of the VBOX 
                            //backgound color is white , border color is white , border width is also trbl (right ki 1pixel)

        Label lblCat = new Label("Categories");                             //new label for categories
        lblCat.setFont(Font.font("", FontWeight.BOLD, 14));                 //setting the font style (bold and 14 size)

        sideBar.getChildren().add(lblCat);                                  //adds the label to the side bar(HBOX)
        sideBar.getChildren().add(new Separator());                         //adds a separator (special, as it acts as a separting line in visuals) _______ this type of linie
        
        //adding Filter Buttons
        //made a method for these to not repeat the code again and again
        sideBar.getChildren().add(createCategoryButton("All Vehicles", "ALL"));             
        sideBar.getChildren().add(createCategoryButton("Cars", "CAR"));
        sideBar.getChildren().add(createCategoryButton("Bikes", "BIKE"));
        sideBar.getChildren().add(createCategoryButton("Buses", "BUS"));
        sideBar.getChildren().add(createCategoryButton("Vans", "VAN"));

        return sideBar;
    }

    private Button createCategoryButton(String label, String categoryCode){
        Button btn = new Button(label);     //will create a button of the same name as the label is received
        btn.setMaxWidth(Double.MAX_VALUE);  //The Width of buttons will be the empty space(unlimited)
        btn.setStyle("-fx-background-color: #f9f9f9; -fx-cursor: hand; -fx-alignment: CENTER-LEFT;");   //the buttons are styled, backgound color (light greyish), cursor is hand , and allignment to be CENTRE(Vertically) LEFT(Horizontally) 

        btn.setOnAction(e -> {                              //catches teh even and implementation is gievn
            this.selectedCategory = categoryCode;           //updates selection
            refreshVehicles();                              //refreshes the vehicle cards
        });
        
        return btn;                                         //returns the button
    }

    //3.-------------------DYNAMIC CONTENT LOGIC--------------------------------
    
    //reads from system list and draws tiles (cards)
    private void refreshVehicles(){                     
        vehicleGrid.getChildren().clear();              //clear old tiles

        ArrayList<Vehicle> allVehicles = system.getVehicleRepo().getAll();          //arraylist will store the arraylist that it will get from the Vehicle Repository System.

        for(Vehicle v : allVehicles){                                             //this will go through the arraylist one by one
            //filter logic
            boolean match = false;          //our flag for match
            if(selectedCategory.equals("ALL")) match = true;               //if the category is ALL then every vehicle is placed in grid(tiles)

            else if(selectedCategory.equals("CAR") && v instanceof Car) match = true;   ///if the category is CAR and v is intance of Car then match become true and this v is added to grid(tiles)   
            else if(selectedCategory.equals("BIKE") && v instanceof Bike) match = true;  //if the catogry is BIKA and v is Bike then match becomes true and the bike is added to teh grid
            else if(selectedCategory.equals("BUS") && v instanceof Bus) match = true;  //same as aboce
            else if(selectedCategory.equals("VAN") && v instanceof Van) match = true;  //same as above

            if(match){                              //if match is true it will add teh current loop v to the grid
                vehicleGrid.getChildren().add(createVehicleTile(v));
            }
        }
    }

    //creates the tile for ONE vehicle
    private VBox createVehicleTile(Vehicle v){              
        VBox card = new VBox(10);                       //VBOX is used for that with a spacing of 10
        card.setPadding(new Insets(15));                // the padding is 15 px
        card.setPrefWidth(220);                         //fixed width for consistent sizeing
        card.setAlignment(Pos.CENTER);                  //the card are alligned in teh centre of teh
        
        //defaultstyle (white box with shadow)s
        String defaultStyle = "-fx-background-color: white; -fx-background-radius: 10; " +             //this wil set backgound to white and the corners will be rounded by radius 10
                              "-fx-effect: dropshadow(gaussian , rgba(0,0,0,0.1), 5, 0, 0, 0);";     // gaussian is the shoadow algorithm, 5,0,0,0 (blur radius(softness of shadow), spread (how much shadow expands) , x offest(move shadow left right), y offset (move shadwo up down) )    
        
        //hover styling(Light Blue)
        String hoverStyle = "-fx-background-color: #cff0ffff; -fx-background-radius: 10; " +        //when mouse moves above it , the tile's backgound wil be light bluish, corner will be rounded by radius 10,  
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 0);";       //shadow effect, gaussian is shadow calculating algo , and rgba is color, 8000 is blur radius, speread , x and y offsets
        
        card.setStyle(defaultStyle);                                                                //card by default will have default styling that we made
        // Hover Events                 
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));                                     //catching mouse events, when the moouse hovers above the card... the styling will change to hover that we made(second)
        card.setOnMouseExited(e -> card.setStyle(defaultStyle));                                    //catching another event , when the mouse leaves the card... teh styling will change back to original

        //1. -----image alternate (Using Emojis based on Type)---
        String icon = "🏎️";                        //Default
        if(v instanceof Bike) icon = "🏍️";       //for bike    
        else if(v instanceof Bus) icon = "🚍";  //for bus
        else if(v instanceof Van) icon = "📦";  //for van

        //javaa can support emojis too becasue they are unicode  
        Label lblIcon = new Label(icon);               //label with the emoji in it :)
        lblIcon.setFont(Font.font(50));                 //size of the label text(emoji) incrased to 50  
        
        //------TEXT DETAILS-------------
        Label lblName = new Label(v.getMake() + " " + v.getModel());            //label with the vehicle's make and model(using getters of vehicle.java)
        lblName.setFont(Font.font("", FontWeight.BOLD, 16));                    //the font is set to bold and the size is set to 16      

        //price logic(red if discounted)
        VBox priceBox = new VBox(2);                                //new Vbox for price wiht a spacing of 2
        priceBox.setAlignment(Pos.CENTER);                          //allignment seet to centre
        
        if(v.getIsDiscountAvailable()){                                 //checks the discount (using the getters from Vehicle.java)
            Label oldPrice = new Label("$" + v.getBaseRatePerDay());    // if the discount is available then it will get the old price using geter from vehicle.java again
            oldPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: #999;");  //the old price will have an effect which will get stricked through and the color of text will become grey

            Label newPrice = new Label("$" + v.getCurrentRate() + " / day");        //new price label creaeted and new price is getted using getter of vehicle.java
            newPrice.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");        //the new price color will become red and the font will be bold 
            
            priceBox.getChildren().addAll(oldPrice, newPrice);                      //VBOX(priceBox) will get both the old and new value
        }
        else{                                                             //if tehre is no discount then it will only show the origianl price
            Label price = new Label("$" + v.getBaseRatePerDay() + " / day");   //gets the base rate perday usign the getter from vehcile.java
            price.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");     //color to green and weigth to bold
            priceBox.getChildren().add(price);                                  //feed into priceBox (Vbox)
        }

        //status Text
        Label lblStatus = new Label(v.getAvailibility() ? "Available" : "Rented Out");                  //if the car is available label is set to Available else Rented Out (this is ternary operator short form of if else)
        lblStatus.setStyle(v.getAvailibility() ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: red;");  //based on the availibility the text color is chosen if available then color wil be green else red (Again ternary operator short form of if else) 

        //----------3.RENT BUTTON------------
        Button btnRent = new Button("Rent Now!");                   //creating a button rent Now
        btnRent.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;"); //the button will be of color green with white text and cursor will be hand when over it
        btnRent.setPrefWidth(180);                              //button's width is 180 piexels (chorai of button)

        //if rented, disable button
        if(!v.getAvailibility()){                                   //if vehicle is not available then the button shouldnt show rentnow       
            btnRent.setDisable(true);                               //so we diable the button (a method of button)
            btnRent.setText("Unavailable");                         //Sets the button's text to Unavailable
            btnRent.setStyle("-fx-background-color: #999; -fx-text-fill: white;");  //color of button will be grey and text is white in it
        }

        //------------------------action of rentnow button--------------------------------------
        btnRent.setOnAction(e -> {                      
           if(sceneManager.isLoggedIn()){
                //if logged in, show rent dialog;''
                RentDialog dialog = new RentDialog(system, v,sceneManager.getCurrentUser());
                dialog.show();                            

                refreshVehicles();                          //refresh the grid to update availiblity status to rented
            }
            else{
                //if not logged in, force login;
                sceneManager.showLoginPage();
            }
        });

        //adding everything to the card
        card.getChildren().addAll(lblIcon, lblName, priceBox, lblStatus, new Separator(), btnRent);
        return card;                //returns the card (VBOX)
    }

    public BorderPane getView(){                    //this will return the BorderPane 
        return root;                                
    }
}