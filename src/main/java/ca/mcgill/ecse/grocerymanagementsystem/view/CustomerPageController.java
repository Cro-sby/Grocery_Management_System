package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.model.CustomerUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerPageController {

    @FXML private TableView<CustomerView> customerTable;
    @FXML private TableColumn<CustomerView, String> usernameCol;
    @FXML private TableColumn<CustomerView, String> nameCol;
    @FXML private TableColumn<CustomerView, String> phoneCol;
    @FXML private TableColumn<CustomerView, String> addressCol;
    @FXML private TableColumn<CustomerView, Number> loyaltyCol;

    // Register fields
    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private TextField registerAddressField;

    // Update fields
    @FXML private TextField updateUsernameField;
    @FXML private TextField updateNameField;
    @FXML private TextField updatePhoneField;
    @FXML private PasswordField updatePasswordField;
    @FXML private TextField updateAddressField;

    // Delete field
    @FXML private TextField deleteUsernameField;

    // Status message
    @FXML private Label statusLabel;

    // In-memory "database" of customers
    private final List<CustomerUI> customerList = new ArrayList<>();

    @FXML
    private void initialize() {
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        phoneCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        addressCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
        loyaltyCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getLoyaltyPoints()));

        refreshCustomerTable();
    }

    @FXML
    private void handleRegisterCustomer() {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String address = registerAddressField.getText();

        if (getCustomerByUsername(username) != null) {
            setStatus("Username already exists.", true);
            return;
        }

        CustomerUI newCustomer = new CustomerUI(username, password, "", "", address, 0);
        customerList.add(newCustomer);

        setStatus("Customer registered successfully!", false);
        clearRegisterFields();
        refreshCustomerTable();
    }

    @FXML
    private void handleUpdateCustomer() {
        String username = updateUsernameField.getText();
        CustomerUI customer = getCustomerByUsername(username);

        if (customer == null) {
            setStatus("Customer not found.", true);
            return;
        }

        if (!updatePasswordField.getText().isBlank()) {
            customer.setPassword(updatePasswordField.getText());
        }

        if (!updateAddressField.getText().isBlank()) {
            customer.setAddress(updateAddressField.getText());
        }

        if (!updateNameField.getText().isBlank()) {
            customer.setName(updateNameField.getText());
        }

        String phoneInput = updatePhoneField.getText();
        if (!phoneInput.isBlank()) {
            if (!phoneInput.matches("^\\(\\d{3}\\) \\d{3}-\\d{4}$")) {
                setStatus("Phone number must be in format (xxx) xxx-xxxx", true);
                return;
            }
            customer.setPhoneNumber(phoneInput);
        }

        setStatus("Customer info updated successfully!", false);
        clearUpdateFields();
        refreshCustomerTable();
    }


    @FXML
    private void handleDeleteCustomer() {
        String username = deleteUsernameField.getText();
        CustomerUI customer = getCustomerByUsername(username);

        if (customer == null) {
            setStatus("Customer not found.", true);
            return;
        }

        customerList.remove(customer);

        setStatus("Customer deleted successfully.", false);
        deleteUsernameField.clear();
        refreshCustomerTable();
    }

    private void refreshCustomerTable() {
        List<CustomerView> views = new ArrayList<>();

        for (CustomerUI customer : customerList) {
            views.add(new CustomerView(
                    customer.getUsername(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getAddress(),
                    customer.getLoyaltyPoints()
            ));
        }

        ObservableList<CustomerView> observableList = FXCollections.observableArrayList(views);
        customerTable.setItems(observableList);
    }

    private CustomerUI getCustomerByUsername(String username) {
        for (CustomerUI customer : customerList) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
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