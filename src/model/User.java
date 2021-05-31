package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;


/**The User class that keeps a list of albums.
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class User implements Serializable {
	
	/**
	 * explicit serialVersionUID for compiler consistency
	 */
	private static final long serialVersionUID = 8534L;
	/**
	 * User's username
	 */
	private String username;
	
	
	/**
	 * stores a list of unique tags that this user has entered
	 */
	private List<String> uniqueTagTypes;
	
	
	/**
	 * User's list of albums
	 */
	private List<Album> albums;
	

	/**Constructs a User with a specified username
	 * @param username		the username
	 */
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
		this.uniqueTagTypes = new ArrayList<String>();
	}
	
	/**Gets the User's album list
	 * @return	the album list
	 */
	public List<Album> getAlbumList(){
		return this.albums;
	}
	
	/**Gets the album in the album list
	 * @param title		the album title
	 * @return	the album with the provided album title
	 */
	public Album getAlbum(String title) {
		try {
			for (Album a : this.albums) {
			     if (a.getAlbumTitle().equals(title)) {
			        return a;
			     }
			  }
			 return null; 
		} catch(NullPointerException e){
			return null;
		}
	}
	
	/**Gets the username field
	 * @return the User's username
	 */
	public String getUsername() {
		return username;
	}
	
	/**Sets the username field
	 * @param username the desired username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return username;
	}
	
	/**Adds a new album to the User's album list. Does not add albums with duplicate titles
	 * @param album		the album to be added
	 * @return boolean value confirming whether the album was successfully added
	 */
	public boolean addNewAlbum(Album album) {
		if(albumTitleExists(album.getAlbumTitle())) return false;
		this.albums.add(album);
		return true;
	}
	
	/**Removes the desired album from the album list
	 * @param album		the album to be removed
	 * @return boolean value confirming whether the album was successfully removed
	 */
	public boolean removeAlbum(Album album) {
		if(!albumTitleExists(album.getAlbumTitle())) return false;
		this.albums.remove(album);
		return true;
	}
	
	/**Renames an Album with a new String title at the given index
	 * @param title		the desired new Album title
	 * @param index		the index of the Album
	 * @return boolean value confirming whether the album was successfully renamed
	 */
	public boolean renameAlbum(String title, int index) {
		if(albumTitleExists(title)) return false;
		try {
			this.albums.get(index).setAlbumTitle(title);
			return true;
		} catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**Helper method that checks whether an album title already exists in the list of Albums
	 * @param title		the title being checked
	 * @return boolean value determining whether an album by the given title already exists
	 */
	public boolean albumTitleExists(String title) {
		if(this.albums == null || this.albums.isEmpty()) return false;
		for(Album album : this.albums) {
			if(album.getAlbumTitle().equals(title)) return true;
		}
		return false;
	}
	
	/**Gets the User's unique tag types
	 * @return	list of the User's Tag types as a List<String>
	 */
	public List<String> getUniqueTagTypes(){
		return uniqueTagTypes;
	}
}
