package Model;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import javafx.scene.control.Label;


/**
 * Class for storing a serialized album set includes methods for accessing the name of, user, and photos in a specified album
 * @author Sam Christian and Anna Faytelson
 */
public class Album implements Serializable {
	 private static final long serialVersionUID = -4673694665117315106L;
    private Set<Photo> photos;
    private String name;
    private LocalDateTime earliest;
    private LocalDateTime latest;

    private Set<String> userTags;
    
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public Album() {
    		userTags=new HashSet<>();
        photos = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Album{" +
                "photos=" + photos +
                ", name='" + name + '\'' +
                ", earliest=" + earliest +
                ", latest=" + latest +
                ", userId='" + userId + '\'' +
                '}';
    }

    public Album(Set<Photo> photos, String name) {
        this.photos = photos;
        this.name = name;
    }

    public int photoCount() {
        return photos.size();
    }

    public boolean addPhoto(Photo photo) {
        // add some logic to set earliest and latest date
        return photos.add(photo);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(photos, album.photos) &&
                Objects.equals(name, album.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(photos, name);
    }

    public Set<Photo> getPhotos() {


        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
