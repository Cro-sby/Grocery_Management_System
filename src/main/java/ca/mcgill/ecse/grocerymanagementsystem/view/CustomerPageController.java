package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.CustomerUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CustomerPageController {

    @FXML private TableColumn<CustomerUI, String> nameCol;
    @FXML private TableColumn<CustomerUI, String> usernameCol;
    @FXML private TableColumn<CustomerUI, String> phoneCol;
    @FXML private TableColumn<CustomerUI, String> addressCol;
    @FXML private TableColumn<CustomerUI, Integer> loyaltyCol;

    // TableView itself
    @FXML private TableView<CustomerUI> customerTable;

    // Other FXML fields (like TextFields, Labels, etc.)
    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private TextField registerAddressField;

    @FXML private TextField updateUsernameField;
    @FXML private PasswordField updatePasswordField;
    @FXML private TextField updateAddressField;
    @FXML private TextField updateNameField;
    @FXML private TextField updatePhoneField;

    @FXML private TextField deleteUsernameField;

    @FXML private Label statusLabel;


    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        loyaltyCol.setCellValueFactory(new PropertyValueFactory<>("loyaltyPoints"));

        refreshCustomerTable();
    }


    @FXML
    private void handleRegisterCustomer() {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String address = registerAddressField.getText();

        try {
            UserController.registerNewCustomer(username, password, address);
            refreshCustomerTable();
            statusLabel.setText("Customer registered.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleUpdateCustomer() {
        String username = updateUsernameField.getText();
        CustomerUI customer = getCustomerUIByUsername(username);  // Use the newly created method to find customer

        if (customer == null) {
            setStatus("Customer not found.", true);
            return;
        }

        if (!updatePasswordField.getText().isBlank()) {
            UserController.updatePassword(username, updatePasswordField.getText());
        }

        if (!updateAddressField.getText().isBlank()) {
            UserController.updateAddress(username, updateAddressField.getText());
        }

        if (!updateNameField.getText().isBlank()) {
            UserController.updateName(username, updateNameField.getText());
        }

        String phoneInput = updatePhoneField.getText();
        if (!phoneInput.isBlank()) {
            if (!phoneInput.matches("^\\(\\d{3}\\) \\d{3}-\\d{4}$")) {
                setStatus("Phone number must be in format (xxx) xxx-xxxx", true);
                return;
            }
            UserController.updatePhoneNumber(username, phoneInput);
        }

        setStatus("Customer info updated successfully!", false);
        clearUpdateFields();
        refreshCustomerTable();
    }



    @FXML
    private void handleDeleteCustomer() {
        String username = deleteUsernameField.getText();
        CustomerUI customer = getCustomerUIByUsername(username);  // Use the newly created method to find customer

        if (customer == null) {
            setStatus("Customer not found.", true);
            return;
        }

        try {
            // Call UserController to delete customer
            UserController.deleteCustomer(username);

            setStatus("Customer deleted successfully.", false);
            deleteUsernameField.clear();
            refreshCustomerTable();
        } catch (Exception e) {
            setStatus("Error deleting customer: " + e.getMessage(), true);
        }
    }


    private void refreshCustomerTable() {
        List<CustomerUI> customerList = UserController.getAllCustomers(); // Make sure you're using UserController here
        ObservableList<CustomerUI> observable = FXCollections.observableArrayList(customerList);
        customerTable.setItems(observable); // This will refresh the table view with the latest data
    }



    private CustomerUI getCustomerUIByUsername(String username) {
        List<CustomerUI> customers = UserController.getAllCustomers();
        for (CustomerUI customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;  // Return null if no matching customer is found
    }


    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isError ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
    }

    private void clearRegisterFields() {
        registerUsernameField.clear();
        registerPasswordField.clear();
        registerAddressField.clear();
    }

    private void clearUpdateFields() {
        updateUsernameField.clear();
        updateNameField.clear();
        updatePhoneField.clear();
        updatePasswordField.clear();
        updateAddressField.clear();
    }
}