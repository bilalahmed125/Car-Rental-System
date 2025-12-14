//package com.bilal;
package com.bilal;

import com.bilal.models.*;
import com.bilal.gui.*;

import javafx.application.Application;

import javafx.stage.Stage;



public class Main extends Application{
    @Override
    public void start(Stage stage){
        CarRentalSystem system = new CarRentalSystem();
        SceneManager scenemanager = new SceneManager(stage,system);
        scenemanager.showLoginPage();
        stage.show();
    
    }
    public static void main(String args[]){
        launch();
    }
}
