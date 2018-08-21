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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Controller.AppContext.COPY_MOVE_ACTIONS;

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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import Model.Album;
import Model.Photo;
import Controller.AppContext.COPY_MOVE_ACTIONS;


/**
 * Controller that handles photo selection view buttons and controls 
 * @author Sam Christian and Anna Faytelson
 */
public class PhotosController extends MainController implements Initializable{

    @FXML TextField caption;
    @FXML Label date;
    @FXML Label tags;

    
    @FXML Button addButton;
    @FXML Button backButton;
    @FXML Button deleteButton;
    @FXML Button saveButton;
    @FXML Button addTagsButton;
    @FXML Button copyButton;
    @FXML Button moveButton;
    @FXML Button prevButton;
    @FXML Button nextButton;
    
    @FXML ImageView photoImage;

    @FXML
    GridPane updateGrid;

       @FXML
    ListView<Label> listView;
    private ObservableList<Label> observableList;
    
    Set<Photo> photoSet;
    Map<Integer, Photo> photoListMap = new HashMap<>();
    
    Label currentLabel;
  
    int currentPosition = 0;
/**
 * initializes the photo view
 */
    @FXML
	public void initialize(URL location, ResourceBundle resources) {
    		Album album=appContext.getCurrentAlbum();
    		observableList = FXCollections.observableArrayList();
    		
		 photoSet = album.getPhotos();
		
		 displayPhotos(photoSet);

		 boolean mode = appContext.getAlbumList(appContext.getUserId()).size() == 1;
		 moveButton.setDisable(mode);
		 copyButton.setDisable(mode);
		listView.setItems(observableList);
		displayNextPhoto(0);
		
    		}
	
/**
 * adds a photo given a valid file path
 * @param path
 * @param name
 */
    private void addPhoto(String path, String name) {
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
        observableList.add(label);
    }
/**
 * cancels the photo action
 * @param event
 */
    @FXML
    public void doCancel(ActionEvent event) {
        System.out.println("Cancel was pressed");
//        updateGrid.getChildren().clear();
        
    		Photo photo = appContext.getCurrentPhoto();
    		caption.setText(photo.getName());
    }
/**
 * saves the photo
 * @param event
 */
    @FXML
    public void doSave(ActionEvent event) {
        System.out.println("Cancel was pressed");
//        updateGrid.getChildren().clear();
        	Photo photo = appContext.getCurrentPhoto();
        	if(caption.getText().trim().length()==0) {
    			showMessage("invalid input", "Please enter a caption", Alert.AlertType.ERROR);
    			return;
    		}
        	photo.setName(caption.getText());
        	displayPhotos(photoSet);
        	
        	for(Map.Entry<Integer, Photo> entry: photoListMap.entrySet()){
        		if(entry.getValue().equals(photo)) {
        			displayNextPhoto(entry.getKey());
        		}
        	}
        	

    }
    /**
     * deletes the photo
     * @param event
     */
    @FXML
	public void doDelete(ActionEvent event) {
		System.out.println("Delete was pressed");
		Photo photo = appContext.currentPhoto;

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("");
		alert.setContentText("Are you sure you want to delete " + photo.getName() + " ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.out.println("OK was pressed");
			photoSet.remove(photo);
			appContext.setCurrentPhoto(null);
			displayPhotos(photoSet);
			clearUpdateGrid();
			
			if (currentPosition >= 0 && currentPosition < observableList.size()) {
				displayNextPhoto(currentPosition);
			} else if (currentPosition == observableList.size()) {
				displayNextPhoto(0);
			}
		}

	}
    /**
     * copies the selected photo
     * @param event
     */
    @FXML
    public void doCopy(ActionEvent event) {
        System.out.println("Copy was pressed");       
        	try {
        		appContext.setRequestAction(COPY_MOVE_ACTIONS.COPY);
    			changeScene("/View/CopyMovePhoto.fxml");		
    		} catch (IOException e) {
    			e.printStackTrace();
    		}

    }
    /**
     * changes view to move the selected photo
     * @param event
     */
    @FXML
    public void doMove(ActionEvent event) {
        System.out.println("move was pressed");       
        	try {
        		appContext.setRequestAction(COPY_MOVE_ACTIONS.MOVE);
    			changeScene("/View/CopyMovePhoto.fxml");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}

    }
    /**
     * changes the slide view to the previous photo
     * @param event
     */
    @FXML
    public void doPrev(ActionEvent event) {
        System.out.println("Prev was pressed");
        displayNextPhoto(currentPosition -1);
        
        listView.getSelectionModel().select(currentPosition);
        listView.scrollTo(currentPosition);
        listView.getFocusModel().focus(currentPosition);
        currentLabel=listView.getSelectionModel().getSelectedItem();
    }
    /**
     * changes the slide view to the next photo
     * @param event
     */
    @FXML
    public void doNext(ActionEvent event) {
        System.out.println("Next was pressed");       
        	displayNextPhoto(currentPosition + 1);
        	listView.getSelectionModel().select(currentPosition);
        listView.scrollTo(currentPosition);
        listView.getFocusModel().focus(currentPosition);
        currentLabel=listView.getSelectionModel().getSelectedItem();
    }
/**
 * adds a photo to the album 
 * @param event
 */
    @FXML
    public void doAdd(ActionEvent event) {
    	
    	final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(addButton.getScene().getWindow());
		if (file != null) {
			
			String filePath="file://"+file.getAbsolutePath();
			System.out.println("File path is: " + filePath);
			String photoName = file.getName();
			int pos = photoName.lastIndexOf(".");
			if (pos > 0) {
				photoName = photoName.substring(0, pos);
			}
			LocalDateTime localDate = null;
			try {
				BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				long mtime = attr.lastModifiedTime().toMillis();
				localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(mtime), ZoneId.systemDefault());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 Photo photo=new Photo(photoName, localDate, filePath);
			 if (photoSet.contains(photo)) {
				 Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Photo already exists");
	                alert.setHeaderText("");
	                alert.setContentText("Photo " + photo.getName() + " already exists");
	                alert.showAndWait();
			 } else {
				photoSet.add(photo);
				displayPhotos(photoSet);
				
				for (Map.Entry<Integer,Photo> entry : photoListMap.entrySet()) {
					if (entry.getValue().equals(photo)) {
						displayNextPhoto(entry.getKey());
					}
				}
				
			 }
		}
        System.out.println("Cancel was pressed");
//        updateGrid.getChildren().clear();


    }
    /**
     * displays the photos in an album
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
	}
    /**
     * creates the label of a photo
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

    @FXML public void handleKeyPress(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            System.out.println("Arrow key pressed");
            int index = listView.getSelectionModel().getSelectedIndex();
            
            System.out.println("Selected item: " + index);
            Photo selectedPhoto = photoListMap.get(index);
//            displayPhoto(selectedPhoto);
            displayNextPhoto(index);
            currentLabel=listView.getSelectionModel().getSelectedItem();
        }
    }

    @FXML public void handleKeyRelease(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            System.out.println("Arrow key pressed");
            int index = listView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected item: " + index);
            Photo selectedPhoto = photoListMap.get(index);
//            displayPhoto(selectedPhoto);
            displayNextPhoto(index);
            currentLabel=listView.getSelectionModel().getSelectedItem();
        }
    }
    @FXML public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
//        Label label = listView.getSelectionModel().getSelectedItem();
//        ImageView imageView = (ImageView) label.getGraphic();
//        displayImage(imageView.getImage(), label.getText());
        
        int index = listView.getSelectionModel().getSelectedIndex();
        System.out.println("Selected item: " + index);
        Photo selectedPhoto = photoListMap.get(index);
//        displayPhoto(selectedPhoto);
        displayNextPhoto(index);
        currentLabel=listView.getSelectionModel().getSelectedItem();

    }
/**
 * displays the photo in the larger view
 * @param photo
 */
    private void displayPhoto(Photo photo) {
    	 	caption.setText(photo.getName());
         date.setText(photo.getTime().format(formatter));
         tags.setText(photo.getPhotoTags().toString());

//         String path;
//         if ("golden".equalsIgnoreCase(imageName))
//             path = "file:///Users/andrewchristian/Downloads/golden.png";
//         else
//             path = "file:///Users/andrewchristian/Downloads/shepard.png";
 //
         Image image = new Image(photo.getLocation());

         photoImage.setImage(image);
         photoImage.setImage(image);
         photoImage.setFitWidth(200);
         photoImage.setPreserveRatio(true);
         photoImage.setSmooth(true);
         photoImage.setCache(true);
         appContext.setCurrentPhoto(photo);
         
    }
    
    private void displayNextPhoto(int pos) {
		currentPosition = pos;
		if (observableList.size() > 0) {
			Photo photo = photoListMap.get(pos);
			saveButton.setDisable(false);
			deleteButton.setDisable(false);
			addTagsButton.setDisable(false);
			displayPhoto(photo);
			currentLabel=observableList.get(pos);
		}
		else {

			saveButton.setDisable(true);
			deleteButton.setDisable(true);
			addTagsButton.setDisable(true);
		}
		// disable copy/move if this the only album or there are currently no photos in this album
		 boolean mode = appContext.getAlbumList(appContext.getUserId()).size() == 1 || observableList.size() == 0;
		 moveButton.setDisable(mode);
		 copyButton.setDisable(mode);
		 
		prevButton.setDisable(currentPosition == 0);
	nextButton.setDisable(currentPosition >= observableList.size()-1);
}
    
    private void displayImage(Image image, String imageName) {
//        updateGrid.getChildren().clear();
        caption.setText(imageName);
        date.setText("2018-04-03");
        tags.setText("foo,bar");

//        String path;
//        if ("golden".equalsIgnoreCase(imageName))
//            path = "file:///Users/andrewchristian/Downloads/golden.png";
//        else
//            path = "file:///Users/andrewchristian/Downloads/shepard.png";
//
//        Image image = new Image(path);

        photoImage.setImage(image);
        photoImage.setImage(image);
        photoImage.setFitWidth(200);
        photoImage.setPreserveRatio(true);
        photoImage.setSmooth(true);
        photoImage.setCache(true);
    }
    
    public void clearUpdateGrid() {
    		caption.clear();
        date.setText("");
        tags.setText("");
        photoImage.setImage(null);
    }

    @FXML
    public void addTags(ActionEvent event) {

    		String photoName=currentLabel.getText();
    	
    		Photo photo= getPhoto(photoName);
    		appContext.setCurrentPhoto(photo);
    		try {
    		changeScene("/view/addTag.fxml");
    		}
    		catch (IOException e) {
                e.printStackTrace();
            }
    }

	@FXML
	public void doBack(ActionEvent event) {
		appContext.setCurrentPhoto(null);
		try {
			changeScene("/view/Albums.fxml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    private Photo getPhoto(String name) {
    	for(Photo photo:photoSet) {
    		if(photo.getName().equals(name)) {
    			return photo;
    		}
    	}
		return null;
    }
    
}