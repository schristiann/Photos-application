package Controller;

import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Album;
import Model.Photo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Class for handling the login view. Goes to admin scene if admin username is entered else goes to the specified existing user's album view
 * @author Anna Faytelson and Sam Christian
 */
public class LoginController extends MainController implements Initializable{

    @FXML
    TextField loginId;

    @FXML
    Button loginButton;
    
    Map<String, List<Album>> albumStore;
    /**
     * logs the user in, or goes to the admin page to edit users.  Alerts the user if the input is invalid
     * @param event
     */
    @FXML
    public void doLogin(ActionEvent event) {
        System.out.println("Login was pressed");
//        updateGrid.getChildren().clear();

        try {
        		if(loginId.getText().equalsIgnoreCase("admin")) {
        			changeScene("/view/admin.fxml");
        		}
        	
        		else if(albumStore.containsKey(loginId.getText())) {
        			System.out.println("I found it");
        			appContext.setUserId(loginId.getText());
        			 List<Album> albumList= albumStore.get(loginId.getText());
        	            for(Album album:albumList) {
        	            	System.out.println(album);
//        	            	changeScene("/photos/view/Albums.fxml");
//        	            AlbumController albumcontrol=new AlbumController();
//        	            albumcontrol.populateAlbums(albumList);
        		}
        	        	changeScene("/view/Albums.fxml");
            
            } else {
            		showMessage("Invalid UserId", "User: " + loginId.getText() + " does not exist", Alert.AlertType.ERROR);
            }
        		
        }
        catch (IOException ioe) {
            System.err.println("Caught exception");
            ioe.printStackTrace();
        }


    }
/**
 * reads in the serializable if it exists, and reads in the stock settings if no serializable exists
 */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
    	MainController file=new MainController();
//		albumStore=file.createAlbums();
//		file.storeAlbums(albumStore);
		
    		loginButton.setDefaultButton(true);
		albumStore = file.readAlbumStore();
		appContext.setMap(albumStore);
		System.out.println("Found album store with: " + albumStore.size() + " users");
    
    	
    }
    
    public void start(Stage stage) {
    		
        appContext.setStage(stage);
       
    }
    
    
}