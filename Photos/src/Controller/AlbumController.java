package Controller;


import Model.Album;

import Model.Photo;

import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * This class controls the albums for each user that is entered through the login page
 * @author Anna Faytelson and Sam Christian
 */
public class AlbumController extends MainController implements Initializable{

    @FXML
    TextField albumName;

    @FXML
    Label photoCount;

    @FXML
    Label dateRange;
    
    List<Album> albumList;
    
    @FXML
    ListView<String> listView;
    
    @FXML
    Button cancelButton;
    
    @FXML
    Button saveButton;
    
    @FXML
    Button openButton;
    
    @FXML
    Button deleteButton;
    
    @FXML
    Button searchButton;
    
    @FXML
    Text albumNameText;
    
    @FXML
    Text numPhotosText;
    
    @FXML
    Text dateRangeText;
    
    private ObservableList<String> observableList;

    
/**
 * initializes the album controller for a specific user's album set
 */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        userId = appContext.getUserId();
        albumList=appContext.getAlbumList(userId);
        System.out.println("Hello: " + userId);

        observableList = FXCollections.observableArrayList();
   
        displayAlbums();
       setButtonsDisable(true);
        
        listView.setItems(observableList);
    }
/**
 * displays the albums in listview
 */
	private void displayAlbums() {
		observableList.clear();
		for (Album album : albumList) {
			String albumName = album.getName();
			System.out.println(albumName);
			observableList.add(albumName);
			showFields(true);
			
		}
	}
    /**
     * disables all buttons, taking true or false to set
     * @param mode
     */
	private void setButtonsDisable(boolean mode) {
		cancelButton.setDisable(mode);
		saveButton.setDisable(mode);
		openButton.setDisable(mode);
		deleteButton.setDisable(mode);
	}
/**
 * Cancels the action and clears the current selected album
 * @param event
 */
    @FXML
    public void doCancel(ActionEvent event) {
        System.out.println("Cancel was pressed");
        showFields(true);
        albumName.clear();
        clearFields();
        setButtonsDisable(true);

    }
/**
 * Saves a new empty album store, alerting the user if the user already exists
 * @param event
 */
    @FXML
	public void doSave(ActionEvent event) {
		System.out.println("Save was pressed");
		if(albumName.getText().trim().length()==0) {
			showMessage("invalid input", "Please enter an album name", Alert.AlertType.ERROR);
			return;
		}
		boolean dupAlbum = false;
		for (Album album : albumList) {
			if (album.getName().equalsIgnoreCase(albumName.getText())) {
				dupAlbum = true;
			}
		}

		if (dupAlbum) {
			showMessage("Album Exists", "Album " + albumName.getText() + " already exists", Alert.AlertType.ERROR);
		} else {
			if (appContext.currentAlbum == null) { // new album
				Album newAlbum = createAlbum(albumName.getText(), appContext.getUserId());
				albumList.add(newAlbum);
				appContext.currentAlbum = newAlbum;
				displayAlbumDetails(albumName.getText());
				setButtonsDisable(false);
			} else {
				appContext.currentAlbum.setName(albumName.getText());
			}
			displayAlbums();
		}

	}
/**
 * sets the text field to enter a title for a new album add
 * @param event
 */
    @FXML
    public void doAdd(ActionEvent event) {
    	    		
        System.out.println("Add was pressed");
        openButton.setDisable(true);
        deleteButton.setDisable(true);
        saveButton.setDisable(false);
        cancelButton.setDisable(false);
        showFields(false);
        
        albumName.clear();
        albumNameText.setText("New Album Name");

        appContext.currentAlbum = null;
    }
    /**
     * sets the fields to empty, taking a boolean
     * @param show
     */
    private void showFields(boolean show) {
    	 	photoCount.setVisible(show);
         numPhotosText.setVisible(show);
         dateRange.setVisible(show);
         dateRangeText.setVisible(show);
         
         if (show) {
        	 	albumNameText.setText("Album Name");
         }
    }
    /**
     * deletes an album
     * @param event
     */
    @FXML
    public void doDelete(ActionEvent event) {
    	    		
        System.out.println("Delete was pressed");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("");
		alert.setContentText("Are you sure you want to delete " + appContext.currentAlbum.getName() + " ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			
			albumList.remove(appContext.currentAlbum);
			appContext.currentAlbum = null;
			clearFields();
			 displayAlbums();
			 setButtonsDisable(true);
		}
    }
/**
 * clears the details of the album
 */
    private void clearFields() {
    		albumName.clear();
    		dateRange.setText("");
    		photoCount.setText("");
    }
    /**
     * opens an album and goes to the photo view displaying the photos
     * @param event
     */
    @FXML
    public void doOpen(ActionEvent event) {
        System.out.println("Open was pressed");

        try {
        		Album album=getAlbum(listView.getSelectionModel().getSelectedItem());
        		appContext.setCurrentAlbum(album);
            changeScene("/View/Photos.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * search for photos contained in user albums, taking the user to the search page
     * @param event
     */
    @FXML
    public void doSearch(ActionEvent event) {
        System.out.println("Search was pressed");

        try {
        		Album album=getAlbum(listView.getSelectionModel().getSelectedItem());
        		appContext.setCurrentAlbum(album);
            changeScene("/View/SearchCriteria.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML public void handleKeyPress(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            System.out.println("Arrow key pressed");
            System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
            String albumName = listView.getSelectionModel().getSelectedItem();
            displayAlbumDetails(albumName);
            setButtonsDisable(false);
            showFields(true);
        }
    }

    @FXML public void handleKeyRelease(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            System.out.println("Arrow key pressed");
            System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
            String albumName = listView.getSelectionModel().getSelectedItem();
            displayAlbumDetails(albumName);
            setButtonsDisable(false);
            showFields(true);
        }
    }
    @FXML public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
        String albumName = listView.getSelectionModel().getSelectedItem();
        displayAlbumDetails(albumName);
        setButtonsDisable(false);
        showFields(true);
    }
/**
 * displays the details of the selected album
 * @param albumName
 */
    private void displayAlbumDetails(String albumName) {
    	
        photoCount.setVisible(true);
        dateRange.setVisible(true);
        this.albumName.setText(albumName);
        Album album = getAlbum(albumName);
        appContext.currentAlbum = album;
        photoCount.setText(String.valueOf(album.getPhotos().size()));
        
        LocalDateTime minDate = LocalDateTime.MAX;
        LocalDateTime maxDate = LocalDateTime.MIN;
        
        Set<Photo> photoList = album.getPhotos();
        
        for (Photo photo : photoList) {
        	if (photo.getTime().isBefore(minDate)) {
        		minDate = photo.getTime();
        	}
        	if (photo.getTime().isAfter(maxDate)) {
        		maxDate = photo.getTime();
        	}
        }
        
		if (!photoList.isEmpty()) {
			dateRange.setText(minDate.format(formatter) + " - " + maxDate.format(formatter));
		} else {
			dateRange.setText("");
		}

    }
    /**
     * returns the position in the listView an album
     * @param albumName
     * @return
     */
    private int getPositionInList(String albumName) {

        for (int i =0; i < observableList.size(); i++) {
            if (observableList.get(i).equals(albumName))
                return i;
        }
        return 0;
    }
    private Album getAlbum(String albumName) {
    	for(Album album:albumList) {
    		if(album.getName().equals(albumName)) {
    			return album;
    		}
    	}
		return null;
    }
}