package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOs.TOItem;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOs.TOShipment;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ShipmentManagementPageController {

    public TextField createShipmentButton;
    public TextField deleteShipmentNumberInput;
    public TextField addItemShipmentNumberInput;
    public TextField addItemNameInput;
    public TextField updateQuantityShipmentNumberInput;
    public TextField updateQuantityItemNameInput;
    public TextField updateQuantityInput;
    public TextField receiveShipmentNumberInput;
    public ComboBox<String> itemDropdown;


    @FXML
    private TableView<TOShipment> shipmentTableView;
    @FXML
    private TableColumn<TOShipment, String> shipmentNumberColumn;
    @FXML
    private TableColumn<TOShipment, String> shipmentDateOrderedColumn;

    @FXML
    private void initialize() {
        shipmentNumberColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getShipmentNumber()));

        shipmentDateOrderedColumn.setCellValueFactory(cellData -> {
            java.sql.Date date = cellData.getValue().getDateOrdered();
            String formattedDate = (date != null) ? date.toString() : "";
            return new ReadOnlyStringWrapper(formattedDate);
        });

        // Refresh setup
        shipmentTableView.addEventHandler(GroceryStoreView.REFRESH, e -> {
            List<TOShipment> shipments = ShipmentController.getShipments();
            shipmentTableView.setItems(FXCollections.observableList(shipments));
        });
        GroceryStoreView.registerRefreshableNode(shipmentTableView);
    }


    public void handleCreateShipmentClick() {
        try {
            // Backend call
            ShipmentController.createShipment();

            // Success Actions
            GroceryStoreView.refresh();
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleDeleteShipmentClick() {
        try {
            int shipmentNumber = Integer.parseInt(deleteShipmentNumberInput.getText());

            // Backend call
            ShipmentController.deleteShipment(shipmentNumber);

            // Success Actions
            deleteShipmentNumberInput.setText("");
            GroceryStoreView.refresh();
        } catch (NumberFormatException e) {
            ViewUtils.showErrorMessage("Shipment number must be a valid integer.");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleAddItemToShipmentClick() {
        try {
            int shipmentNumber = Integer.parseInt(addItemShipmentNumberInput.getText());
            String itemName = addItemNameInput.getText();

            // Backend call
            ShipmentController.addItemToShipment(shipmentNumber, itemName);

            // Success Actions
            addItemNameInput.setText("");
            GroceryStoreView.refresh();
        } catch (NumberFormatException e) {
            ViewUtils.showErrorMessage("Shipment number must be a valid integer.");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleUpdateQuantityClick() {
        try {
            int shipmentNumber = Integer.parseInt(updateQuantityShipmentNumberInput.getText());
            String itemName = updateQuantityItemNameInput.getText();
            int quantity = Integer.parseInt(updateQuantityInput.getText());

            // Backend call
            ShipmentController.updateQuantityInShipment(shipmentNumber, itemName, quantity);

            // Success Actions
            updateQuantityInput.setText("");
            GroceryStoreView.refresh();
        } catch (NumberFormatException e) {
            ViewUtils.showErrorMessage("Shipment number and quantity must be valid integers.");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleReceiveShipmentClick() {
        try {
            int shipmentNumber = Integer.parseInt(receiveShipmentNumberInput.getText());

            // Backend call
            ShipmentProcessingController.receiveShipment(shipmentNumber);

            // Success Actions
            receiveShipmentNumberInput.setText("");
            GroceryStoreView.refresh();
        } catch (NumberFormatException e) {
            ViewUtils.showErrorMessage("Shipment number must be a valid integer.");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }


    /**
     * Utility method to check for null or blank strings.
     */
    private boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static class ViewUtils {
        public static void showErrorMessage(String message) {
            makePopupWindow("Error", message);
        }

        public static void makePopupWindow(String title, String message) {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogPane = new VBox();

            // create UI elements
            Text text = new Text(message);
            Button okButton = new Button("OK");
            okButton.setOnAction(a -> dialog.close());

            // display the popup window
            int innerPadding = 10; // inner padding/spacing
            int outerPadding = 100; // outer padding
            dialogPane.setSpacing(innerPadding);
            dialogPane.setAlignment(Pos.CENTER);
            dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
            dialogPane.getChildren().addAll(text, okButton);
            Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
            dialog.setScene(dialogScene);
            dialog.setTitle(title);
            dialog.show();
        }
    }
}
