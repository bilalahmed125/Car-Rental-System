//package com.bilal;
package com.bilal;

import com.bilal.models.*;
import com.bilal.models.users.Admin;
import com.bilal.gui.*;

import javafx.application.Application;

import javafx.stage.Stage;



public class Main extends Application{
    @Override
    public void start(Stage stage){
        CarRentalSystem system = new CarRentalSystem();

        initializeSampleData(system);

        SceneManager scenemanager = new SceneManager(stage,system);
        scenemanager.showHomePage();
        stage.show();
    
    }
    public static void main(String args[]){
        launch();
    }

    @SuppressWarnings("exports")
    public void initializeSampleData(CarRentalSystem system){
        try {
            //---Create Default Admin----
            //ID = admin, Pass = admin123
            if(system.getUserRepo().getById("admin") == null){
                Admin admin = new Admin("admin", "Super Admin", "admin@rentawheel.com", "0000", "admin123", "Manager");
                system.getUserRepo().add(admin);
                System.out.println("Default Admin Created: ID=admin, Pass=admin123");
            }

            //=======Create Dummy Vehicles (if none exists)=========
            if(system.getTotalVehicles() == 0){
                //cars
                system.registerCar("C-001", "Toyota", "Corolla", 45.0, 4, "Automatic");
                system.registerCar("C-002", "Honda", "Civic", 50.0, 4, "Manual");
                
                //bike
                system.registerBike("B-001", "Yamaha", "R1", 25.0, 1000, true);
                
                //bus
                system.registerBus("BUS-01", "Volvo", "9700", 150.0, 50, true);
                
                //van
                system.registerVan("V-001", "Ford", "Transit", 70.0, 1200.0);

                System.out.println("Dummy Vehicles Added to Fleet");
            }

        } catch (Exception e){
            System.out.println("Error feding data: " + e.getMessage());
        }
    }

}
