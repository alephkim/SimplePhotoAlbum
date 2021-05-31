package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**The Album class that keeps a list of images.
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Album implements Serializable {
	
	/**
	 * explicit serialVersionUID for compiler consistency
	 */
	private static final long serialVersionUID = 41687L;
	/**
	 * The Album title
	 */
	private String title;
	/**
	 * The List of images as Pic objects
	 */
	private List<Photo> images;
	
	/**Constructs an album with a specified title
	 * @param title		the album title
	 */
	public Album(String title) {
		this.title = title;
		this.images = new ArrayList<Photo>();
	}
	
	/**Gets the Album title
	 * @return the album title
	 */
	public String getAlbumTitle() {
		if(this.title == null) return "";
		return this.title;
	}
	
	/**Gets the Album size
	 * @return the album's size
	 */
	public int getAlbumSize() {
		try {
			return this.images.size();
		} catch(NullPointerException e) {
			return 0;
		}
	}
	
	public List<Photo> getPhotoList() {
		return this.images;
	}
	
	/**Helper method for getDateRange. Gets the oldest Calendar in the album.
	 * @return	the oldest Pic's Calendar
	 */
	public Calendar getOldestCalendar() {
		if(this.getAlbumSize() == 0) return null;
		
		Photo temp = this.images.get(0);
		for(Photo pic : this.images) {
			if(pic.getCalendar().compareTo(temp.getCalendar()) < 0) temp = pic;
		}
		
		return temp.getCalendar();
	}
	
	/**Helper method for getDateRange. Gets the newest Calendar in the album.
	 * @return	the newest Pic's Calendar
	 */
	public Calendar getNewestCalendar() {
		if(this.getAlbumSize() == 0) return null;
		
		Photo temp = this.images.get(0);
		for(Photo pic : this.images) {
			if(pic.getCalendar().compareTo(temp.getCalendar()) > 0) temp = pic;
		}
		
		return temp.getCalendar();
	}
	
	/**Gets the date range of all pics in the album.
	 * @return	the Album's date range
	 */
	public String getDateRange() {
		if(this.getAlbumSize() == 0) return "";
		return getOldestCalendar().getTime().toString() +" - " +getNewestCalendar().getTime().toString();
	}
	
	/**Sets the album title
	 * @param title		the desired album title
	 */
	public void setAlbumTitle(String title) {
		this.title = title;
	}
	
	/**Adds a photo to the Album
	 * @param photo		the image being added to the album
	 */
	public boolean addPhoto(Photo photo) {
		if(photoExists(photo)) return false;
		this.images.add(photo);
		return true;
	}
	

	/**Removes the photo from the album
	 * @param photo		the photo to be removed
	 * @return	boolean value confirming whether the removal was successful
	 */
	public boolean removePhoto(Photo photo) {
		if(!photoExists(photo)) return false;
		this.images.remove(photo);
		return true;
	}
	
	/**Checks whether the photo exists in the album
	 * @param photo		the photo being checked
	 * @return	boolean confirming whether the photo exists in the album
	 */
	public boolean photoExists(Photo photo) {
		if(this.images == null || this.images.isEmpty()) return false;
		for(Photo temp : this.images) {
			if(temp.equals(photo)) return true;
		}
		return false;
	}
}
