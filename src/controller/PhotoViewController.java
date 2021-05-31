package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.*;

/**Photo list and display controller
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class PhotoViewController {
	
	/**
	 * Photo caption text field
	 */
	@FXML TextField caption;
	/**
	 * Photo date text field
	 */
	@FXML TextField date;
	/**
	 * Photo tags text field
	 */
	@FXML TextField tags;
	
	/**
	 * Photo ListView
	 */
	@FXML ListView<Photo> photoListView;
	
	/**
	 * Photo display area
	 */
	@FXML ImageView display;
	
	/**
	 * Photo ObservableList
	 */
	private ObservableList<Photo> obsList;
	
	/**
	 * Initializes the controller
	 */
	@FXML
	public void initialize() {
		UserList.deserializeUsers();
		
		obsList = FXCollections.observableArrayList(UserList.getCurrentAlbum().getPhotoList());
		photoListView.setItems(obsList);
		
		photoListView.setCellFactory(param -> new ListCell<Photo>() {
			private ImageView imgView = new ImageView();
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                	imgView.setImage(item.getImage());
                	imgView.setFitHeight(60);
        			imgView.setFitWidth(60);
        			imgView.setPreserveRatio(true);
                    setText(null);
                    setGraphic(imgView);
                }
            }
        });
		
		caption.setEditable(false);
		date.setEditable(false);
		tags.setEditable(false);
		

		// select the first item
        photoListView.getSelectionModel().clearAndSelect(0);
        showPhotoDetails();
		photoListView.getSelectionModel().selectedItemProperty().addListener( (obs, oldVal, newVal) -> showPhotoDetails() );
	}
	
	/**
	 * Populates photo display and text into
	 */
	private void showPhotoDetails() {
		Photo photo = photoListView.getSelectionModel().getSelectedItem();
		if(photo == null) return;
		photo.sortTags();
		display.setImage(photo.getImage());
		caption.setText(photo.getCaption());
		date.setText(photo.getDate());
		tags.setText(photo.getTagList().toString());
	}
	

	/**Returns to the album window
	 * @param e		the action event
	 */
	@FXML
	private void goBack(ActionEvent e) {
		UserList.serializeUsers();
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/album_view.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();	
			stage.hide();
			
			stage.setScene(scene);
			stage.show();
		} catch(IOException error) {
			error.printStackTrace();
		}
	}
	
	/**Adds a photo to the current album
	 * @param e		action event
	 * @throws IOException
	 */
	@FXML
	private void addPhoto(ActionEvent e) throws IOException {
		FileChooser fChooser = new FileChooser();
		
		FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        fChooser.setTitle("Add Photo");
        
        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		File file = fChooser.showOpenDialog(app_stage);
		
		if(file == null) return;
		
		BufferedImage buffImage = ImageIO.read(file);
		Image newImage = SwingFXUtils.toFXImage(buffImage, null);
		
		PhotoData pData = new PhotoData();
		pData.setPixelsFromImage(newImage);
		//check if photo exists in the album
		for(Photo photo : UserList.getCurrentAlbum().getPhotoList()) {
			if(pData.equals(photo.getPhotoData())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Add Photo Failed");
				alert.setHeaderText(null);
				alert.setContentText("That photo already exists in the album!");
				alert.showAndWait();
				return;
			}
		}
		
		//check if photo exists in other albums, add that photo object if one exists
		for(Album album : UserList.getCurrentUser().getAlbumList()) {
			for(Photo photo : album.getPhotoList()) {
				if(pData.equals(photo.getPhotoData())) {
					obsList.add(photo);
					UserList.getCurrentAlbum().addPhoto(photo);
					UserList.serializeUsers();	
					photoListView.getSelectionModel().select(photo);
					return;
				}
			}
		}
		
		//add new photo
		Photo temp = new Photo(pData.getImageFromPixels());
		obsList.add(temp);
		UserList.getCurrentAlbum().addPhoto(temp);
		photoListView.getSelectionModel().select(temp);
		UserList.serializeUsers();
	}
	
	/**
	 * Deletes the selected photo from the album.
	 */
	@FXML
	private void deletePhoto() {
		Photo temp = photoListView.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete this photo?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			obsList.remove(temp);
			UserList.getCurrentAlbum().removePhoto(temp);
			UserList.serializeUsers();
		}
		if(obsList.size() == 0) {
			display.setImage(null);
			date.clear();
			tags.clear();
			caption.clear();
			photoListView.getSelectionModel().clearSelection();
		}
	}
	
	/**
	 * Moves the selected photo to an existing album via drop-down menu.
	 */
	@FXML
	private void movePhoto() {
		Photo temp = photoListView.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		List<String> choices = new ArrayList<String>();
		for(Album album : UserList.getCurrentUser().getAlbumList()) {
			if(!UserList.getCurrentAlbum().getAlbumTitle().equals(album.getAlbumTitle())) choices.add(album.getAlbumTitle());
		}
		Collections.sort(choices);
		
		if(choices == null || choices.size() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Move Photo Failed");
			alert.setHeaderText(null);
			alert.setContentText("There is no other album to move the photo to!");
			alert.showAndWait();
			return;
		}
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Select the album you wish to move your photo to.");
		dialog.setContentText("Choose your album");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			String s = result.get();
			if(UserList.getCurrentUser().getAlbum(s).addPhoto(temp)) {
				obsList.remove(temp);
				UserList.getCurrentAlbum().removePhoto(temp);
				UserList.serializeUsers();
				if(obsList.size() == 0) {
					display.setImage(null);
					date.clear();
					tags.clear();
					caption.clear();
					photoListView.getSelectionModel().clearSelection();
				}
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Move Photo Failed");
				alert.setHeaderText(null);
				alert.setContentText("That photo already exists in that album!");
				alert.showAndWait();
			}
		}
	}
	
	/**
	 * Copies the selected photo to an existing album via drop-down menu.
	 */
	@FXML
	private void copyPhoto() {
		Photo temp = photoListView.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		List<String> choices = new ArrayList<String>();
		for(Album album : UserList.getCurrentUser().getAlbumList()) {
			if(!UserList.getCurrentAlbum().getAlbumTitle().equals(album.getAlbumTitle())) choices.add(album.getAlbumTitle());
		}
		Collections.sort(choices);
		
		if(choices == null || choices.size() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Copy Photo Failed");
			alert.setHeaderText(null);
			alert.setContentText("There is no other album to copy the photo to!");
			alert.showAndWait();
			return;
		}
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Copy Photo");
		dialog.setHeaderText("Select the album you wish to copy your photo to.");
		dialog.setContentText("Choose your album");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			String s = result.get();
			if(!UserList.getCurrentUser().getAlbum(s).addPhoto(temp)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Copy Photo Failed");
				alert.setHeaderText(null);
				alert.setContentText("That photo already exists in that album!");
				alert.showAndWait();
			}
			UserList.serializeUsers();
		}
	}
	
	/**
	 * Adds a new tag to the selected photo via pop-up window.
	 */
	@FXML
	private void addTag() {
		Photo temp = photoListView.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Type in a new tag for your photo.");
		
		Label tagType = new Label("Tag Type:");
		Label tagName = new Label("Tag Name:");
		TextField tagTypeField = new TextField();
		TextField tagNameField = new TextField();
		
		ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.add(tagType, 0, 0);
		grid.add(tagName, 0, 1);
		grid.add(tagTypeField, 1, 0);
		grid.add(tagNameField, 1, 1);
		
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
			if(dialogButton == confirmButtonType) {
				return new Pair<>(tagTypeField.getText(), tagNameField.getText());
			}
			return null;
		});
		
		Optional<Pair<String, String>> pair = dialog.showAndWait();
		if(pair.isPresent()) {
			String type = pair.get().getKey();
			String name = pair.get().getValue();
			if(type.replaceAll("\\s","").isEmpty() || type == null || name.replaceAll("\\s","").isEmpty() || name == null || !temp.addTag(type, name)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Add Tag Failed");
				alert.setHeaderText(null);
				alert.setContentText("That tag either exists or is not valid.");
				alert.showAndWait();
				return;
			}
			temp.sortTags();
			//UserList.serializeUsers();
			showPhotoDetails();
		}
	}
	
	/**
	 * Removes a tag from the selected photo via drop-down menu.
	 */
	@FXML
	private void removeTag() {
		Photo temp = photoListView.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		List<String> choices = new ArrayList<String>();
		for(Tag tag : temp.getTagList()) {
			choices.add(tag.getType() +": " +tag.getName());
		}
		if(choices == null || choices.size() < 1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Remove Tag Failed");
			alert.setHeaderText(null);
			alert.setContentText("There are no tags to remove!");
			alert.showAndWait();
			return;
		}
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Remove Tag");
		dialog.setHeaderText("Select the tag you wish to remove from your photo.");
		dialog.setContentText("Choose your tag");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			String[] tagInfo = result.get().split(":");
			temp.removeTag(tagInfo[0], tagInfo[1].substring(1));
			UserList.serializeUsers();
			showPhotoDetails();
		}
	}
	
	/**
	 * Add a caption to the selected photo. Overwrites the current caption.
	 */
	@FXML
	private void addCaption() {
		Photo temp = photoListView.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Caption");
		dialog.setHeaderText("Please enter the desired caption for your photo.");
		dialog.setContentText("Caption:");

		try {
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				String s = result.get();
				temp.setCaption(s);
				UserList.serializeUsers();
				showPhotoDetails();
			}
		} catch(NullPointerException e) {
			//probably a TERRIBLE idea but let's just pretend this doesn't happen for now
		}
	}
	
	/**
	 * Switches to the previous photo in the album list.
	 */
	@FXML
	private void previousPhoto() {
		int currentIndex = photoListView.getSelectionModel().getSelectedIndex();
		if(currentIndex == 0) {
			photoListView.getSelectionModel().clearAndSelect(obsList.size() - 1);
		} else {
			photoListView.getSelectionModel().clearAndSelect(currentIndex - 1);
		}
	}
	
	/**
	 * Switches to the next photo in the album list.
	 */
	@FXML
	private void nextPhoto() {
		int currentIndex = photoListView.getSelectionModel().getSelectedIndex();
		if(currentIndex == obsList.size() - 1) {
			photoListView.getSelectionModel().clearAndSelect(0);
		} else {
			photoListView.getSelectionModel().clearAndSelect(currentIndex + 1);
		}
	}
	
	/**Empty start method
	 * @param primaryStage		the primary stage
	 */
	public void start(Stage primaryStage) {
		
	}
}
