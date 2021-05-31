/*
 *	[CS213] Assignment 1
 *	by Greg Melillo and Eric Kim
*/

package application;

import java.io.IOException;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.UserList;

/**Main application class
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Photos extends Application {
	@Override
	public void start(Stage primaryStage) 
	throws IOException {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("../view/login.fxml"));
		
		
		AnchorPane root = (AnchorPane)loader.load();
		
		LoginController loginController = loader.getController();
		loginController.start(primaryStage);

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setScene(scene);
		primaryStage.show(); 
	}
	


	/**Main method
	 * @param args	the args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
	 * Saves the user data in the event that the main window is closed
	 */
	@Override
	public void stop(){
	    UserList.serializeUsers();
	}

}
