package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

// Keep this import if you still need the singleton instance
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;

public class OrderProcessingController {

    // Helper to find order by number - Remains the same
    private static Order findOrderByOrderNumber(int orderNumber) throws GroceryStoreException {
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        for (Order order : system.getOrders()) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
    }

    // Helper to find employee by username - Remains the same
    private static Employee findEmployeeByUsername(String username) throws GroceryStoreException {
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        User user = User.getWithUsername(username);
        if (user == null) {
            throw new GroceryStoreException("there is no user with username \"" + username + "\"");
        }
        // Check if the user has an Employee role
        for (UserRole role : user.getRoles()) {
            if (role instanceof Employee) {
                return (Employee) role;
            }
        }
        // If loop finishes without finding an Employee role
        throw new GroceryStoreException("\"" + username + "\" is not an employee");
    }

    /**
     * Attempts to check out an order. Moves from Cart -> Checkout -> Pending.
     *
     * @param orderNumber The number of the order to check out.
     * @throws GroceryStoreException If the order cannot be checked out (wrong state, empty).
     */
    public static void checkOut(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        if (!order.hasItems()){
            throw new GroceryStoreException("cannot check out an empty order");
        }
        System.out.print(order.getStatusFullName());
        if (!Objects.equals(order.getStatusFullName(), "under_construction")){
            throw new GroceryStoreException("order has already been checked out");
        } else {
            order.checkout();
        }
    }


    public static void payForOrder(int orderNumber, boolean usePoints) throws GroceryStoreException {

    }


    public static void assignOrderToEmployee(int orderNumber, String employeeUsername) throws GroceryStoreException {

    }

    /**
     * Marks the assembly of an order as finished. Moves from Assembling -> ReadyForDelivery.
     *
     * @param orderNumber The number of the order.
     * @throws GroceryStoreException If finishing assembly fails (wrong state, date/perishable conflict).
     */
    public static void finishOrderAssembly(int orderNumber) throws GroceryStoreException {

    }

    /**
     * Marks an order as delivered. Moves from ReadyForDelivery -> Delivered.
     *
     * @param orderNumber The number of the order.
     * @throws GroceryStoreException If delivery fails (wrong state, delivery date not today).
     */
    public static void deliverOrder(int orderNumber) throws GroceryStoreException {

    }

    /**
     * Cancels an order. Moves from Checkout, Pending, or OrderPlaced to Cancelled.
     *
     * @param orderNumber The number of the order to cancel.
     * @throws GroceryStoreException If cancellation fails (wrong state).
     */
    public static void cancelOrder(int orderNumber) throws GroceryStoreException {

    }
}