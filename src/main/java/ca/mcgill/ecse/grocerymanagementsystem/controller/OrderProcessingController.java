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
        if (!Objects.equals(order.getStatusFullName(), "under_construction")){
            throw new GroceryStoreException("order has already been checked out");
        } else {
            order.checkout();
        }
    }


    public static void payForOrder(int orderNumber, boolean usePoints) throws GroceryStoreException {
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        Order order = findOrderByOrderNumber(orderNumber);
        List<OrderItem> oi = order.getOrderItems();
        for (OrderItem orderItem : oi){
            Item item = orderItem.getItem();
            if (item.getQuantityInInventory() < orderItem.getQuantity()){
                throw new GroceryStoreException("insufficient stock of item \"" + orderItem.getItem().getName() + "\"" );
            }
        }
        if (Objects.equals(order.getStatusFullName(), "under_construction")){
            throw new GroceryStoreException("cannot pay for an order which has not been checked out");
        } else if (Objects.equals(order.getStatusFullName(), "cancelled")) {
            throw new GroceryStoreException("cannot pay for an order which has been cancelled");
        } else if (!Objects.equals(order.getStatusFullName(), "pending")) {
            throw new GroceryStoreException("cannot pay for an order which has already been paid for");
        }
        if (usePoints){
            order.payOrder(order.getTotalCost(), order.getOrderPlacer().getNumberOfPoints());
        }else{
            order.payOrder(order.getTotalCost(), 0);
        }

    }



    public static void assignOrderToEmployee(int orderNumber, String employeeUsername) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);

        Employee employeetoassign = findEmployeeByUsername(employeeUsername);

        if (Objects.equals(order.getStatusFullName(), "under_construction") || Objects.equals(order.getStatusFullName(), "pending")){
            throw new GroceryStoreException("cannot assign employee to order that has not been placed");
        } else if (Objects.equals(order.getStatusFullName(), "cancelled")) {
            throw new GroceryStoreException("cannot assign employee to an order that has been cancelled");
        } else if (!Objects.equals(order.getStatusFullName(), "placed") && !Objects.equals(order.getStatusFullName(), "in_preparation")) {
            throw new GroceryStoreException("cannot assign employee to an order that has already been prepared");
        }

        order.assignEmployee(employeetoassign);
        order.setOrderAssignee(employeetoassign);
    }

    /**
     * Marks the assembly of an order as finished. Moves from Assembling -> ReadyForDelivery.
     *
     * @param orderNumber The number of the order.
     * @throws GroceryStoreException If finishing assembly fails (wrong state, date/perishable conflict).
     */
    public static void finishOrderAssembly(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        for (OrderItem orderit : order.getOrderItems()) {
            if(orderit.getItem().getIsPerishable()) {
                LocalDate today = LocalDate.now();
                int dead = -4;
                int datep = 4;
                if (order.getDeadline() == Order.DeliveryDeadline.SameDay) {
                    dead = 0;
                } else if (order.getDeadline() == Order.DeliveryDeadline.InOneDay) {
                    dead = 1;
                } else if (order.getDeadline() == Order.DeliveryDeadline.InTwoDays) {
                    dead = 2;
                } else if (order.getDeadline() == Order.DeliveryDeadline.InThreeDays) {
                    dead = 3;
                }
                if (Objects.equals(order.getDatePlaced().toLocalDate(), today)) {
                    datep = 0;
                } else if (Objects.equals(order.getDatePlaced().toLocalDate(), today.minusDays(1))) {
                    datep = -1;
                } else if (Objects.equals(order.getDatePlaced().toLocalDate(), today.minusDays(2))) {
                    datep = -2;
                } else if (Objects.equals(order.getDatePlaced().toLocalDate(), today.minusDays(3))) {
                    datep = -3;
                }
                int delay = datep + dead;
                if (delay > 0) {
                    throw new GroceryStoreException("cannot finish assembling an order with perishable items before the deadline");
                }
            }
        }
        if (Objects.equals(order.getStatusFullName(), "ready_for_delivery") || Objects.equals(order.getStatusFullName(), "delivered")){
            throw new GroceryStoreException("cannot finish assembling order that has already been assembled");
        } else if (Objects.equals(order.getStatusFullName(), "under_construction") || Objects.equals(order.getStatusFullName(), "pending") || Objects.equals(order.getStatusFullName(), "placed")) {
            throw new GroceryStoreException("cannot finish assembling order because it has not been assigned to an employee");
        } else if (Objects.equals(order.getStatusFullName(), "cancelled")) {
            throw new GroceryStoreException("cannot finish assembling order because it was cancelled");
        }
        order.finishAssembly();
    }

    /**
     * Marks an order as delivered. Moves from ReadyForDelivery -> Delivered.
     *
     * @param orderNumber The number of the order.
     * @throws GroceryStoreException If delivery fails (wrong state, delivery date not today).
     */
    public static void deliverOrder(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        if (!Objects.equals(order.getStatusFullName(), "ready_for_delivery")){
            throw new GroceryStoreException("cannot mark an order as delivered if it is not ready for delivery");
        }
        for (OrderItem orderit : order.getOrderItems()) {
            if(orderit.getItem().getIsPerishable()) {
                LocalDate today = LocalDate.now();
                int dead = -4;
                int datep = 4;
                if (order.getDeadline() == Order.DeliveryDeadline.SameDay) {
                    dead = 0;
                } else if (order.getDeadline() == Order.DeliveryDeadline.InOneDay) {
                    dead = 1;
                } else if (order.getDeadline() == Order.DeliveryDeadline.InTwoDays) {
                    dead = 2;
                } else if (order.getDeadline() == Order.DeliveryDeadline.InThreeDays) {
                    dead = 3;
                }
                if (order.getDatePlaced() != null) {
                    if (Objects.equals(order.getDatePlaced().toLocalDate(), today)) {
                        datep = 0;
                    } else if (Objects.equals(order.getDatePlaced().toLocalDate(), today.minusDays(1))) {
                        datep = -1;
                    } else if (Objects.equals(order.getDatePlaced().toLocalDate(), today.minusDays(2))) {
                        datep = -2;
                    } else if (Objects.equals(order.getDatePlaced().toLocalDate(), today.minusDays(3))) {
                        datep = -3;
                    }
                }
                int delay = datep + dead;
                if (delay > 0) {
                    throw new GroceryStoreException("cannot mark order as delivered before the delivery date");
                }
            }
        }
        order.completeDelivery();
    }



    /**
     * Cancels an order. Moves from Checkout, Pending, or OrderPlaced to Cancelled.
     *
     * @param orderNumber The number of the order to cancel.
     * @throws GroceryStoreException If cancellation fails (wrong state).
     */
    public static void cancelOrder(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        if (Objects.equals(order.getStatusFullName(), "cancelled")){
            throw new GroceryStoreException("order was already cancelled");
        } else if (!Objects.equals(order.getStatusFullName(), "under_construction") && !Objects.equals(order.getStatusFullName(), "pending") && !Objects.equals(order.getStatusFullName(), "placed")) {
            throw new GroceryStoreException("cannot cancel an order that has already been assigned to an employee");
        }
        if (Objects.equals(order.getStatusFullName(), "under_construction")){
            checkOut(orderNumber);
            order.cancel();
            for (OrderItem orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem();
                int quantity = orderItem.getQuantity();
                item.setQuantityInInventory(item.getQuantityInInventory() - quantity);
            }
        } else if (Objects.equals(order.getStatusFullName(), "pending")) {
            order.cancel();
            for (OrderItem orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem();
                int quantity = orderItem.getQuantity();
                item.setQuantityInInventory(item.getQuantityInInventory() - quantity);
            }
        }else
            order.cancel();
    }
}