package Controller;

import javafx.collections.FXCollections;



import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import Controller.AppContext.COPY_MOVE_ACTIONS;
import Model.Album;
import Model.Photo;


/**
 * Class that controls the movement from albums to photo selection view
 * @author Anna Faytelson and Sam Christian 
 */

public class CopyMovePhotoController extends MainController implements Initializable{

    @FXML TextField caption;
    @FXML Label date;
    @FXML Label tags;
    @FXML Label copyToAlbum;
    @FXML Text actionText;
    
    @FXML Button actionButton;
    @FXML Button cancelButton;
    
    @FXML ImageView photoImage;

    @FXML
    GridPane updateGrid;

       @FXML
    ListView<String> listView;
    private ObservableList<String> observableList;
    private String currentKey;  
    int currentPosition = 0;
/**
 * initializes the view for copying a photo to another album
 */
    @FXML
	public void initialize(URL location, ResourceBundle resources) {
    		Album currentAlbum=appContext.getCurrentAlbum();
    		observableList = FXCollections.observableArrayList();
    		
    		// Get the current user's albums
		String currentUserId = appContext.getUserId();
		
		 
		 List<Album> userAblums = appContext.albumStore.get(currentUserId);
		 for (Album userAlbum : userAblums) {
			 if (! userAlbum.equals(currentAlbum)) {

					observableList.add(userAlbum.getName());
			 }		 
		 }
		if (appContext.getRequestAction().equals(COPY_MOVE_ACTIONS.COPY)) {
			actionButton.setText("Copy");
			actionText.setText("Copy to album");
		} else {
			actionButton.setText("Move");
			actionText.setText("Move to album");
		}
		actionButton.setDisable(true);
		 
		listView.setItems(observableList);
		displayPhoto(appContext.getCurrentPhoto());
    		}
	
/**
 * cancels the move action, returns to photo view
 * @param event
 */
    @FXML
    public void doCancel(ActionEvent event) {
        System.out.println("Cancel was pressed");
        try {
			changeScene("/View/Photos.fxml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
/**
 * stores the photo to be copied or moved to the respective album
 * @param event
 */
    @FXML
    public void doAction(ActionEvent event) {
        System.out.println(actionButton.getText() + " was pressed");
    		
    		String albumName = currentKey;
    		
    		for (Album album :   appContext.albumStore.get(appContext.getUserId()) ) {
    			if (album.getName().equalsIgnoreCase(albumName)) {
    				Photo currentPhoto = appContext.currentPhoto;
    				Photo photo = new Photo(currentPhoto.getName(),currentPhoto.getTime(), currentPhoto.getLocation());
    				photo.setPhotoTags(currentPhoto.getPhotoTags());
    				album.addPhoto(photo);
    				
    				if (appContext.getRequestAction().equals(COPY_MOVE_ACTIONS.MOVE)) {
    					appContext.currentAlbum.getPhotos().remove(currentPhoto);
    				}
    				break;
    			}
    		}
    		try {
    			changeScene("/View/Photos.fxml");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    }
    
    @FXML public void handleKeyPress(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            System.out.println("Arrow key pressed");
            currentKey = listView.getSelectionModel().getSelectedItem();
            System.out.println("Album key: " + currentKey);
            copyToAlbum.setText(currentKey);
            actionButton.setDisable(false);
        }
    }

    @FXML public void handleKeyRelease(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            System.out.println("Arrow key pressed");
            currentKey = listView.getSelectionModel().getSelectedItem();
           System.out.println("Album key: " + currentKey);
           copyToAlbum.setText(currentKey);
           actionButton.setDisable(false);
        }
    }
    @FXML public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
        System.out.println("Arrow key pressed");
        currentKey = listView.getSelectionModel().getSelectedItem();
        System.out.println("Album key: " + currentKey);
        copyToAlbum.setText(currentKey);
        actionButton.setDisable(false);
    }
/**
 * display the details of the selected photo
 * @param photo
 */
    private void displayPhoto(Photo photo) {
    	 	caption.setText(photo.getName());
         date.setText(photo.getTime().format(formatter));
         tags.setText(photo.getPhotoTags().toString());

         Image image = new Image(photo.getLocation());

         photoImage.setImage(image);
         photoImage.setImage(image);
         photoImage.setFitWidth(200);
         photoImage.setPreserveRatio(true);
         photoImage.setSmooth(true);
         photoImage.setCache(true);
         appContext.setCurrentPhoto(photo);
    }
    
       
    public void clearUpdateGrid() {
    		caption.clear();
        date.setText("");
        tags.setText("");
        photoImage.setImage(null);
    }

   
	    
}