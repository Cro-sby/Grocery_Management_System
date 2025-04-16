package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.beans.property.SimpleStringProperty;

public class EmployeeView {
    private final SimpleStringProperty username;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;

    public EmployeeView(String username, String name, String phone) {
        this.username = new SimpleStringProperty(username);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
    }

    public String getUsername() { return username.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }
}