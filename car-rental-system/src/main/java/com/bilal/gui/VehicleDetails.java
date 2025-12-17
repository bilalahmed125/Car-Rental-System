package com.bilal.gui;


import com.bilal.models.*;
import com.bilal.models.users.Customer;
import com.bilal.models.vehicles.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import javafx.scene.control.TextArea;

public class VehicleDetails {

    private Vehicle currentVehicle;         //vehicle for whom we have to check details
    private SceneManager manager;           //manager   
    private CarRentalSystem system;         //the system
    private BorderPane root;

    public VehicleDetails(SceneManager manager ,CarRentalSystem system ,Vehicle currentVehicle){
        this.system = system;
        this.currentVehicle = currentVehicle;
        this.manager = manager;

        createView();               //will build the UI for this page;

    }

    public void createView(){
        root = new BorderPane();
        root.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");

        root.setTop(createTopBar());
        root.setLeft(createSideBar());
        root.setCenter(createCenterContent());
    }

    //creation of top bar,
    private HBox createTopBar(){
        HBox top = new HBox(20);
        top.setPadding(new Insets(15,30,15,30));       //trbl
        top.setStyle("-fx-background-color: #2c3e50;");
        top.setAlignment(Pos.CENTER_RIGHT);                 //verticaly centre + horizontally right side

        Label lblTitle = new Label("VEHICLE DETAILS");
        lblTitle.setStyle("-fx-text-fill: white;"); 
        lblTitle.setFont(Font.font("",FontWeight.BOLD,20));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);         //hamesha space lega

        Button home = new Button("Home");
        Button btnRent = new Button("Rent Now");
        Button back = new Button("<- Back");

        home.setStyle("-fx-background-color: rgba(216, 216, 216, 0.96); -fx-cursor: hand;");
        if(!currentVehicle.getAvailibility()){
                btnRent.setDisable(true);                           
                btnRent.setText("Unavailable");                         
                btnRent.setStyle("-fx-background-color: #999; -fx-text-fill: white;"); 
            }
        else
        btnRent.setStyle("-fx-background-color: linear-gradient(to right, rgba(72, 172, 75, 1) , rgba(68, 83, 222, 1)); -fx-text-fill: white; -fx-cursor: hand;");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-cursor: hand;");

        home.setOnAction(e -> manager.showHomePage());


        btnRent.setOnAction(e-> {
        if(manager.isLoggedIn()){
            if(currentVehicle.getAvailibility()){ 
            RentDialog dialog = new RentDialog(system, currentVehicle, manager.getCurrentUser());
            dialog.show(); 
        }
            
        
        }else{
            manager.showLoginPage();
        } 
        });

        back.setOnAction(e -> manager.returnToPreviousScene());

        top.getChildren().addAll(back,lblTitle,spacer,home,btnRent);
        return top;
    }
        //SIDEBAR CREATION
    private VBox createSideBar(){
        VBox sideBar = new VBox(15);
        sideBar.setPadding(new Insets(30,15,30,15));
        sideBar.setStyle("-fx-background-color: rgba(243, 243, 243, 0.86);");
        sideBar.setAlignment(Pos.TOP_LEFT);
        sideBar.setPrefWidth(150);

        if(!manager.isLoggedIn()){
            Button login = new Button("Login");
            Button signUp = new Button("SignUp");

            login.setMaxWidth(Double.MAX_VALUE);
            signUp.setMaxWidth(Double.MAX_VALUE);

            String style = "-fx-background-color: #e5cce1ff ; -fx-text-fill: white; -fx-cursor: hand; " ;
            login.setStyle(style);
            signUp.setStyle(style);

            login.setOnAction(e -> manager.showLoginPage());
            signUp.setOnAction(e -> manager.showSignupPage());
            
            sideBar.getChildren().addAll(login,signUp);
        }
        else if(manager.getCurrentUser() instanceof Customer){
            Button btnDash = new Button("My Dashboard");
            Button logOut = new Button("LogOut");

            btnDash.setStyle("-fx-background-color: rgba(78, 209, 113, 0.88); -fx-cursor: hand;");
            btnDash.setOnAction(e-> manager.showDashboard());

            logOut.setStyle("-fx-background-color: rgba(199, 101, 94, 1); -fx-cursor: hand;");
            logOut.setOnAction(e -> manager.logout());

            sideBar.getChildren().addAll(btnDash,logOut);
        }
        else{
            Button btnDash = new Button("My Dashboard");
            Button btnAddRec = new Button("Add Maintenance Record");

            btnDash.setStyle("-fx-background-color: rgba(78, 209, 113, 0.88); -fx-cursor: hand;");
            btnDash.setOnAction(e-> manager.showDashboard());

            btnAddRec.setStyle("-fx-background-color: rgba(250, 184, 179, 1); -fx-cursor: hand;");
            btnAddRec.setOnAction(e -> addMaintenanceRecord());

            sideBar.getChildren().addAll(btnDash, btnAddRec);
        }


        return sideBar;
    }
    //add maintenecae record
    private void addMaintenanceRecord(){        
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Maintenance Record");

        VBox dialog = new VBox(15);
        dialog.setPadding(new Insets(20));
        dialog.setPrefWidth(500);
        dialog.setAlignment(Pos.CENTER);
        
        Label lblhead = new Label("ADD Maintenance Record for "+currentVehicle.getMake()+" "+currentVehicle.getModel());
        lblhead.setStyle("-fx-text-fill: rgba(47, 47, 47, 1); -fx-font-weight: bold; -fx-font-size: 20;");

        DatePicker date = new DatePicker(LocalDate.now());
        date.setPromptText("Please pick a Date");
        date.setMaxWidth(Double.MAX_VALUE);

        TextField cost = new TextField();
        cost.setPromptText("Please enter the cost (>0)");
        cost.setMaxWidth(Double.MAX_VALUE);
        
        TextArea desc = new TextArea();
        desc.setPromptText("Please add a descritpion [optional]");
        desc.setMaxWidth(Double.MAX_VALUE);
        
        if(desc.getText().isEmpty()){
            desc.setText("Unknown");
        }

        Button btnAdd = new Button("Add");
        btnAdd.setStyle("-fx-background-color: #afff5eff; -fx-text-fill:white; -fx-cursor: hand;");
        
        btnAdd.setOnAction(e->{
        if(!(cost.getText().isEmpty() || date.getValue() == null)){
            
                currentVehicle.addMaintenanceRecord(desc.getText(), date.getValue(), Double.parseDouble(cost.getText()));
                new Alert(Alert.AlertType.INFORMATION, "Maintenance Record Added").show();
                stage.close();
        }
        else{
            new Alert(Alert.AlertType.ERROR, "Non Optional Fields are Empty!").show();
        }
        });
        
        dialog.getChildren().addAll(lblhead,date,cost,desc,btnAdd);
        stage.setScene(new Scene(dialog));
        stage.showAndWait();

    }   


    //CENTRE BOX
    private VBox createCenterContent(){
        VBox content = new VBox(15);
        content.setPadding(new Insets(5,10,5,10));
        content.setAlignment(Pos.CENTER);

        Label vehType = null;
        if(currentVehicle instanceof Car)   vehType = new Label("VEHICLE TYPE: CAR");
        else if( currentVehicle instanceof Bike) vehType = new Label("VEHICLE TYPE: BIKE");
        else if( currentVehicle instanceof Bus) vehType = new Label("VEHICLE TYPE: BUS");
        else if (currentVehicle instanceof Van) vehType = new Label ("VEHICLE TYPE: VAN");
        else vehType = new Label("VEHICLE TYPE: N/A");

        vehType.setStyle("-fx-text-fill: rgba(133, 223, 241, 1); -fx-font-weight: bold; -fx-font-size:18;");

        Label lblTitle = new Label(currentVehicle.getMake() +" "+ currentVehicle.getModel()+" \t\t\t (ID: "+currentVehicle.getVehicleId()+")");
        lblTitle.setFont(Font.font("",FontWeight.BOLD,22));
        lblTitle.setStyle("-fx-text-fill: linear-gradient(to left, rgba(70, 227, 255, 1), #ff6e81ff ); -fx-font-size: 32; -fx-font-weight: bold;");

        Label lblAvailable = new Label(currentVehicle.getAvailibility()? "Available" : "Rented Out");
        lblAvailable.setStyle(currentVehicle.getAvailibility()? "-fx-text-fill: rgba(94, 192, 72, 1); -fx-font-weight: bold;"
        +" -fx-font-size: 16;" : "-fx-text-fill: rgba(225, 44, 44, 1); -fx-font-weight: bold;" +
        "-fx-font-size: 16;");

        VBox priceBox = new VBox(15);
        priceBox.setAlignment(Pos.CENTER);
        if(currentVehicle.getIsDiscountAvailable()){
            Label oldPrice = new Label("$ "+currentVehicle.getBaseRatePerDay());
            Label newPrice = new Label("$ "+currentVehicle.getCurrentRate()+" /day");
            
            oldPrice.setStyle("-fx-text-fill: rgba(163, 163, 163, 0.86); -fx-font-size: 15;");
            newPrice.setStyle("-fx-text-fill: rgba(117, 225, 98, 1); -fx-font-size: 20; -fx-font-weight: bold;");
            priceBox.getChildren().addAll(oldPrice,newPrice);
        }
        else{
            Label price = new Label("$ "+currentVehicle.getBaseRatePerDay());
            price.setStyle("-fx-text-fill:  orange ; -fx-font-weight: bold; -fx-font-size: 19;");
            priceBox.getChildren().add(price);
        }
        
        content.getChildren().addAll(lblTitle,vehType,new Separator(),lblAvailable,priceBox,createMaintenanceTable());
        return content;
    }
    //Helper creator of table for the centre 
    private TableView<MaintenanceRecord> createMaintenanceTable(){
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        TableView<MaintenanceRecord> table = new TableView<>();

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MaintenanceRecord, String> colSr = new TableColumn<>("Sr No.");
        colSr.setCellFactory(cell -> new TableCell<>(){
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item,empty);

                if(empty){
                    setText(null);
                }
                else{
                    setText((getIndex()+1)+".");
                }
            }
        });

        TableColumn<MaintenanceRecord, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate().toString()));

        TableColumn<MaintenanceRecord, String> colCost = new TableColumn<>("Cost");
        colCost.setCellValueFactory(cell -> new SimpleStringProperty("$ "+ cell.getValue().getCost()));

        TableColumn <MaintenanceRecord, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));

        table.getColumns().addAll(colSr,colDate,colCost,colDesc);
        table.getItems().setAll(currentVehicle.getMaintenanceRecord());

        return table;
    }

    public BorderPane getView(){
        return root;
    } 

}
