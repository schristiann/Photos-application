package Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Model.Album;
import Model.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 * Controller to handle the search critiera including searching by name, date, and tags
 * If the search is a valid search, the search results fxml will show the results
 * @author Sam Christian and Anna Faytelson
 */
public class SearchCriteriaController extends MainController implements Initializable {

	@FXML
	DatePicker fromDatePicker;
	
	@FXML
	DatePicker toDatePicker;
	
	@FXML
	Text selectedTag;
	
	@FXML
	TextField tagValue;
	
	@FXML
	Button cancelButton;
	
	@FXML
	Button searchButton;
	
	@FXML
	Button addButton;
	@FXML
	ListView<String> tagNameListView;
	@FXML
	ListView<String> tagNameValueListView;
	
	private ObservableList<String> observableList;
	
	private ObservableList<String> selectedList;
	/**
	 * initializes the view for search criteria
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Set<String> tagTypes=appContext.getTagTypes();
		observableList = FXCollections.observableArrayList();
		selectedList = FXCollections.observableArrayList();
		
		if (appContext.getSearchResults() != null) {
			appContext.getSearchResults().clear();
		}
	
		for (String type : tagTypes) {
			observableList.add(type);	
		}
		tagNameListView.setItems(observableList);
		
		tagNameValueListView.setItems(selectedList);
		searchButton.setDisable(true);
		
		 fromDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
			 if (newValue != null) {
	            System.out.println("From date value has change to: " + newValue.toString());
	            searchButton.setDisable(false);
			 }
	        });
		 
		 toDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
			 if (newValue != null) {
	            System.out.println("To date value has change to: " + newValue.toString());
	            searchButton.setDisable(false);
			 }
	        });
	}
	
	 @FXML public void handleKeyPress(KeyEvent event) {
	        if (event.getCode().isArrowKey()) {
	            System.out.println("Arrow key pressed");
	            String tagName = tagNameListView.getSelectionModel().getSelectedItem();
	            
	            System.out.println("Selected item: " + tagName);
	            selectedTag.setText(tagName);
	            addButton.setDisable(false);
	        }
	    }

	    @FXML public void handleKeyRelease(KeyEvent event) {
	        if (event.getCode().isArrowKey()) {
	        	 System.out.println("Arrow key pressed");
		            String tagName = tagNameListView.getSelectionModel().getSelectedItem();
		            
		            System.out.println("Selected item: " + tagName);
		            selectedTag.setText(tagName);
		            addButton.setDisable(false);
	        }
	    }
	    @FXML public void handleMouseClick(MouseEvent arg0) {
	    	 System.out.println("Arrow key pressed");
	            String tagName = tagNameListView.getSelectionModel().getSelectedItem();
	            
	            System.out.println("Selected item: " + tagName);
	            selectedTag.setText(tagName);
	            addButton.setDisable(false);
	    }
	    
	   /**
	    * adds the type value pair to search on
	    * @param event
	    */
	    @FXML
	    public void doAdd(ActionEvent event) {
	        System.out.println("Add was pressed");       
	        selectedList.add(selectedTag.getText() + "=" + tagValue.getText());
	        observableList.remove(selectedTag.getText());
	        selectedTag.setText("");
	        tagValue.clear();
	        addButton.setDisable(true);
	        searchButton.setDisable(false);
	    }
/**
 * cancels the photo search
 * @param event
 */
	    @FXML
	    public void doCancel(ActionEvent event) {
	        System.out.println("Cancel was pressed");       
	     
			try {
				changeScene("/view/Albums.fxml");
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	   /**
	    * resets the values to search on 
	    * @param event
	    */
	    @FXML
	    public void doReset(ActionEvent event) {
	        System.out.println("Reset was pressed");       
	        		tagValue.clear();
				observableList.clear();
				observableList.addAll(appContext.getTagTypes());
				selectedList.clear();
				fromDatePicker.getEditor().clear();
				toDatePicker.getEditor().clear();
			
	    }
	    /**
		 * Searches album based on what user enters a field in the search bar
		 */
	    @FXML
	public void doSearch(ActionEvent event) {
		System.out.println("Search was pressed");
		System.out.println(selectedList);
		LocalDate fromDate = fromDatePicker.getValue();
		LocalDate toDate = toDatePicker.getValue();
		if (fromDate != null) {
			System.out.println(fromDate);
		} else {
			System.out.println("From date is null");
		}

		if (toDate != null) {
			System.out.println(toDate);
		} else {
			System.out.println("To date is null");
		}

		if ((toDate == null && fromDate != null) || (toDate != null && fromDate == null)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText("");
			alert.setContentText("From Date and To Date must both be empty or populated");
			alert.showAndWait();
		} else {
			
			Set<Photo> searchResults = getSearchResults(fromDate, toDate, selectedList);

			if (searchResults.isEmpty()) {
				showMessage("Search Results", "No photos found", Alert.AlertType.INFORMATION);
			} else {
				try {
					appContext.setSearchResults(searchResults);
					changeScene("/View/SearchResults.fxml");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	    /**
	     * returns the set of photos in the search results given dates and tags to search
	     * @param fromDate
	     * @param toDate
	     * @param searchTags
	     * @return
	     */
	    private Set<Photo> getSearchResults(LocalDate fromDate, LocalDate toDate, List<String> searchTags) {
	    		List<Album> albumList = appContext.getAlbumList(appContext.getUserId());
			
			Set<Photo> searchResultList = new HashSet<>();
			
			Map<String, Photo> photoMap = new HashMap<>();
			
			// for each album, select photos that meet the search criteria
			
			for (Album album : albumList) {
				for (Photo photo : album.getPhotos()) {
					
					boolean tagMatch = false;
					boolean dateMatch = false;
					boolean hasTags = searchTags != null && !searchTags.isEmpty();
					boolean hasDates = fromDate!= null && toDate != null;
					
					
					if (! photo.getPhotoTags().isEmpty()) {
						tagMatch = matchTags(photo.getPhotoTags(), searchTags);
					}
					dateMatch =  matchDates(photo.getTime(), fromDate, toDate);
					
					if (hasTags && hasDates) {		// And condition for both date and tags
						if (dateMatch && tagMatch) {
							searchResultList.add(photo);
							photoMap.put(album.getName() +"-" + photo.getName(), photo);
						}
					} else if (hasTags && tagMatch) {
						searchResultList.add(photo);
						photoMap.put(album.getName() +"-" + photo.getName(), photo);
					} else if (hasDates && dateMatch) {
						searchResultList.add(photo);
						photoMap.put(album.getName() +"-" + photo.getName(), photo);
					}
					
					
				}
			}
			return searchResultList;
	    }
	    /**
	     * checks if the dates match a photo
	     * @param time
	     * @param fromDate
	     * @param toDate
	     * @return
	     */
	    private boolean matchDates(LocalDateTime time, LocalDate fromDate, LocalDate toDate) {
			
			  if (fromDate == null || toDate == null) {
				  return false;
			  }
			  LocalDateTime fromDateTime = fromDate.atStartOfDay();
			  LocalDateTime toDateTime =  LocalDateTime.of(toDate, LocalTime.of(23, 59,59));
			  
			  
			return time.isAfter(fromDateTime) && time.isBefore(toDateTime);
		}
/**
 * checks if the tags match a photo
 * @param photoTags
 * @param searchTags
 * @return
 */
		private boolean matchTags(Set<String> photoTags, List<String> searchTags) {
			
			  Map<String, String> photoTagMap = new HashMap<>();
			  Map<String, String> searchTagMap = new HashMap<>();
			
			  for (String tag : photoTags) {
				  String[] nv = tag.split(":");
				  if (nv.length ==2)
					  photoTagMap.put(nv[0], nv[1]);
			  }
			  
			  for (String tag : searchTags) {
				  String[] nv = tag.split("=");
				  if (nv.length == 2)
					  searchTagMap.put(nv[0], nv[1]);
			  }
			  
			  // All tags with same key values must have same values
			  int matchCount = 0;
			  for (String key : searchTagMap.keySet()) {
				  if (photoTagMap.containsKey(key)) {
					  if (searchTagMap.get(key).equals(photoTagMap.get(key))) {
						  matchCount++;
					  }
				  }
			  }
			return matchCount == searchTagMap.size();
		}
}