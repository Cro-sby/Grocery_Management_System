package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class GroceryStoreView extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MainPage.fxml"))); // Load FXML
		stage.setTitle("Grocery Store Management"); // Set title
		stage.setScene(new Scene(root)); // Set scene
		stage.show(); // Show stage
	}
	public static void main(String[] args) {
		launch(args);
	}
}