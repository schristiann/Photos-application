package Controller;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import Model.Album;
import Model.Photo;


/**
 * Class stores the main instance of the users and their respective albums with photos
 * @author Anna Faytelson and Sam Christian 
 */
public class AppContext {
    private final static AppContext instance = new AppContext();
    
    private Set<Photo> searchResults;
    
	Map<String, List<Album>> albumStore;
	
    Set<String>  tagTypes=new HashSet<>();
    /**
     * sets default tag types 
     * @param types
     */
    public void setAllTagTypes(Set<String> types) {
    	tagTypes=types;
    }
    /**
     * adds tag types to list on user input
     * @param type
     */
   public void addTagTypes(String type) {
	   tagTypes.add(type);
   }
    
    enum COPY_MOVE_ACTIONS {COPY, MOVE};
	
	private COPY_MOVE_ACTIONS requestAction;
	/**
	 * returns copy move action
	 * @return
	 */
    public COPY_MOVE_ACTIONS getRequestAction() {
		return requestAction;
	}
/**
 * sets the copy move action
 * @param requestAction
 */
	public void setRequestAction(COPY_MOVE_ACTIONS requestAction) {
		this.requestAction = requestAction;
	}
    /**
     * gets the tag types for adding tags
     * @return
     */
    public Set<String> getTagTypes() {
		return tagTypes;
	}
/**
 * sets the tag types of a photo
 */
	public void setTagTypes() {
		tagTypes.add("Person");
		tagTypes.add("Location");
	}

	Album currentAlbum;
    
    Photo currentPhoto;
    /**
     * returns the current photo for tag adding
     * @return
     */
    public Photo getCurrentPhoto() {
		return currentPhoto;
	}
/**
 * sets the current photo for tag adding
 * @param currentPhoto
 */
	public void setCurrentPhoto(Photo currentPhoto) {
		this.currentPhoto = currentPhoto;
	}
/**
 * gets the current album 
 * @return
 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}
/**
 * sets the current album
 * @param currentAlbum
 */
	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}

	List<Album> albumList;
    
    Set<Photo> photoList;

    private Stage stage;
    /**
     * sets the map to eventually be serialized
     * @param albumList
     */
    public void setMap(Map<String, List<Album>> albumList) {
    	albumStore=albumList;
    }
    /**
     * returns the instance of appContext for storing changes
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }
    
    /**
     * gets the list of photos for an album
     * @return
     */
    public Set<Photo> getPhotoList() {
    	return photoList;
    }
    /**
     * gets the album list for a user
     * @param userName
     * @return
     */
    public List<Album> getAlbumList(String userName) {
    	return albumStore.get(userName);
    }

    private String userId;

/**
 * returns the user id to specify the specific album store
 * @return
 */
    public String getUserId() {
        return userId;
    }
/**
 * sets the user id on login
 * @param userId
 */
    public void setUserId(String userId) {
        this.userId = userId;
    }
/**
 * gets the current stage 
 * @return
 */
    public Stage getStage() {
        return stage;
    }
/**
 * sets the stage 
 * @param stage
 */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * add a user from the admin view
     * @param user
     */
    public void addUser(String user) {
    
    	
    	albumStore.put(user, new ArrayList<>());
    }
/**
 * returns the search results of photos
 * @return
 */
	public Set<Photo> getSearchResults() {
		return searchResults;
	}
/**
 * sets the search results from the user input
 * @param searchResults
 */
	public void setSearchResults(Set<Photo> searchResults) {
		this.searchResults = searchResults;
	}
    /**
     * returns the list of users for admin view list
     * @return
     */
	 public ObservableList<String> getUsers() {
	    	ObservableList<String> users = FXCollections.observableArrayList();
		    //iterate through all users
		    for (Map.Entry<String,List<Album>> userid : this.albumStore.entrySet()) {
		       users.add(userid.getKey());
		    }
	    		return users;
	    }
	    /**
	     * returns the album store for all users
	     * @return
	     */
	    public Map<String, List<Album>> getAlbumStore(){
			return albumStore;
	    }
	    /**
	     * removes a user from the album store
	     * @param user
	     */
	    public void delete(String user) {
	   		albumStore.remove(user);
	    }
   
    
}