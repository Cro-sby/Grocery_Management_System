package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
	public static final EventType<Event> REFRESH = new EventType<>("REFRESH");
	private static Set<Node> refreshableNodes = new HashSet<>();

	/**
	 * Register a new node as refreshable so that it will receive an event when the page needs to be refreshed.
	 */
	public static void registerRefreshableNode(Node n) {
		refreshableNodes.add(n);
	}

	/**
	 * Refresh all nodes that have been registered as refreshable.
	 */
	public static void refresh() {
		for (Node n : refreshableNodes) {
			n.fireEvent(new Event(REFRESH));
		}
	}
}