package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**UserList class contains a static arraylist of users that will be serialized/deserialized. Also contains a static reference to the current user
 * that all controllers will have access to.
 * @author Eric S Kim
 * @author Greg Melillo
 */
public class UserList {
	/**
	 * Directory string
	 */
	private static final String directory = "data";
	/**
	 * filename string
	 */
	private static final String file = "users";
	/**
	 * The user list
	 */
	private static ArrayList<User> users;
	
	/**
	 * The current username being tracked throughout the program's lifetime
	 */
	private static String current_username;
	
	/**
	 * The current album being tracked throughout the program's lifetime
	 */
	private static String current_album;
	
	
	/**Gets the current user
	 * @return	the current user
	 */
	public static User getCurrentUser() {
		User temp = UserList.getUser(current_username);
		return temp;
	}
	
	/**Set the current user
	 * @param user	the current user
	 */
	public static void setCurrentUser(User user) {
		UserList.current_username = user.getUsername();
	}
	
	/**Gets the current album
	 * @return	the current album
	 */
	public static Album getCurrentAlbum() {
		Album temp = getCurrentUser().getAlbum(current_album);
		return temp;
	}
	
	/**Sets the current album
	 * @param album	the current album
	 */
	public static void setCurrentAlbum(Album album) {
		UserList.current_album = album.getAlbumTitle();
	}
	
	/**Gets the User list
	 * @return	the user list
	 */
	public static List<User> getUserList() {
		return UserList.users;
	}
	
	/**Adds a new User to the list
	 * @param name	the name of the new user
	 * @return	boolean confirming whether the addition was successful
	 */
	public static boolean addNewUser(String name) {
		if(usernameExists(name)) return false;
		UserList.users.add(new User(name));
		return true;
	}
	
	/**Removes the user from the list
	 * @param user	the user to be removed
	 * @return	boolean confirming whether the removal was successful
	 */
	public static boolean removeUser(User user) {
		if(!usernameExists(user.getUsername())) return false;
		try {
			UserList.users.remove(user);
			return true;
		} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**Checks whether the username exists in the User list
	 * @param name	the name being checked
	 * @return	boolean confirming whether the username exists in the list
	 */
	public static boolean usernameExists(String name) {
		if(UserList.users == null || users.isEmpty()) return false;
		for(User user : UserList.users) {
			if(user.getUsername().equals(name)) return true;
		}
		return false;
	}
	
	/**
	 * Searches the username list for the given name
	 * @param name the username to be looked up
	 * @return the user if one is found
	 */
	public static User getUser(String name) { 
		try {
			for (User user : UserList.users) {
			     if (user.getUsername().equals(name)) {
			        return user;
			     }
			  }
			 return null; 
		} catch(NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Grabs the serialized user data from file
	 */
	public static void deserializeUsers() {
		try {
			FileInputStream fileIn = new FileInputStream(directory +File.separator +file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			UserList.users = (ArrayList<User>) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException | ClassNotFoundException e) {
			
		}finally {
			if(UserList.users == null) {
				UserList.users = new ArrayList<User>();
				UserList.addNewUser("admin");
				UserList.addNewUser("stock");
			}
		}
	}
	
	/**
	 * Writes user data to file
	 */
	public static void serializeUsers() {
		try {
			FileOutputStream fileOut = new FileOutputStream(directory +File.separator +file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(UserList.users);
			out.close();
			fileOut.close();
		}catch(IOException e) {
			
		}	
	}
}
