package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import Model.Album;
import Model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Super class for handling creation of albums, reading albums, serialization of data, and creating users
 * @author Anna Faytelson and Sam Christian
 */
public class MainController {

protected AppContext appContext = AppContext.getInstance();
    protected String userId;

    protected   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    /**
     * changes the scene, going to the selected view
     * @param fxml
     * @throws IOException
     */
    protected void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(
                getClass().getResource(fxml));

        appContext.getStage().getScene().setRoot(pane);
    }
    /**
     * stores the map to  be serialized
     */
   public static void storeMap() {
	   storeAlbums(AppContext.getInstance().albumStore, AppContext.getInstance().tagTypes);
   }
    /**
     * reads the album store from file if it exists, and loads the stock options if it does not exist
     * @return
     */
    protected Map<String, List<Album>> readAlbumStore() {

        Map<String, List<Album>> albumStore = null;
        
        Set<String>  tagTypes= null;

        
        try (FileInputStream fileIn = new FileInputStream("./album.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            albumStore = (Map<String, List<Album>>) in.readObject();
            

        } catch (FileNotFoundException e) {
        	System.out.println("Album store does not exist");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (albumStore == null) {
          	
            albumStore = new HashMap<>();
        }
        if ( ! albumStore.containsKey("stock") || albumStore.get("stock").size() == 0) {
        		createStockUser(albumStore);
        }
        
        try (FileInputStream fileIn = new FileInputStream("./tags.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
               tagTypes = (Set<String>) in.readObject();
               appContext.setAllTagTypes(tagTypes);
               

           } catch (FileNotFoundException e) {
           	System.out.println("Album store does not exist");
           } catch (IOException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }

           if (tagTypes == null) {
        	   		tagTypes=new HashSet<>();
             	appContext.setTagTypes();
               
           }
        

        return albumStore;
    }
    
    /**
     * creates the stock user 
     * @param albumStore
     */
	private void createStockUser(Map<String, List<Album>> albumStore) {

		List<Album> albums = new ArrayList<>();
		albumStore.put("stock", albums);
		Album stockAlbum = createAlbum("stock", "stock");
		addPhoto(stockAlbum, new Photo("Stock1", LocalDateTime.of(2015,2,15,9,10,20),"file://"+new File("./stock1.png").getAbsolutePath()));
	      addPhoto(stockAlbum, new Photo("Stock2", LocalDateTime.of(2016,9,20,8,10,30),"file://"+new File("./stock2.png").getAbsolutePath()));
	      addPhoto(stockAlbum, new Photo("Stock3", LocalDateTime.of(2016,9,20,8,10,30),"file://"+new File("./stock3.png").getAbsolutePath()));
	      addPhoto(stockAlbum, new Photo("Stock4", LocalDateTime.of(2016,9,20,8,10,30),"file://"+new File("./stock4.png").getAbsolutePath()));
	      addPhoto(stockAlbum, new Photo("Stock5", LocalDateTime.of(2016,9,20,8,10,30),"file://"+new File("./stock5.png").getAbsolutePath()));
		albums.add(stockAlbum);
	}
/**
 * creates albums
 * @return
 */
	protected Map<String, List<Album>> createAlbums() {

        Map<String, List<Album>> albumStore = new HashMap<>();
        
        

        List<Album> albums = new ArrayList<>();
        Album petAlbum = createAlbum("Pet Photos", "user1");

//        addPhoto(petAlbum, new Photo("golden", LocalDateTime.of(2015,2,15,9,10,20),"file:///Users/andrewchristian/Downloads/golden.png"));
//        addPhoto(petAlbum, new Photo("shepard", LocalDateTime.of(2016,9,20,8,10,30),"file:///Users/andrewchristian/Downloads/shepard.png"));

        albums.add(petAlbum);

//        Album teamAlbum = createAlbum("Team Photos", "user1");
//        
//        addPhoto(teamAlbum, new Photo("goal1", LocalDateTime.of(2014,5,5, 2,45,00),"/photos/goal1.png"));
//        addPhoto(teamAlbum, new Photo("goal2", LocalDateTime.of(2014,5,5,12,20,50),"/photos/goal2.png"));
//
//       albums.add(teamAlbum);
        albumStore.put("user1", albums);

        albums = new ArrayList<>();
        Album vacationAlbum = createAlbum("Vacation Photos", "user2");

   //     addPhoto(vacationAlbum, new Photo("Disney - Tower of Terror", LocalDateTime.of(2017,11,7,10,45,0),"/vacationpics/disney1.png"));
    //    addPhoto(vacationAlbum, new Photo("Grand Canyon - Day 1", LocalDateTime.of(2017,6,14,15,15,0),"/vacationpics/grandCanyon1.png"));

        albums.add(vacationAlbum);

        albumStore.put("user2", albums);

        return albumStore;

    }
    /**
     * creates albums for a user given a user and album name
     * @param albumName
     * @param userName
     * @return
     */
    protected Album createAlbum(String albumName, String userName) {
        Album album = new Album();

        album.setName(albumName);
        album.setUserId(userName);

       
        
        return album;

    }
    /**
     * serializes the albums and tags to be later read
     * @param albumStore
     * @param tagTypes
     */
    protected static void storeAlbums(Map<String, List<Album>>  albumStore, Set<String> tagTypes) {

        try (
                FileOutputStream fileOut =
                    new FileOutputStream("./album.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut) ) {

                 out.writeObject(albumStore);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (
                FileOutputStream fileOut =
                    new FileOutputStream("./tags.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut) ) {

                 out.writeObject(tagTypes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * adds a photo to an album
     * @param album
     * @param photo
     */
    private void addPhoto(Album album, Photo photo) {

        album.addPhoto(photo);
    }
    /**
     * quits out of the application
     * @param event
     */
    @FXML
    protected void doQuit(ActionEvent event) {
    	
    		
        System.out.println("Quit was pressed");
//        updateGrid.getChildren().clear();
        
        Platform.exit();
    }
/**
 * logs out of the user
 * @param event
 */
	@FXML
	protected void doLogout(ActionEvent event) {

		System.out.println("Logout was pressed");
		// updateGrid.getChildren().clear();

		appContext.setCurrentAlbum(null);
		appContext.setUserId(null);
		storeAlbums(appContext.albumStore, appContext.tagTypes);
		try {
			changeScene("/View/login.fxml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * throws error messages on incorrect input
	 * @param title
	 * @param msg
	 * @param type
	 */
	protected void showMessage(String title, String msg, Alert.AlertType type) {
		  Alert alert = new Alert(type);
			alert.setTitle(title);
			alert.setHeaderText("");
			alert.setContentText(msg);
			 alert.showAndWait();
	  }
}