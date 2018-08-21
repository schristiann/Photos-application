package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class controls the admin account for admin usernames entered through the login page
 * @author Anna Faytelson and Sam Christian
 */

public class AdminController extends MainController implements Initializable {
	
    /**
     * ListView for List of Users
     */
    @FXML
    public ListView<String> listView;

    /**
     * TextField for creating a new user
     */
    @FXML
    public TextField create_name;
    public TextField delete_name;
    
    private ObservableList<String> users;
    
    /**
     * Initializes the admin scene
     * The Initialize method is basically a constructor for this class
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    		users = appContext.getUsers();
    		showListView();
	}
/**
 * Logs the user out, returns to logout page
 * @param e
 * @throws IOException
 */
    public void handleLogout(ActionEvent e) throws IOException {
    		//go to start method of login
    		MainController.storeMap();
    		//have to logout differently so that it goes to login page and then login page goes
    		//to dologin with correct username
    		changeScene("/View/login.fxml");
    }
    
    public void handleQuit(ActionEvent e) throws IOException {
    		Platform.exit();
    }
    
    /**
     * creates a new user album set, alerting the user if the user exists or the field is empty
     * @param e
     * @throws IOException
     */
    @FXML
    public void handleCreateNewUser(ActionEvent e) throws IOException {
    
	    	if(create_name.getText().trim().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Input");
				alert.setContentText("Username string cannot be empty");
				alert.showAndWait();
				return;
	    	} else {
	    		if(appContext.getAlbumStore().containsKey(create_name.getText())) {
	    			System.out.println("albumStore size" + appContext.getAlbumStore().size());
	    			Alert alert = new Alert(AlertType.ERROR);
	    			alert.setTitle("Duplicate Username");
				alert.setContentText("Duplicate username please enter a different username");
				alert.showAndWait();
				return;
	    		} else {
	    			//add user to albumstore map
	    			String new_user = create_name.getText();
	    			appContext.addUser(new_user);
	    			//show new listview
	    			users.add(new_user);
	    			showListView();
	    			
	    		}
	    	}
	    	create_name.setText("");
    }
    /**
     * Deletes an existing user
     * @param e
     * @throws IOException
     */
	public void handleDeleteExistingUser(ActionEvent e) throws IOException {
		AppContext app = appContext.getInstance();
		if(appContext.getAlbumStore().containsKey(delete_name.getText())){
	    app.delete(delete_name.getText());
	    users.remove(delete_name.getText());
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Does not exist");
		alert.setContentText("Username does not exist");
		alert.showAndWait();
		return;
		}
		
	    showListView();
	}
	    
	public void showListView() {
		listView.setItems(users);
		
	    if (users.size() > 0) {
	       listView.getSelectionModel().select(0);
	    }
	}
    
    
}
