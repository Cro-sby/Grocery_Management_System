package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.model.EmployeeUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class EmployeePageController {

    @FXML private TableView<EmployeeView> employeeTable;
    @FXML private TableColumn<EmployeeView, String> usernameCol;
    @FXML private TableColumn<EmployeeView, String> nameCol;
    @FXML private TableColumn<EmployeeView, String> phoneCol;


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

    private final List<EmployeeUI> employeeList = new ArrayList<>();
    @FXML
    private void initialize() {
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        phoneCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));

        refreshEmployeeTable(); // Initial load of data
    }



    @FXML
    private void handleRegisterEmployee() {
        try {
            String username = registerUsernameField.getText();

            if (getEmployeeByUsername(username) != null) {
                setStatus("Username already exists.", true);
                return;
            }
            EmployeeUI newEmployee = new EmployeeUI(username,"","");
            employeeList.add(newEmployee);

            setStatus("Employee registered successfully!", false);

            clearRegisterFields();
            refreshEmployeeTable();
        } catch (GroceryStoreException e) {
            setStatus(e.getMessage(), true);
        }
    }



    @FXML
    private void handleUpdateEmployee() {
        String username = updateUsernameField.getText();
        EmployeeUI employee = getEmployeeByUsername(username);

        try {
            if (employee == null) {
                setStatus("Employee not found.", true);
                return;
            }

            if (!updateNameField.getText().isBlank()) {
                employee.setName(updateNameField.getText());
            }
            String phoneInput = updatePhoneField.getText();
            if (!phoneInput.isBlank()) {
                if (!phoneInput.matches("^\\(\\d{3}\\) \\d{3}-\\d{4}$")) {
                    setStatus("Phone number must be in format (xxx) xxx-xxxx", true);
                    return;
                }
                employee.setPhoneNumber(phoneInput);
            }

            setStatus("Employee info updated successfully!", false);
            clearUpdateFields();
            refreshEmployeeTable();


        } catch (GroceryStoreException e) {
            setStatus(e.getMessage(), true);
        }
    }

    @FXML
    private void handleDeleteEmployee() {
        try {
            String username = deleteUsernameField.getText();
            EmployeeUI employee = getEmployeeByUsername(username);
            if (employee == null) {
                setStatus("Employee not found.", true);
                return;
            }
            employeeList.remove(employee);
            setStatus("Employee deleted successfully.", false);
            deleteUsernameField.clear();
            refreshEmployeeTable();




        } catch (GroceryStoreException e) {
            setStatus(e.getMessage(), true);
        }
    }
    private EmployeeUI getEmployeeByUsername(String username) {
        for (EmployeeUI employee : employeeList) {
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

    @FXML
    private void refreshEmployeeTable() {
        // Get the list of all customers
        List<EmployeeView> views = new ArrayList<>();
        for (EmployeeUI employee : employeeList){
            views.add(new EmployeeView(
                    employee.getUsername(),
                    employee.getName(),
                    employee.getPhoneNumber()
            ));
        }

        // Set the items of the table
        ObservableList<EmployeeView> observableList = FXCollections.observableArrayList(views);
        employeeTable.setItems(observableList);
    }





}