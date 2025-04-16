package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerView {
    private final SimpleStringProperty username;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty address;
    private final SimpleIntegerProperty loyaltyPoints;

    public CustomerView(String username, String name, String phone, String address, int loyaltyPoints) {
        this.username = new SimpleStringProperty(username);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.loyaltyPoints = new SimpleIntegerProperty(loyaltyPoints);
    }

    public String getUsername() { return username.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }
    public String getAddress() { return address.get(); }
    public int getLoyaltyPoints() { return loyaltyPoints.get(); }
}