package com.bilal.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import com.bilal.models.*;
import com.bilal.models.users.Customer;
import java.time.LocalDate;

public class CustomerDashboard{

    private SceneManager sceneManager;     //reference to scene manager to switch scenes
    private CarRentalSystem system;        //reference to backend system
    private Customer customer;             //the logged in customer whose dashboard is showing
    private BorderPane root;               //root layout of this dashboard window

    private TableView<RentalRecord> activeTable;   //table for active rentals (or overdue) with buttons to return/cancel
    private TableView<RentalRecord> historyTable;  //table for past rental history, readonly

    //constructor
    public CustomerDashboard(SceneManager sceneManager, CarRentalSystem system, Customer customer){
        this.sceneManager = sceneManager;        //assign scene manager
        this.system = system;                    //assign backend system refernece
        this.customer = customer;                //assign current user
        createView();                            //call method that will build the whole UI
    }

    //------CREATE VIEW()--------
    private void createView(){
        root = new BorderPane();                        //BorderPane is main layout type, lets us set top, center, bottom, left, right easily
        root.setStyle("-fx-background-color: #f4f4f4;"); //background set

        root.setTop(createTopBar());                   //top bar with title + home/logout buttons

        VBox content = new VBox(20);                   //VBox layout with spacing 20px
        content.setPadding(new Insets(30));           //padding from all sides

        //welcome label
        Label lblWelcome = new Label("Welcome back, " + customer.getName());
        lblWelcome.setFont(Font.font("", FontWeight.BOLD, 24));    //bold font, size 24
        
        //active rentals section label
        Label lblActive = new Label("Active & Overdue Rentals");
        lblActive.setFont(Font.font("", FontWeight.BOLD, 16));

        activeTable = createTable(true);      //true means table has buttons for return/cancel (interactive)

        //past rentals section
        Label lblHistory = new Label("Past Rental History");
        lblHistory.setFont(Font.font("", FontWeight.BOLD, 16));

        historyTable = createTable(false);    //false means table is redonly (no buttons)

        //add all nodes to VBox content in right order
        content.getChildren().addAll(
            lblWelcome, new Separator(), 
            lblActive, activeTable, new Separator(), 
            lblHistory, historyTable
        );

        root.setCenter(content);             //put the content in the center of BorderPane
        
        refreshTables();                     //fills the tables with data from customer rental history
    }

    //---------TOP BAR(TITLE + BUTTONS)-------------
    private HBox createTopBar(){
        HBox topBar = new HBox(20);                       //HBox with 20px spacing
        topBar.setPadding(new Insets(15, 30, 15, 30));    //padding top, right, bottom, left
        topBar.setAlignment(Pos.CENTER_LEFT);             //align everything left
        topBar.setStyle("-fx-background-color: #2c3e50;"); //dark blue background

        Label lblTitle = new Label("MY DASHBOARD");       //tilte label
        lblTitle.setTextFill(javafx.scene.paint.Color.WHITE); //text white
        lblTitle.setFont(Font.font("", FontWeight.BOLD, 20)); //bold 20px font

        Region spacer = new Region();                     //spacer is invisible node used to push buttons to right
        HBox.setHgrow(spacer, Priority.ALWAYS);          //let spacer expand horizontally

        Button btnHome = new Button("Back to Home");     //button to go back home
        btnHome.setOnAction(e -> sceneManager.showHomePage());  //on click, switch scene

        Button btnLogout = new Button("Logout");         //logout button
        btnLogout.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;"); //red bg, white text
        btnLogout.setOnAction(e -> sceneManager.logout());    //call logout function

        topBar.getChildren().addAll(lblTitle, spacer, btnHome, btnLogout);  //add everything to topBar
        return topBar;  //return the topbar HBox
    }

    //------TABLE CREATION(ACTIVE OR HISTORY)-----------
    @SuppressWarnings("unchecked")
    private TableView<RentalRecord> createTable(boolean withActions){
        TableView<RentalRecord> table = new TableView<>();       //new table for rentals

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);  //columns stretch to fill width
        table.setPrefHeight(200);                                   //table height

        //-----VEHICLE COLUMN----
        TableColumn<RentalRecord, String> colVehicle = new TableColumn<>("Vehicle");
        //cell value factory tells the column what data to show for each row
        colVehicle.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getRentedVehicle().getMake()+" "+cell.getValue().getRentedVehicle().getModel())         //getValue() gets the rentalRecord obeject , then the getstatus is its getter, the simpleStringproerty is observable string (Wrapper around String)                 
        );
        //SimpleStringProperty is used because TableView works with ObservableValues, not raw strings

        //----STATUS COLUMN----
        TableColumn<RentalRecord, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getStatus())               //getValue() gets the rentalRecord obeject , then the getstatus is its getter, the simpleStringproerty is observable string (Wrapper around String)
        );
        //status will be ACTIVE, OVERDUE, COMPLETED

        //----RETURN DATE COLUMN----
        TableColumn<RentalRecord, LocalDate> colDate = new TableColumn<>("Return Date");
        colDate.setCellValueFactory(cell -> 
            new SimpleObjectProperty<>(cell.getValue().getReturnDate())
        );
        //SimpleObjectProperty is used for non-String types (LocalDate here)

        //----TOTAL COST COLUMN----
        TableColumn<RentalRecord, String> colCost = new TableColumn<>("Total Cost");
        colCost.setCellValueFactory(cell -> 
            new SimpleStringProperty("$" + cell.getValue().getTotalCost())
        );

        table.getColumns().addAll(colVehicle, colStatus, colDate, colCost); //add columns

        //--------ACTIONS COLUMN(RETURN / CANCEL)------------
        if(withActions){
            TableColumn<RentalRecord, Void> colAction = new TableColumn<>("Actions");

            colAction.setCellFactory(param -> new TableCell<>(){
                private final Button btnReturn = new Button("Return");   //return button
                private final Button btnCancel = new Button("Cancel");   //cancel button
                private final HBox pane = new HBox(5, btnReturn, btnCancel); //hbox to hold buttons

                @Override
                protected void updateItem(Void item, boolean empty){
                    super.updateItem(item, empty);

                    if(empty){
                        setGraphic(null);   //empty row? no buttons
                    } else{                     //this line simply stores the Rentalrecord from the rows in the refrence
                        RentalRecord record = getTableView().getItems().get(getIndex()); //gets rental record using the row number(getIndex),
                                //then we get the ObservableList<RentalRecord> object (getItmes), ten 

                        btnReturn.setOnAction(e -> handleReturn(record));  //call return function
                        btnCancel.setOnAction(e -> handleCancel(record));  //call cancel function

                        //styling buttons
                        btnReturn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        btnCancel.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                        setGraphic(pane);   //set HBox with buttons in cell
                    }
                }
            });

            table.getColumns().add(colAction);   //add actions column
        }

        return table;  //return table ready to use
    }

    //----------REFRESH TABLE DATA------
    private void refreshTables(){
        activeTable.getItems().clear();   //clear previous data
        historyTable.getItems().clear();

        for(RentalRecord r : customer.getRentalHistory()){   //iterate over all rentals
            r.checkOverDue();           //update status based on current date

            if(r.getStatus().equals("ACTIVE") || r.getStatus().equals("OVERDUE")){
                activeTable.getItems().add(r);   //add to active table
            } else{
                historyTable.getItems().add(r);  //add to past table
            }
        }
    }

    //------HANDLE RETURN VEHICLE---------
    private void handleReturn(RentalRecord record){
        try{
            system.returnVehicle(record.getRentalId());   //tell backend system vehicle is returned

            Alert alert = new Alert(Alert.AlertType.INFORMATION);  //popup info
            alert.setContentText("Vehicle Returned! Thanks For using our service :)");
            alert.showAndWait();   //wait user ok

            refreshTables();      //refresh tables after return
        } catch(Exception e){
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();  //error alert
        }
    }

    //------HANDLE CANCEL VEHICLE---------
    private void handleCancel(RentalRecord record){
        try{
            record.cancelRental();   //cancel in memory, not saved unless system.saveData() called later
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Booking Cancelled! Cash will be transfered to you!");
            alert.showAndWait();     //wait user ok

            refreshTables();         //refresh tables after cancel
        } catch(Exception e){
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();  //error alert
        }
    }

    //------GET ROOT VIEW FOR SCENE MANAGER------------
    public BorderPane getView(){
        return root;    //return the whole BorderPane to be set in scene
    }
}
