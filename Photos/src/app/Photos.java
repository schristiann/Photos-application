package app;

import Controller.AppContext;

import Controller.MainController;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import Model.Album;
import Model.Photo;
import Controller.LoginController;
import Controller.PhotosController;


import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import java.util.stream.Collectors;

/**
 * Controller for the initial photo application start up that calls the login fxml view
 * @author Anna Faytelson and Sam Christian 
 */

public class Photos extends Application {


private LoginController loginController;
    public static void main(String[] args) {


        Application.launch(args);


    }


/**
 * starts the application and sets the login view
 */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                getClass().getResource("/View/login.fxml"));
        AnchorPane root = loader.load();

        loginController = loader.getController();
        loginController.start(stage);

        Scene scene = new Scene(root, 1200, 1600);
        stage.setScene(scene);
        stage.setTitle("Photo Album Application");
        stage.show();

        stage.show();
    }
    
   @Override
    public void stop() {
        System.out.println("Shutting down, saving the file");
        try {
            MainController.storeMap();
        }
        catch(Exception ioe) {
            System.out.println("Failed to save songs " + ioe.getLocalizedMessage());
        }
    }


}
