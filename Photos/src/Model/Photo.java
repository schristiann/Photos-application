package Model;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import Controller.AppContext;


/**
 * Class for storing a serialized photo instance including location of photo and what album the photo is in
 * @author Sam Christian and Anna Faytelson
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = -8346857833613929648L;
    private String name;
    public Set<String>  photoTags=new HashSet<>();
    private String photoId;
    
    public String getPhotoId() {
		return photoId;
	}

	public void addPhotoTag(String name, String value) {
    	photoTags.add(name+":"+value);
    }

    public Set<String> getPhotoTags() {
		return photoTags;
	}

	public void setPhotoTags(Set<String> photoTags) {
		this.photoTags = photoTags;
	}
	
	public void deletePhotoTag(String name, String value) {
		String keyValue=name+":"+value;
		photoTags.remove(keyValue);
	}

	public Photo(String name, LocalDateTime time, String location) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.photoId = UUID.randomUUID().toString();
    }

    @Override
	public String toString() {
		return "Photo [name=" + name + ", photoTags=" + photoTags + ", photoId=" + photoId + ", time=" + time
				+ ", location=" + location + "]";
	}

	public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(name, photo.name) &&
                Objects.equals(time, photo.time) &&
                Objects.equals(location, photo.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, time, location);
    }

    public void setName(String name) {
        this.name = name;

    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private LocalDateTime time;
    private String location;

}
