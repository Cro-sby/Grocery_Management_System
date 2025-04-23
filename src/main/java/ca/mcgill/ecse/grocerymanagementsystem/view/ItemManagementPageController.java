package ca.mcgill.ecse.grocerymanagementsystem.view;

// Imports for backend controller, exception, and TO
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOs.TOItem;

// JavaFX Imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

// Java Util Imports
import java.util.List;



public class ItemManagementPageController {

    // --- FXML Elements ---
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

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInInventory"));
        itemPerishableColumn.setCellValueFactory(new PropertyValueFactory<>("isPerishable"));
        itemPointsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPoints"));


        refreshItemTable();


        statusLabel.setText("");
    }

    // --- Action Handlers ---

    @FXML
    void handleAddItemClicked(ActionEvent event) {

        String name = addItemNameInput.getText();
        String priceStr = addItemPriceInput.getText();
        boolean isPerishable = addItemIsPerishableInput.isSelected();
        String pointsStr = addItemPointsInput.getText();

        if (isNullOrBlank(name)) { showError("Item name cannot be empty."); return; }
        if (isNullOrBlank(priceStr)) { showError("Price cannot be empty."); return; }
        if (isNullOrBlank(pointsStr)) { showError("Loyalty points cannot be empty."); return; }

        try {

            int price = Integer.parseInt(priceStr);
            int points = Integer.parseInt(pointsStr);


            ItemController.create(name, isPerishable, points, price);


            clearAddInputs();
            statusLabel.setText("Item '" + name + "' added successfully.");
            refreshItemTable();

        } catch (NumberFormatException e) {
            showError("Price and points must be valid integers.");
        } catch (GroceryStoreException e) {
            showError("Error adding item: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateItemClicked(ActionEvent event) {

        String name = updateItemNameInput.getText();
        String priceStr = updateItemPriceInput.getText();
        String pointsStr = updateItemPointsInput.getText();

        if (isNullOrBlank(name)) { showError("Item name to update cannot be empty."); return; }
        boolean priceProvided = !isNullOrBlank(priceStr);
        boolean pointsProvided = !isNullOrBlank(pointsStr);
        if (!priceProvided && !pointsProvided) { showError("Please provide a new price or new points to update."); return; }

        try {

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


            if (updated) {
                clearUpdateInputs();
                statusLabel.setText("Item '" + name + "' updated successfully.");
                refreshItemTable();
            }

        } catch (NumberFormatException e) {
            showError("Price and points must be valid integers if provided.");
        } catch (GroceryStoreException e) {
            showError("Error updating item: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteItemClicked(ActionEvent event) {

        String name = deleteItemNameInput.getText();
        if (isNullOrBlank(name)) { showError("Item name to delete cannot be empty."); return; }

        try {

            ItemController.delete(name);


            clearDeleteInputs();
            statusLabel.setText("Item '" + name + "' deleted successfully.");
            refreshItemTable();

        } catch (GroceryStoreException e) {
            showError("Error deleting item: " + e.getMessage());
        }
    }

    @FXML
    void handleRefreshClicked(ActionEvent event) {

        statusLabel.setText("Refreshing table...");
        refreshItemTable();

    }

    // --- Helper Methods ---


    private void refreshItemTable() {
        List<TOItem> itemsList = ItemController.getAllItems();
        ObservableList<TOItem> observable = FXCollections.observableArrayList(itemsList);
        itemTableView.setItems(observable);
        itemTableView.refresh();
    }



    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }


    private void clearAddInputs() {
        addItemNameInput.clear();
        addItemPriceInput.clear();
        addItemIsPerishableInput.setSelected(false);
        addItemPointsInput.clear();
    }


    private void clearUpdateInputs() {
        updateItemNameInput.clear();
        updateItemPriceInput.clear();
        updateItemPointsInput.clear();
    }


    private void clearDeleteInputs() {
        deleteItemNameInput.clear();
    }
}