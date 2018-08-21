package Controller;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import Model.Album;
import Model.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;


/**
 * Controller for handling the search results from the photo selection view
 * @author Sam Christian and Anna Faytelson
 */
public class SearchResultsController extends MainController implements Initializable {

	@FXML
	Button cancelButton;
	
	@FXML
	Button saveButton;
	
	 @FXML
	    ListView<Label> listView;
	 @FXML
	 TextField newAlbumName;
	 
	 @FXML
	 Label createLabel;
	 
	    private ObservableList<Label> observableList;
	    private List<Album> albumList;
	    private Set<Photo> searchResultList;
	    
	 Map<Integer, Photo> photoListMap = new HashMap<>();
	   /**
	    * displays the photos from the search criteria
	    */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		 observableList = FXCollections.observableArrayList();
		// Get all the albums for this user
		
		albumList = appContext.getAlbumList(appContext.getUserId());
		
		searchResultList = appContext.getSearchResults();
		
		displayPhotos(searchResultList);
		
		if (searchResultList.isEmpty()) {
			saveButton.setDisable(true);
			newAlbumName.setVisible(false);
			createLabel.setVisible(false);
			
			showMessage("Search Results", "No photos found", Alert.AlertType.INFORMATION);
		}
	}
/**
 * displays the photos from the search criteria
 * @param photoSet
 */
	private void displayPhotos(Set<Photo> photoSet) {

		// sort on time field
		
		List<Photo> photoList = photoSet.stream().sorted(Comparator.comparing(Photo::getTime)).collect(Collectors.toList());

		int i = 0;
		photoListMap.clear();
		observableList.clear();
		for (Photo photo : photoList) {
			observableList.add(createPhotoLabel(photo.getName(), photo.getLocation()));
			photoListMap.put(i, photo);
			i++;
		}
		listView.setItems(observableList);
	}
    /**
     * creates the new album name
     * @param name
     * @param path
     * @return
     */
    private Label createPhotoLabel(String name, String path) {
    	Image image = new Image(path);

        ImageView imageView = new ImageView();

        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);


        Label label = new Label(name);
        label.setGraphic(imageView);
        
        return label;
    }
	/**
	 * cancels the search 
	 * @param event
	 */
	@FXML
	    public void doCancel(ActionEvent event) {
	        System.out.println("Cancel was pressed");       
	       
			try {
				changeScene("/view/SearchCriteria.fxml");
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 /**
	  * saves the album of searched photos 
	  * @param event
	  */
	  @FXML
	    public void doSave(ActionEvent event) {
	        
		  String newName = newAlbumName.getText();
		  if (newName.length() == 0) {
			  showMessage("Invalid Input", "Please enter a new album name", Alert.AlertType.ERROR);
		  }
		  else if (albumList.stream().filter(a -> a.getName().equalsIgnoreCase(newName)).findAny().isPresent()) {
			  showMessage("Album Exists", "Album name: " + newName + " already exists", Alert.AlertType.ERROR);
		  }
		  else {
			  Album album = new Album(searchResultList, newName);		// create new album
			  appContext.getAlbumList(appContext.getUserId()).add(album);		// add to user's album list
			  try {
					changeScene("/view/Albums.fxml");
				} catch (IOException e) {
					e.printStackTrace();
				}
		  }
			  
		  
	       
	    }
	  
	  
}