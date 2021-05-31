package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javafx.scene.image.Image;

/**The Pic class that keeps ImageData for serialization
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Photo implements Serializable {
	
	/**
	 * explicit serialVersionUID for compiler consistency
	 */
	private static final long serialVersionUID = 810L;
	/**
	 * The actual image data
	 */
	private PhotoData image;
	/**
	 * The image's caption
	 */
	private String caption;
	/**
	 * The image's tags
	 */
	private List<Tag> tags;
	/**
	 * The image's date
	 */
	private Calendar date;
	
	/**Pic Constructor used to create a serializable Image object.
	 * @param image		the Image object
	 */
	public Photo(Image image) {
		caption = "";
		tags = new ArrayList<Tag>();
		date = Calendar.getInstance();
		date.set(Calendar.MILLISECOND,0);
		this.image = new PhotoData();
		this.image.setPixelsFromImage(image);
	}
	
	/**Gets the Pic's Image object
	 * @return the Pic's Image
	 */
	public Image getImage() {
		return image.getImageFromPixels();
	}
	
	/**Gets the Pic's PhotoData
	 * @return the Pic's PhotoData
	 */
	public PhotoData getPhotoData() {
		return image;
	}
	
	/**Gets the Pic's caption as a String
	 * @return the Pic's caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**Gets the Pic's tag list
	 * @return the tag list
	 */
	public List<Tag> getTagList() {
		return this.tags;
	}
	
	/**Sets the Pic's caption with the input String
	 * @param caption	the input string
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**Gets the Pic's Calendar object
	 * @return the pic's calendar object
	 */
	public Calendar getCalendar() {
		return this.date;
	}
	
	/**returns the Pic's date
	 * @return a string representation of the Pic's date
	 */
	public String getDate() {
		return this.date.getTime().toString();
	}
	
	/**Adds a new non-duplicate tag. Returns false if a duplicate is found.
	 * @param type	the type of the new tag
	 * @param name	the name of the new tag
	 * @return boolean value determining whether the tag was successfully added
	 */
	public boolean addTag(String type, String name) {
		if(tagExists(type,name)) return false;
		tags.add(new Tag(type,name));
		return true;
	}
	
	/**Removes a Tag from the list with the matching type and name
	 * @param type		the type of the tag
	 * @param name		the name of the tag
	 * @return boolean value determining whether the tag was successfully removed
	 */
	public boolean removeTag(String type, String name) {
		if(!tagExists(type,name)) return false;
		for(Tag tag : this.tags) {
			if (tag.getType().equals(type) && tag.getName().equals(name)) {
				this.tags.remove(tag);
				return true;
			}
		}
		return false;
	}
	
	/**Edits a tag's values at a given index with the given string parameters if the tag exists.
	 * @param index		the index of the tag
	 * @param type		the new type of the tag
	 * @param name		the new name of the tag
	 * @return boolean value determining whether the tag was successfully edited
	 */
	public boolean editTag(int index, String type, String name) {
		try {
			tags.get(index).setType(type);
			tags.get(index).setName(name);
			return true;
		} catch(NullPointerException | ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public void sortTags() {
		this.tags.sort(Comparator
				.comparing(Tag::getType)
				.thenComparing(Tag::getName));
	}
	
	/**Helper method to check if a Pic contains a particular tag. Case insensitive.
	 * @param type		the type of the tag
	 * @param name		the name of the tag
	 * @return True if the tag exists. False otherwise.
	 */
	public boolean tagExists(String type, String name) {
		if(this.tags == null || this.tags.isEmpty()) return false;
		for(Tag tag : this.tags) {
			if(tag.getType().equalsIgnoreCase(type) && tag.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Photo)) return false;
		Photo photo = (Photo) obj;
		return this.getPhotoData().equals(photo.getPhotoData());
	}
}
