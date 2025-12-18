package com.bilal.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import com.bilal.models.*;
import com.bilal.models.users.Admin;
import com.bilal.models.users.Customer;
import com.bilal.models.vehicles.*;
@SuppressWarnings("unused")


public class AdminDashboard{

    private SceneManager sceneManager;                   //this will help switch between pages n stuff
    private CarRentalSystem system;                      //main system 

    private Admin admin;                                //logged in admin user, very imp
    private BorderPane root;                         //main layout container, borderpane cuz top left center etc

    public AdminDashboard(SceneManager sceneManager, CarRentalSystem system, Admin admin){
        this.sceneManager = sceneManager;                 //init scene manager
        this.system = system;                           //assign the system
        this.admin = admin;                             //store the admin object
        createView();                                   //call method to build GUI view
    }

    private void createView(){
        root = new BorderPane();                                            //our main container for dashboard
        root.setStyle("-fx-background-color: #f4f4f4;");                 //light gray bg

        root.setTop(createTopBar());                                        //add the top bar nav thing

                        //----------------SideBAR------------- 
        VBox sidebar = new VBox(10);                          //vertical menu buttons, gap 10
        sidebar.setPadding(new Insets(20));                     //inner spacing
        sidebar.setPrefWidth(220);                           //fixed width for sidebar
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setStyle("-fx-background-color: #fff; -fx-border-width: 0 1 0 0;");                 //white bg, border right

        Button btnStats = createSideBarButton("Fleet Statistics");                   //button for stats view
        Button btnVehicles = createSideBarButton("Manage Vehicles");                 //manage vehs btn
        Button btnUsers = createSideBarButton("Manage Users");                      //user mgmt btn
        Button btnRentals = createSideBarButton("Active Rentals");                  //active rents btn

        btnStats.setOnAction(e -> showStatsView());                                     //show stats view when clicked
        btnVehicles.setOnAction(e -> showVehiclesView());                               //veh table view
        btnUsers.setOnAction(e -> showUsersView());                                     //users table view
        btnRentals.setOnAction(e -> showRentalsView());                                 //rentals table view

        sidebar.getChildren().addAll(btnStats, btnVehicles, btnUsers, btnRentals);                 //add all btns to sidebar
        root.setLeft(sidebar);                                                          //attach sideba to left

        showStatsView();                                                                //default view is stats
    }

                    //---VIEW1 is STATISTICS------
    private void showStatsView(){
        VBox content = new VBox(20);                                            //container for main content, 20 gap
        content.setPadding(new Insets(30));                                      //outer padding
        content.setAlignment(Pos.TOP_CENTER );                                      //will make the vbox data in centre top of the border pane cntre
        Label lblTitle = new Label("System Overview");                        //title label
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 24));                 //big bold font

        TextArea txtStats = new TextArea(system.getVehicleRepo().getFleetStatistics());          //get fleet stats
        txtStats.setEditable(false);                                                             //dont allow edit
        txtStats.setPrefHeight(200);                                                            //fix height
        txtStats.setFont(Font.font("",FontWeight.BOLD,18));                                     //size is 18 + bold

        Label lblUsers = new Label(system.getUserRepo().getUserStatistics());                 //user stats label
        lblUsers.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");                 //dark blue bold

        //totla money calcualtion
        double totalMoney = 0;
        //loop throgh all rentals to sum up teh costs
        for(RentalRecord r : system.getRentalRepo().getAll()){
            if(r.getStatus().equals("ACTIVE") || r.getStatus().equals("COMPLETED") || r.getStatus().equals("OVERDUE"))
                totalMoney += r.getTotalCost(); //adding cost to total
            }
        
        //Show Total money Label
        Label lblCash = new Label("Total Cash: $" + totalMoney);
        lblCash.setFont(Font.font("", FontWeight.BOLD, 20));
        lblCash.setStyle("-fx-text-fill: #27ae60;"); //gren color for money

        content.getChildren().addAll(lblTitle, lblUsers, txtStats,lblCash);                 //add to content container
        root.setCenter(content);                 //set content in center
    }

                    //-------------VIEW2 iS MANAGEVEHICLES--------------
    @SuppressWarnings("unchecked")
    private void showVehiclesView(){
        VBox content = new VBox(15);                         //main container for vehicles
        content.setPadding(new Insets(20));                 //padding around content

        HBox header = new HBox(20);                         //header container
        Label lblTitle = new Label("Manage Fleet");         //header label
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 22));    //bold header font
        
        Region spacer = new Region();                            //spacer for layout
        HBox.setHgrow(spacer, Priority.ALWAYS);                   //grow to fill space
 
        Button btnAdd = new Button("+ Add New Vehicle");          //add new vehicle btn
        btnAdd.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");                //green btn style
        btnAdd.setOnAction(e -> openAddVehicleWindow());                                          //open add window

        header.getChildren().addAll(lblTitle, spacer, btnAdd);                                   //add header elements

        TableView<Vehicle> table = new TableView<>();                                            //table to show vehs
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);                       //auto column size

        TableColumn<Vehicle, String> colId = new TableColumn<>("ID");                                             //vehicle id column
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVehicleId()));                 //bind value

        TableColumn<Vehicle, String> colMake = new TableColumn<>("Make");                                //vehicle make
        colMake.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMake()));                 //bind

        TableColumn<Vehicle, String> colModel = new TableColumn<>("Model");                                     //vehicle model
        colModel.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getModel()));                 //bind

        TableColumn<Vehicle, String> colRate = new TableColumn<>("Rate");                                                //rate column
        colRate.setCellValueFactory(cell -> new SimpleStringProperty("$" + cell.getValue().getCurrentRate()));                 //add $

        TableColumn<Vehicle,String> colDisc = new TableColumn<>("Discounted");
        colDisc.setCellValueFactory(cell -> new SimpleStringProperty((cell.getValue().getIsDiscountAvailable())? "Yes" : "No"));
                        //---ACTIONS COL---
        TableColumn<Vehicle, Void> colAction = new TableColumn<>("Actions");                                    //col for action buttons
        
        colAction.setCellFactory(param -> new TableCell<>(){
            private final Button btnDel = new Button("Delete");                                             //delete button
            private final Button btnDisc = new Button("Discount");                                           //discount button
            private final HBox pane = new HBox(5, btnDisc, btnDel);                                            //wrap btns in HBox

            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                
                if(empty){
                    setGraphic(null);                                                                                  //empty cell
                }else{
                    Vehicle v = getTableView().getItems().get(getIndex());                                       //get vehicle object

                    btnDel.setOnAction(e -> handleDelete(v));                                                   //delete action
                    btnDisc.setOnAction(e -> openDiscountWindow(v));                                            //discount action

                    btnDel.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");                 //red del btn
                    btnDisc.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");                 //orange disc btn

                    setGraphic(pane);                                                                            //set HBox as cell graphic
                }
            }
        });
        
        table.getColumns().addAll(colId, colMake, colModel, colRate,colDisc, colAction);                 //add cols
        table.getItems().addAll(system.getVehicleRepo().getAll());                                       //populate rows

        content.getChildren().addAll(header, table);                                                      //add header + table
        root.setCenter(content);                                                                         //attach to center
    }

                //----------------VIEW3 is MANAGEUSERS-------------------------
    @SuppressWarnings("unchecked")
    private void showUsersView(){
        VBox content = new VBox(15);                                  //content container
        content.setPadding(new Insets(20));                             //padding

        Label lblTitle = new Label("Manage Customers");                 //header label
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 20));           //bold font

        TableView<Customer> table = new TableView<>();                  //table for users
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);                   //auto resize

        TableColumn<Customer, String> colId = new TableColumn<>("User ID");                 //user id col
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUserId()));                 //bind

        TableColumn<Customer, String> colName = new TableColumn<>("Name");                                        //name col
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));                 //bind

        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");                                        //email col
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));                 //bind

        TableColumn<Customer, Void> colAction = new TableColumn<>("Action");                                        //delete btn col
        colAction.setCellFactory(param -> new TableCell<>(){
            private final Button btnDelete = new Button("Delete User");                                             //delete btn

            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);                 //nothing to show
                }else{
                    Customer c = getTableView().getItems().get(getIndex());                     //get current user
                    btnDelete.setOnAction(e -> handleDeleteUser(c));                            //attach delete logic
                    btnDelete.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");                 //red btn
                    setGraphic(btnDelete);                                                              //add btn to cell
                }
            }
        });

        table.getColumns().addAll(colId, colName, colEmail, colAction);                 //add cols

        for (User u : system.getUserRepo().getAll()){                 //loop all users
            if (u instanceof Customer){                                 //only customers
                table.getItems().add((Customer) u);                 //add to table
            }
        }

        content.getChildren().addAll(lblTitle, table);                 //add to content
        root.setCenter(content);                 //attach to center
    }

                //---------------VIEW4 IS ACTIVERENTALS-----------------
    @SuppressWarnings("unchecked")
    private void showRentalsView(){
        VBox content = new VBox(20);                 //main container
        content.setPadding(new Insets(30));                 //padding

        Label lblTitle = new Label("Currently Rented Out");                 //title
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 20));                 //font
        
        TableView<RentalRecord> table = new TableView<>();                 //rentals table
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);                 //auto sizing

        TableColumn<RentalRecord, String> colRecId = new TableColumn<>("ID");                 //rental id
        colRecId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRentalId()));                 //bind

        TableColumn<RentalRecord, String> colUser = new TableColumn<>("Customer");                 //customer col
        colUser.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getName()));                 //bind

        TableColumn<RentalRecord, String> colCar = new TableColumn<>("Vehicle");                                 //veh col
        colCar.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getRentedVehicle().getMake() + " " + cell.getValue().getRentedVehicle().getModel()
        ));                                                                                                      //concat make model

        TableColumn<RentalRecord, String> colDate = new TableColumn<>("Due Date");                               //due date
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReturnDate().toString()));                 //bind

        table.getColumns().addAll(colRecId, colUser, colCar, colDate);                                            //add cols
        table.getItems().addAll(system.getRentalRepo().getActiveRentals());                                      //populate table

        content.getChildren().addAll(lblTitle, table);                                                           //add title + table
        root.setCenter(content);                                                                                  //center
    }

                    //---ADDVEHICLEWINDOW-----
    private void openAddVehicleWindow(){
        Stage stage = new Stage();                 //new window
        stage.initModality(Modality.APPLICATION_MODAL);                 //block bg
        stage.setTitle("Add New Vehicle");                 //title

        VBox layout = new VBox(10);                        //vertical layout
        layout.setPadding(new Insets(20));                 //padding
        layout.setPrefWidth(300);                 //width

        ComboBox<String> cmbType = new ComboBox<>();                 //type selector
        cmbType.getItems().addAll("Car", "Bike", "Bus", "Van");                 //options
        cmbType.setValue("Car");                 //default

        TextField txtId = new TextField(); txtId.setPromptText("Vehicle ID");                 //id input
        TextField txtMake = new TextField(); txtMake.setPromptText("Make");                 //make
        TextField txtModel = new TextField(); txtModel.setPromptText("Model");                  //model
        TextField txtRate = new TextField(); txtRate.setPromptText("Daily Rate");                 //rate
        TextField txtSpec1 = new TextField(); txtSpec1.setPromptText("Doors / Seats / CC");                 //spec1
        TextField txtSpec2 = new TextField(); txtSpec2.setPromptText("Auto / Manual / AC (true/false)");                 //spec2

        Button btnSave = new Button("Save Vehicle");                 //save btn
        btnSave.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");                 //green style
        
        btnSave.setOnAction(e -> {                 //save action
            try {
                String type = cmbType.getValue();                 //get type
                String id = txtId.getText();                 //get id
                String make = txtMake.getText();                 //make
                String model = txtModel.getText();                 //model
                double rate = Double.parseDouble(txtRate.getText());                 //parse rate

                if (type.equals("Car")){
                    system.registerCar(id, make, model, rate, Integer.parseInt(txtSpec1.getText()), txtSpec2.getText());                 //car reg
                } else if (type.equals("Bike")){
                    system.registerBike(id, make, model, rate, Integer.parseInt(txtSpec1.getText()), Boolean.parseBoolean(txtSpec2.getText()));                 //bike reg
                } else if (type.equals("Bus")){
                    system.registerBus(id, make, model, rate, Integer.parseInt(txtSpec1.getText()), Boolean.parseBoolean(txtSpec2.getText()));                 //bus reg
                } else if (type.equals("Van")){
                    system.registerVan(id, make, model, rate, Double.parseDouble(txtSpec1.getText()));                 //van reg
                }

                showVehiclesView();                 //refresh table
                stage.close();                 //close window
                
            } catch (Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage());                 //show error
                alert.show();                 //popup
            }
        });

        layout.getChildren().addAll(new Label("Type:"), cmbType, txtId, txtMake, txtModel, txtRate, 
                                    new Label("Specifics:"), txtSpec1, txtSpec2, btnSave);                 //add to layout
        
        stage.setScene(new Scene(layout));                 //set scene
        stage.show();                 //show window
    }

                    //-----DISCOUNTWINDOW-------
    private void openDiscountWindow(Vehicle v){
        Stage stage = new Stage();                 //new window
        stage.initModality(Modality.APPLICATION_MODAL);                 //block bg
        stage.setTitle("Set Discount");                 //title

        VBox layout = new VBox(10);                 //vertical layout
        layout.setPadding(new Insets(20));                 //padding
        layout.setAlignment(Pos.CENTER);                 //center elements

        Label lblInfo = new Label("Set Discount % for " + v.getModel());                 //info lbl
        Label lblInfo2 = new Label("0 to remove discount!");
        lblInfo2.setStyle("-fx-font-size: 14; -fx-text-fill: rgba(67, 65, 63, 0.6)");
        TextField txtPercent = new TextField();                           //input field
        txtPercent.setPromptText("Enter % (0 to reset)");                 //placeholder

        Button btnApply = new Button("Apply Discount");                 //apply btn
        btnApply.setOnAction(e -> {                 //action handler
            try {
                double percent = Double.parseDouble(txtPercent.getText());                 //parse percent
                if(percent == 0){
                    v.resetPrice();                 //reset price
                } else{
                    system.setVehicleDiscount(v.getVehicleId(), percent);                 //set discount
                }
                showVehiclesView();                 //refresh
                stage.close();                 //close
            } catch (Exception ex){
                new Alert(Alert.AlertType.ERROR, "Invalid Number").show();                 //alert if invalid
            }
        });

        layout.getChildren().addAll(lblInfo, lblInfo2, txtPercent, btnApply);                 //add elements
        stage.setScene(new Scene(layout, 300, 200));                 //set scene size
        stage.show();                 //show
    }

                    //---DELETELOGIC---
    private void handleDelete(Vehicle v){
        try {
            boolean deleted = system.deleteVehicle(v.getVehicleId(), false);                 //try delete normally
            if (deleted){
                showVehiclesView();                 //refresh
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vehicle has history. Delete anyway?", ButtonType.YES, ButtonType.NO);                 //confirm dialog
                alert.showAndWait();                 //wait for answer
                
                if (alert.getResult() == ButtonType.YES){
                    system.deleteVehicle(v.getVehicleId(), true);                 //force delete
                    showVehiclesView();                 //refresh
                }
            }
        } catch (Exception e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();                 //show error
        }
    }

    private void handleDeleteUser(Customer c){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete user " + c.getName() + "?", ButtonType.YES, ButtonType.NO);                 //confirm user del
        alert.showAndWait();                 //wait

        if (alert.getResult() == ButtonType.YES){                 //if yes
            try {
                admin.deleteUser(system.getUserRepo(), c.getUserId());                 //delete user
                showUsersView();                 //refresh table
            } catch (Exception e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();                 //show error
            }
        }
    }

                    //---HELPERMETHODS-----
    private HBox createTopBar(){
        HBox topBar = new HBox(20);                 //container
        topBar.setPadding(new Insets(15, 30, 15, 30));                 //padding
        topBar.setAlignment(Pos.CENTER_RIGHT);                 //align right
        topBar.setStyle("-fx-background-color: #2c3e50;");                 //dark bg

        Label lblTitle = new Label("ADMIN DASHBOARD");                 //title label
        lblTitle.setStyle("-fx-text-fill: white");                 //white text
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 20));                 //bold font

        Region spacer = new Region();                 //spacer for layout
        HBox.setHgrow(spacer, Priority.ALWAYS);                 //grow spacer
        
        Button btnHome = new Button("Home");                 //home btn
        btnHome.setStyle("-fx-background-color: #a65e9ee8; ");
        btnHome.setOnAction(e -> sceneManager.showHomePage());                 //show home

        Button btnLogout = new Button("Logout");                 //logout btn
        btnLogout.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");                 //red btn
        btnLogout.setOnAction(e -> sceneManager.logout());                 //logout action

        topBar.getChildren().addAll(lblTitle, spacer, btnHome, btnLogout);                 //add all
        return topBar;                 //return bar
    }

    private Button createSideBarButton(String text){
        Button btn = new Button(text);                 //create button
        btn.setPrefWidth(Double.MAX_VALUE);                 //full width
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14px;");                 //style
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #eee;"));                 //hover
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent;"));                 //exit hover
        return btn;                 //return button
    }

    public BorderPane getView(){
        return root;                 //return main container
    }
}
