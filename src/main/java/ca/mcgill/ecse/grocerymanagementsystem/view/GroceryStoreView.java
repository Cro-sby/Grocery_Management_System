package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GroceryStoreView extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		// https://github.com/openjfx/samples/blob/master/HelloFX/Gradle/hellofx/src/main/java/HelloFX.java
		String message = "Hello there!";
		Scene scene = new Scene(new StackPane(new Label(message)), 640, 480);
		stage.setScene(scene);
		stage.show();
	}
}
