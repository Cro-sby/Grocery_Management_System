package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.EmployeeUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class EmployeePageController {

    @FXML private TableView<EmployeeUI> employeeTable;
    @FXML private TableColumn<EmployeeUI, String> usernameCol;
    @FXML private TableColumn<EmployeeUI, String> nameCol;
    @FXML private TableColumn<EmployeeUI, String> phoneCol;

    // Register fields
    @FXML private TextField registerUsernameField;

    // Update fields
    @FXML private TextField updateUsernameField;
    @FXML private TextField updateNameField;
    @FXML private TextField updatePhoneField;

    // Delete field
    @FXML private TextField deleteUsernameField;

    // Status message
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        phoneCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhoneNumber()));

        refreshEmployeeTable();
    }

    @FXML
    private void handleRegisterEmployee() {
        String username = registerUsernameField.getText();

        try {
            UserController.registerNewEmployee(username);
            setStatus("Employee registered successfully!", false);
            clearRegisterFields();
            refreshEmployeeTable();
        } catch (Exception e) {
            setStatus("Error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleUpdateEmployee() {
        String username = updateUsernameField.getText();
        EmployeeUI employee = getEmployeeByUsername(username);

        if (employee == null) {
            setStatus("Employee not found.", true);
            return;
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

        setStatus("Employee info updated successfully!", false);
        clearUpdateFields();
        refreshEmployeeTable();
    }

    @FXML
    private void handleDeleteEmployee() {
        String username = deleteUsernameField.getText();
        EmployeeUI employee = getEmployeeByUsername(username);

        if (employee == null) {
            setStatus("Employee not found.", true);
            return;
        }

        try {
            UserController.deleteEmployee(username);
            setStatus("Employee deleted successfully.", false);
            deleteUsernameField.clear();
            refreshEmployeeTable();
        } catch (Exception e) {
            setStatus("Error deleting employee: " + e.getMessage(), true);
        }
    }

    private void refreshEmployeeTable() {
        List<EmployeeUI> employeeList = UserController.getAllEmployees();
        ObservableList<EmployeeUI> observableList = FXCollections.observableArrayList(employeeList);
        employeeTable.setItems(observableList);
    }

    private EmployeeUI getEmployeeByUsername(String username) {
        List<EmployeeUI> employees = UserController.getAllEmployees();
        for (EmployeeUI employee : employees) {
            if (employee.getUsername().equals(username)) {
                return employee;
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
    }

    private void clearUpdateFields() {
        updateUsernameField.clear();
        updateNameField.clear();
        updatePhoneField.clear();
    }
}
