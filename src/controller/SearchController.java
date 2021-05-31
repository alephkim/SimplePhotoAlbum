package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.UserList;
import model.Tag;


/**
 * Handles the search screen
 * @author Greg Melillo
 * @author Eric S Kim
 */
public class SearchController {
	
	@FXML ComboBox<String> searchType;

	@FXML Button button_dateConfirm;
	@FXML DatePicker datePicker_from;
	@FXML DatePicker datePicker_to;
	
	@FXML Button button_back;
	@FXML Button button_1tagConfirm;
	@FXML Button button_2tagConfirm;
	@FXML Button button_newAlbum;

	

	
	@FXML ComboBox<String> combo_numTags;
	@FXML ComboBox<String> combo_tag1;
	@FXML ComboBox<String> combo_tag2;
	@FXML ComboBox<String> combo_logic;
	@FXML TextField textfield_tag1;
	@FXML TextField textfield_tag2;
	@FXML ListView<Photo> photoListView;



	private ObservableList<String> searchOptions;
	private ObservableList<String> logicOptions;
	private ObservableList<String> numTagsOptions;
	private ObservableList<String> tagOptions;
	private ObservableList<Photo> obsSearchResultsList;
	
	
	/**
	 * hold the search results Photo array
	 */
	private List<Photo> searchResultsList = new ArrayList<Photo>();

	
	public void initialize() {
		// populate user list
		UserList.deserializeUsers();
	
		
		// set the original view
		combo_numTags.setVisible(false);
		combo_tag1.setVisible(false);
		combo_tag2.setVisible(false);
	    combo_logic.setVisible(false);
		textfield_tag1.setVisible(false);
		textfield_tag2.setVisible(false);
		button_1tagConfirm.setVisible(false);
		button_2tagConfirm.setVisible(false);
		
		
		
		// set up the listview of images
		obsSearchResultsList = FXCollections.observableArrayList(searchResultsList);
		photoListView.setItems(obsSearchResultsList);
		
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
		


		// Set the search type combo box options
		searchOptions = 
			    FXCollections.observableArrayList(
			        "By Tag",
			        "By Date"
			    );
		
		// listen for changes in the combo box
		searchType.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue.equals("By Tag")){
				displayTagSearch();
			}else if(newValue.equals("By Date")) {
				displayDateSearch();
			}
		}
	    );
		
		
		// Set the logic combo box options
		logicOptions = 
			FXCollections.observableArrayList(
				"AND",
				"OR"
			);
				
		// listen for changes in the combo box
		combo_logic.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue.equals("AND")){
			}else if(newValue.equals("OR")) {
			}
		});
				
				
		// Set the num tags combo box options
		numTagsOptions = 
			FXCollections.observableArrayList(
					"1",
					"2"
			);
				
		// listen for changes in the combo box
		combo_numTags.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue.equals("1")){
				displayTagSearch(1);
			}else if(newValue.equals("2")) {
				displayTagSearch(2);
			}
		});
		
		
		// Set the num tags combo box options
		tagOptions = 
			FXCollections.observableArrayList(UserList.getCurrentUser().getUniqueTagTypes());
		
						
		// listen for changes in the combo box
		combo_tag1.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			
		});
		
		// listen for changes in the combo box
		combo_tag2.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
		
		});
		
								
		searchType.setItems(searchOptions);
		combo_logic.setItems(logicOptions);
		combo_numTags.setItems(numTagsOptions);
		combo_tag1.setItems(tagOptions);
		combo_tag2.setItems(tagOptions);
	}
	
	/**
	 * sets widgets for a Tag search
	 */
	public void displayTagSearch() {
		obsSearchResultsList.clear();

		button_dateConfirm.setVisible(false);
		datePicker_from.setVisible(false);
		datePicker_to.setVisible(false);
		combo_numTags.setVisible(true);
	}
	
	/**
	 * sets widgets for a 1 or 2 tag search
	 * @param i an int that represents a 1 or 2 tag search
	 */
	public void displayTagSearch(int i) {
		obsSearchResultsList.clear();

		if(i == 1) {
			combo_tag1.setVisible(true);
			combo_tag2.setVisible(false);
		    combo_logic.setVisible(false);
			textfield_tag1.setVisible(true);
			textfield_tag2.setVisible(false);
			button_1tagConfirm.setVisible(true);
			button_2tagConfirm.setVisible(false);

			
		}else {
			combo_tag1.setVisible(true);
			combo_tag2.setVisible(true);
		    combo_logic.setVisible(true);
			textfield_tag1.setVisible(true);
			textfield_tag2.setVisible(true);
			button_1tagConfirm.setVisible(false);
			button_2tagConfirm.setVisible(true);
		}
	}
	
	/**
	 * sets widgets for a Date search
	 */
	public void displayDateSearch() {
		obsSearchResultsList.clear();
		combo_numTags.setVisible(false);
		combo_tag1.setVisible(false);
		combo_tag2.setVisible(false);
	    combo_logic.setVisible(false);
		textfield_tag1.setVisible(false);
		textfield_tag2.setVisible(false);
		button_1tagConfirm.setVisible(false);
		button_2tagConfirm.setVisible(false);
		
		button_dateConfirm.setVisible(true);
		datePicker_from.setVisible(true);
		datePicker_to.setVisible(true);
	}
	
	/**
	 * handles clicking of confirm button for Date search
	 */
	public void searchByDate() {
		obsSearchResultsList.clear();
		
		LocalDate fromValue = datePicker_from.getValue();
		LocalDate toValue = datePicker_to.getValue();
		
		if(fromValue == null || toValue == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Search Failed");
			alert.setHeaderText(null);
			alert.setContentText("You have empty fields!");
			alert.showAndWait();
			return;
		}
		
		
		if(toValue.isBefore(fromValue)) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Date Search Failed");
			alert.setHeaderText(null);
			alert.setContentText("From-date must be before to-date");
			alert.showAndWait();
			datePicker_from.getEditor().clear();
			datePicker_to.getEditor().clear();
			return;
		}
		
		LocalDateTime t;
		LocalDate d;
		for(Album a: UserList.getCurrentUser().getAlbumList()) {
			for(Photo p: a.getPhotoList()) {
				 t = LocalDateTime.ofInstant(p.getCalendar().toInstant(), ZoneId.systemDefault());
				 d = t.toLocalDate();
				 if(d.isEqual(fromValue) || d.isAfter(fromValue) && (d.isEqual(toValue) || d.isBefore(toValue) )) {
					 obsSearchResultsList.add(p);
				 }
			}
		}
	}

	

	/**
	 * handles clicking of the confirm button for a 1 tag search
	 */
	public void searchBy1Tag() {
		if(!(combo_tag1.getValue() == null)
				&& !(textfield_tag1.getText() == null) ) {
			
			obsSearchResultsList.clear();
			
			String type = combo_tag1.getValue();
			String name = textfield_tag1.getText();

			
			for(Album a: UserList.getCurrentUser().getAlbumList()) {
				for(Photo p: a.getPhotoList()) {
					for(Tag t: p.getTagList()) {
						if(t.getType().equals(type) && t.getName().equals(name) && !obsSearchResultsList.contains(p)) {
							obsSearchResultsList.add(p);
						}
					}
				}
			}
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Search Failed");
			alert.setHeaderText(null);
			alert.setContentText("You have empty fields!");
			alert.showAndWait();
			return;
		}
	}
	
	/**
	 * handles clicking of the confirm button for a 2 tag search
	 */
	public void searchBy2Tag() {
		if(!(combo_logic.getValue() == null)
				&& !(combo_tag1.getValue() == null)
				&& !(combo_tag2.getValue() == null)
				&& !(combo_tag1.getValue() == null)
				&& !(textfield_tag1.getText() == null)
				&& !(textfield_tag2.getText() == null)){

			obsSearchResultsList.clear();
			String type1 = combo_tag1.getValue();
			String name1 = textfield_tag1.getText();
			
			String type2 = combo_tag2.getValue();
			String name2 = textfield_tag2.getText();

			for(Album a: UserList.getCurrentUser().getAlbumList()) {
				for(Photo p: a.getPhotoList()) {
					boolean firstTagExists = false;
					boolean secondTagExists = false;
					for(Tag t: p.getTagList()) {
						if(t.getType().equals(type1) && t.getName().equals(name1)) {
							firstTagExists = true;
						}
						if(t.getType().equals(type2) && t.getName().equals(name2)) {
							secondTagExists = true;
						}
					}

					if(combo_logic.getValue().equals("AND")) {
						if((firstTagExists && secondTagExists) && !obsSearchResultsList.contains(p)) {
							obsSearchResultsList.add(p);
						}
					}else if(combo_logic.getValue().equals("OR")) {
						if((firstTagExists || secondTagExists) && !obsSearchResultsList.contains(p)) {
							obsSearchResultsList.add(p);
						}
					}
				}
			}
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Search Failed");
			alert.setHeaderText(null);
			alert.setContentText("You have empty fields!");
			alert.showAndWait();
			return;
		}
		
	}

	/**
	 * handles clicking of the confirm button for the back button, returns to album view
	 */
	@FXML
	public void handleBackButtonClick(ActionEvent e) {
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
	
	/**
	 * listener for the pair of tag search confirm buttons, calls their corresponding methods in turn
	 */
	@FXML
	public void handleConfirmButtonClick(ActionEvent e) {
		Button b = (Button)e.getSource();

		if(b == button_1tagConfirm) {
			searchBy1Tag();
		}else if(b == button_2tagConfirm) {
			searchBy2Tag();
		}
		
	}
	
	/**
	 * listener for the "New Album" button
	 */
	@FXML
	public void handleNewAlbumButtonClick(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Album from Selection");
		dialog.setHeaderText("Please enter the new album's name.");
		dialog.setContentText("Album name:");
	
		try {
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				Album temp = new Album(result.get());
				if(!obsSearchResultsList.isEmpty()) {
					for(Photo p : obsSearchResultsList) {
						temp.addPhoto(p);
					}
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Create Album Failed");
					alert.setHeaderText(null);
					alert.setContentText("Album cannot be empty!");
					alert.showAndWait();
					return;
				}
				if(temp == null || UserList.getCurrentUser().albumTitleExists(temp.getAlbumTitle())) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Create Album Failed");
					alert.setHeaderText(null);
					alert.setContentText("An album with that title already exists!");
					alert.showAndWait();
					return;
				}
				
				UserList.getCurrentUser().addNewAlbum(temp);
				UserList.serializeUsers();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Album Created");
				alert.setHeaderText(null);
				alert.setContentText("Album successfully created!");
				alert.showAndWait();
				return;
			}
		} catch(NullPointerException n) {
			//probably a TERRIBLE idea but let's just pretend this doesn't happen for now
		}
	}
	
		
}
