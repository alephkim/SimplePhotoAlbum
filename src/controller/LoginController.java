package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.*;

/**
 * Handles the login screen
 * @author Greg Melillo
 * @author Eric S Kim
 */
public class LoginController {
	/**
	 * login button
	 */
	@FXML Button button_login;
	/**
	 * Username text field
	 */
	@FXML TextField textfield_username;
		
	/**
	 * String that tracks the entered username
	 */
	private String entered_username;
	
	/**
	 * Initializes the controller
	 */
	public void initialize() {
		UserList.deserializeUsers();
	}
	
	@FXML
	/**
	 * Handles clicking of buttons on login screen
	 */
	private void handleButtonAction(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		
		// Login button pressed
		if (b == button_login) {
			entered_username = textfield_username.getText();

			User user = UserList.getUser(entered_username);
			if(user != null) { // user with that name found
				
				// change to Album View scene
				
	            //start passing user and user list data to AlbumViewController
				String path = user.getUsername().equals("admin") ? "../view/admin.fxml" : "../view/album_view.fxml";
				
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
	            Stage stage = (Stage) button_login.getScene().getWindow();
	            UserList.setCurrentUser(user);
	            UserList.serializeUsers();
	            //end data passing

				stage.setScene(new Scene(loader.load()));
				stage.show();
			}else {
				// display some error text on login screen
			}
		}
	}
	
	
	/**Starts the controller
	 * @param mainStage		main stage
	 */
	public void start(Stage mainStage) {
	    // execute own shutdown procedure
	    mainStage.close();
	}
}
