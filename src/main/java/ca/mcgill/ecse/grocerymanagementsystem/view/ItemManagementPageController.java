package ca.mcgill.ecse.grocerymanagementsystem.view;

// Imports for backend controller, exception, and TO
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOs.TOItem; // Ensure this path is correct

// JavaFX Imports
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.CustomerUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType; // Import Alert for error popups
import javafx.scene.control.cell.PropertyValueFactory;

// Java Util Imports
import java.util.List;

// Removed unused imports: import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
// Removed unused imports: import ca.mcgill.ecse.grocerymanagementsystem.model.CustomerUI;

public class ItemManagementPageController {

    // --- FXML Elements (Keep as they are) ---
    @FXML private TextField addItemNameInput;
    @FXML private TextField addItemPriceInput;
    @FXML private CheckBox addItemIsPerishableInput;
    @FXML private TextField addItemPointsInput;
    @FXML private Button addItemButton;
    @FXML private TextField updateItemNameInput;
    @FXML private TextField updateItemPriceInput;
    @FXML private TextField updateItemPointsInput;
    @FXML private Button updateItemButton;
    @FXML private TextField deleteItemNameInput;
    @FXML private Button deleteItemButton;
    @FXML private TableView<TOItem> itemTableView;
    @FXML private TableColumn<TOItem, String> itemNameColumn;
    @FXML private TableColumn<TOItem, Integer> itemPriceColumn;
    @FXML private TableColumn<TOItem, Integer> itemQuantityColumn;
    @FXML private TableColumn<TOItem, Boolean> itemPerishableColumn;
    @FXML private TableColumn<TOItem, Integer> itemPointsColumn;
    @FXML private Button refreshButton;
    @FXML private Label statusLabel;

    // --- Initialization ---

    @FXML
    public void initialize() {
        // Configure Table Columns (Same as before)
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInInventory"));
        itemPerishableColumn.setCellValueFactory(new PropertyValueFactory<>("isPerishable"));
        itemPointsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPoints"));

        // *** REMOVED Global Refresh Listener and Registration ***
        // itemTableView.addEventHandler(GroceryStoreView.REFRESH, e -> {
        //     refreshItemTable();
        // });
        // GroceryStoreView.registerRefreshableNode(itemTableView);

        // Perform an initial population of the table when the view loads
        refreshItemTable(); // Call local refresh directly

        // Clear the status label initially
        statusLabel.setText("");
    }

    // --- Action Handlers ---

    @FXML
    void handleAddItemClicked(ActionEvent event) {
        // Input retrieval and validation (Same as before)
        String name = addItemNameInput.getText();
        String priceStr = addItemPriceInput.getText();
        boolean isPerishable = addItemIsPerishableInput.isSelected();
        String pointsStr = addItemPointsInput.getText();

        if (isNullOrBlank(name)) { showError("Item name cannot be empty."); return; }
        if (isNullOrBlank(priceStr)) { showError("Price cannot be empty."); return; }
        if (isNullOrBlank(pointsStr)) { showError("Loyalty points cannot be empty."); return; }

        try {
            // Parsing (Same as before)
            int price = Integer.parseInt(priceStr);
            int points = Integer.parseInt(pointsStr);

            // Backend call (Same as before)
            ItemController.create(name, isPerishable, points, price);

            // Success Actions
            clearAddInputs();
            statusLabel.setText("Item '" + name + "' added successfully.");
            refreshItemTable(); // *** CHANGED: Call local refresh directly ***

        } catch (NumberFormatException e) {
            showError("Price and points must be valid integers.");
        } catch (GroceryStoreException e) {
            showError("Error adding item: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateItemClicked(ActionEvent event) {
        // Input retrieval and validation (Same as before)
        String name = updateItemNameInput.getText();
        String priceStr = updateItemPriceInput.getText();
        String pointsStr = updateItemPointsInput.getText();

        if (isNullOrBlank(name)) { showError("Item name to update cannot be empty."); return; }
        boolean priceProvided = !isNullOrBlank(priceStr);
        boolean pointsProvided = !isNullOrBlank(pointsStr);
        if (!priceProvided && !pointsProvided) { showError("Please provide a new price or new points to update."); return; }

        try {
            // Backend calls (Same as before)
            boolean updated = false;
            if (priceProvided) {
                int price = Integer.parseInt(priceStr);
                ItemController.updatePrice(name, price);
                updated = true;
            }
            if (pointsProvided) {
                int points = Integer.parseInt(pointsStr);
                ItemController.updatePoints(name, points);
                updated = true;
            }

            // Success Actions
            if (updated) {
                clearUpdateInputs();
                statusLabel.setText("Item '" + name + "' updated successfully.");
                refreshItemTable(); // *** CHANGED: Call local refresh directly ***
            }

        } catch (NumberFormatException e) {
            showError("Price and points must be valid integers if provided.");
        } catch (GroceryStoreException e) {
            showError("Error updating item: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteItemClicked(ActionEvent event) {
        // Input retrieval and validation (Same as before)
        String name = deleteItemNameInput.getText();
        if (isNullOrBlank(name)) { showError("Item name to delete cannot be empty."); return; }

        try {
            // Backend call (Same as before)
            ItemController.delete(name);

            // Success Actions
            clearDeleteInputs();
            statusLabel.setText("Item '" + name + "' deleted successfully.");
            refreshItemTable(); // *** CHANGED: Call local refresh directly ***

        } catch (GroceryStoreException e) {
            showError("Error deleting item: " + e.getMessage());
        }
    }

    @FXML
    void handleRefreshClicked(ActionEvent event) {
        // Manual refresh button calls local refresh directly
        statusLabel.setText("Refreshing table...");
        refreshItemTable(); // *** CHANGED: Call local refresh directly ***
        // statusLabel.setText("Table refreshed."); // Handled inside refreshItemTable now
    }

    // --- Helper Methods ---

    /**
     * Fetches all items from the backend controller, updates the TableView's
     * data model, and explicitly forces a visual refresh of the table.
     */

    private void refreshItemTable() {
        List<TOItem> itemsList = ItemController.getAllItems(); // Make sure you're using UserController here
        ObservableList<TOItem> observable = FXCollections.observableArrayList(itemsList);
        itemTableView.setItems(observable); // This will refresh the table view with the latest data
        itemTableView.refresh();
    }


    /**
     * Displays an error message in a popup dialog. (Same as before)
     */
    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Utility method to check for null or blank strings. (Same as before)
     */
    private boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /** Clears input fields for adding items. (Same as before) */
    private void clearAddInputs() {
        addItemNameInput.clear();
        addItemPriceInput.clear();
        addItemIsPerishableInput.setSelected(false);
        addItemPointsInput.clear();
    }

    /** Clears input fields for updating items. (Same as before) */
    private void clearUpdateInputs() {
        updateItemNameInput.clear();
        updateItemPriceInput.clear();
        updateItemPointsInput.clear();
    }

    /** Clears input field for deleting items. (Same as before) */
    private void clearDeleteInputs() {
        deleteItemNameInput.clear();
    }
}