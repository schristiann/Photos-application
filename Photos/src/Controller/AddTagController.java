package Controller;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import Model.Photo;

/**
 * This class controls the tags for each selected photo
 * @author Anna Faytelson and Sam Christian
 */
public class AddTagController extends MainController implements Initializable {

	@FXML
	TextField Value;
	
	@FXML
	TextField tagType;
	
	@FXML
	Label currentTags;
	
	Photo selectedPhoto;
	
	@FXML
	ListView<String> listView;
	
	private ObservableList<String> observableList;
	
	/**
	 * Initializes the window for adding a tag
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedPhoto=appContext.getCurrentPhoto();
		Set<String> tagTypes=appContext.getTagTypes();
		observableList = FXCollections.observableArrayList();
		for (String type : tagTypes) {
			observableList.add(type);
			listView.setItems(observableList);
		}
		currentTags.setText(selectedPhoto.getPhotoTags().toString());
	}
	
	/**
	 * Adds a tag to the photo, and alerts the user if either the type or value is null, or the tag already exists
	 * @param e: Action Event
	 */
	
	@FXML
	public void addTag(ActionEvent e) {
		
		String tagType=listView.getSelectionModel().getSelectedItem();
		String tagValue=Value.getText();
		String tagCombo=tagType+":"+tagValue;
		if(tagType==null||tagValue.trim().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tag type/value error");
            alert.setHeaderText("");
            alert.setContentText("Both tag type and tag value must be specified");
            alert.showAndWait();
		}
		if(selectedPhoto.photoTags.contains(tagCombo)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The tag already exists");
            alert.setHeaderText("");
            alert.setContentText("Tag "+tagCombo+" Already exists");
            alert.showAndWait();
		}
		else {
			selectedPhoto.addPhotoTag(tagType, tagValue);
		}
		currentTags.setText(selectedPhoto.getPhotoTags().toString());
	}
	/**
	 * Deletes a tag from the photo, and alerts the user if a field is empty or the tag does not exist already
	 * @param e: Action Event
	 */
	@FXML
	public void deleteTag(ActionEvent e) {
		String tagType=listView.getSelectionModel().getSelectedItem();
		String tagValue=Value.getText();
		String tagCombo=tagType+":"+tagValue;
		if(!selectedPhoto.photoTags.contains(tagCombo)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The tag does not currently exist");
            alert.setHeaderText("");
            alert.setContentText("Tag "+tagCombo+" does not exist");
            alert.showAndWait();
		}
		else {
			selectedPhoto.deletePhotoTag(tagType, tagValue);
		}
		currentTags.setText(selectedPhoto.getPhotoTags().toString());
	}
	
	/**
	 * adds a tag type for the user to use in a tag value pair
	 * @param e: Action Event
	 */
	@FXML 
	public void addTagType(ActionEvent e) {
		if(tagType.getText().trim().length()==0) {
			showMessage("invalid input", "Please enter a tag type", Alert.AlertType.ERROR);
			return;
		}
		String tag=tagType.getText();
		
		if(appContext.tagTypes.contains(tag)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The tag already exists");
            alert.setHeaderText("");
            alert.setContentText("Tag "+tag+" already exists");
            alert.showAndWait();
		}
		else {
			appContext.addTagTypes(tag);
			observableList.add(tag);
		}
	}
	/**
	 *Goes back to photo page
	 */
	@FXML
	public void back(ActionEvent e) {
		try {
		changeScene("/View/Photos.FXML");
		}
		catch (IOException a) {
            a.printStackTrace();
        }
	}
}
