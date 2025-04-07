package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;


public class OrderProcessingController {

    private static Order findOrderByOrderNumber(int orderNumber) throws GroceryStoreException {
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        for (Order order : system.getOrders()) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
    }

    private static Employee findEmployeeByUsername(String username) throws GroceryStoreException {
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        User user = User.getWithUsername(username);
        if (user == null) {
            throw new GroceryStoreException("there is no user with username \"" + username + "\"");
        }
        for (UserRole role : user.getRoles()) {
            if (role instanceof Employee) {
                return (Employee) role;
            }
        }
        throw new GroceryStoreException("\"" + username + "\" is not an employee");
    }

    public static void checkOut(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        Order.Status initialStatus = order.getStatus();

        if (initialStatus != Order.Status.Cart) {
            if (initialStatus == Order.Status.Checkout || initialStatus == Order.Status.Pending ||
                    initialStatus == Order.Status.OrderPlaced || initialStatus == Order.Status.Assembling ||
                    initialStatus == Order.Status.ReadyForDelivery || initialStatus == Order.Status.Delivered||
                    initialStatus== Order.Status.Cancelled) {

                throw new GroceryStoreException("order has already been checked out");
            } else if (initialStatus == Order.Status.Cancelled) {
                throw new GroceryStoreException("cannot check out an order which has been cancelled");
            } else if(order.hasItems()){ // Idle
                throw new GroceryStoreException("order must contain items before checking out");
            }
        }

        boolean costCalculated = order.calculateCost();
        if (!costCalculated) {
            throw new GroceryStoreException("Internal error: Failed to calculate cost from Cart state for order " + orderNumber);
        }

        boolean finalized = order.finalizeOrder();
        if (!finalized) {
            if (!order.hasItems()) {
                // Cannot check out empty order
                throw new GroceryStoreException("cannot check out an empty order");
            } else {
                // Unknown reason if items exist but finalize failed
                throw new GroceryStoreException("Internal error: Failed to finalize order from Checkout state for order " + orderNumber);
            }
        }
    }

    public static void payForOrder(int orderNumber, boolean usePoints) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        Order.Status initialStatus = order.getStatus();

        if (initialStatus != Order.Status.Pending) {
            if (initialStatus == Order.Status.Cart || initialStatus == Order.Status.Checkout || initialStatus == Order.Status.Idle) {
                throw new GroceryStoreException("cannot pay for an order which has not been checked out");
            } else if (initialStatus == Order.Status.OrderPlaced || initialStatus == Order.Status.Assembling ||
                    initialStatus == Order.Status.ReadyForDelivery || initialStatus == Order.Status.Delivered) {
                throw new GroceryStoreException("cannot pay for an order which has already been paid for");
            } else if (initialStatus == Order.Status.Cancelled) {
                throw new GroceryStoreException("cannot pay for an order which has been cancelled");
            } else {
                throw new GroceryStoreException("Cannot pay for order in the current state: " + initialStatus);
            }
        }

        String outOfStockItem = null;
        // Check for out-of-stock items
        for (OrderItem oi : order.getOrderItems()) {
            if (oi.getItem().getQuantityInInventory() < oi.getQuantity()) {
                outOfStockItem = oi.getItem().getName();
                break;
            }
        }
        if (outOfStockItem != null) {
            throw new GroceryStoreException("insufficient stock of item \"" + outOfStockItem + "\"");
        }

        int pointsToUse = 0;
        double amountToPay = 0.0;
        int totalCostCents = order.getTotalCost();

        if (usePoints) {
            Customer customer = order.getOrderPlacer();
            if (customer != null) {
                pointsToUse = Math.min(customer.getNumberOfPoints(), totalCostCents);
                amountToPay = (double)(totalCostCents - pointsToUse) / 100.0;
            } else {
                amountToPay = (double)totalCostCents / 100.0;
            }
        } else {
            amountToPay = (double)totalCostCents / 100.0;
            pointsToUse = 0;
        }

        // Round to two decimal places to avoid any floating point precision errors
        amountToPay = Math.round(amountToPay * 100.0) / 100.0;

        boolean paid = order.payOrder(amountToPay, pointsToUse);

        if (!paid) {
            // Check again for out-of-stock items after attempting to pay
            outOfStockItem = null;
            for (OrderItem oi : order.getOrderItems()) {
                if (oi.getItem().getQuantityInInventory() < oi.getQuantity()) {
                    outOfStockItem = oi.getItem().getName();
                    break;
                }
            }
            if (outOfStockItem != null) {
                throw new GroceryStoreException("insufficient stock of item \"" + outOfStockItem + "\"");
            }

            throw new GroceryStoreException("Payment failed for order " + orderNumber + ". Ensure payment amount/points are valid.");
        }

        // If payment succeeds, ensure the order's points and cost are correctly recorded
        if (order.getStatus() == Order.Status.Pending) {
            throw new GroceryStoreException("Order could not be processed properly after payment.");
        }
    }




    public static void assignOrderToEmployee(int orderNumber, String employeeUsername) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        Employee employee = findEmployeeByUsername(employeeUsername);
        Order.Status initialStatus = order.getStatus();

        if (initialStatus == Order.Status.OrderPlaced || initialStatus == Order.Status.Assembling) {
            order.setOrderAssignee(employee);
            order.assignEmployee(employee);

            if (initialStatus == Order.Status.OrderPlaced) {
                boolean assigned = order.assignEmployee(employee);
                if (!assigned && order.getOrderAssignee() != employee) {
                    throw new GroceryStoreException("Internal error during assignment state transition for order " + orderNumber);
                }
            }
        } else {
            if (initialStatus == Order.Status.Cart || initialStatus == Order.Status.Checkout || initialStatus == Order.Status.Pending || initialStatus == Order.Status.Idle) {
                throw new GroceryStoreException("cannot assign employee to order that has not been placed");
            } else if (initialStatus == Order.Status.ReadyForDelivery || initialStatus == Order.Status.Delivered) {
                throw new GroceryStoreException("cannot assign employee to an order that has already been prepared");
            } else if (initialStatus == Order.Status.Cancelled) {
                throw new GroceryStoreException("cannot assign employee to an order that has been cancelled");
            } else {
                throw new GroceryStoreException("Cannot assign employee in the current order state: " + initialStatus);
            }
        }
    }

    public static void finishOrderAssembly(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        Order.Status initialStatus = order.getStatus();

        if (initialStatus != Order.Status.Assembling) {
            if (order.getOrderAssignee() == null &&
                    (initialStatus == Order.Status.OrderPlaced || initialStatus == Order.Status.Pending ||
                            initialStatus == Order.Status.Cart || initialStatus == Order.Status.Checkout || initialStatus == Order.Status.Idle) ) {
                throw new GroceryStoreException("cannot finish assembling order because it has not been assigned to an employee");
            } else if (initialStatus == Order.Status.ReadyForDelivery || initialStatus == Order.Status.Delivered) {
                throw new GroceryStoreException("cannot finish assembling order that has already been assembled");
            } else if (initialStatus == Order.Status.Cancelled) {
                throw new GroceryStoreException("cannot finish assembling order because it was cancelled");
            } else {
                throw new GroceryStoreException("Cannot finish assembly in the current state: " + initialStatus);
            }
        }

        boolean finished = order.finishAssembly();

        if (!finished) {
            throw new GroceryStoreException("cannot finish assembling an order with perishable items before the deadline");
        }
    }

    public static void deliverOrder(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        Order.Status initialStatus = order.getStatus();

        if (initialStatus != Order.Status.ReadyForDelivery) {
            throw new GroceryStoreException("cannot mark an order as delivered if it is not ready for delivery");
        }

        boolean delivered = order.completeDelivery();

        if (!delivered) {
            throw new GroceryStoreException("cannot mark order as delivered before the delivery date");

           }
    }

    public static void cancelOrder(int orderNumber) throws GroceryStoreException {
        Order order = findOrderByOrderNumber(orderNumber);
        Order.Status initialStatus = order.getStatus();

        // Check if the order can be canceled
        if (initialStatus == Order.Status.Assembling || initialStatus == Order.Status.ReadyForDelivery || initialStatus == Order.Status.Delivered) {
            throw new GroceryStoreException("Cannot cancel an order that has already been assigned to an employee");
        } else if (initialStatus == Order.Status.Cancelled) {
            throw new GroceryStoreException("Order was already cancelled");
        }

        if (initialStatus != Order.Status.Checkout && initialStatus != Order.Status.Pending && initialStatus != Order.Status.OrderPlaced) {
            throw new GroceryStoreException("Cannot cancel order in the current state: " + initialStatus);
        }



        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem();
            int quantityInStock = item.getQuantityInInventory();
            int quantityToReturn = orderItem.getQuantity();

            item.setQuantityInInventory(quantityInStock - quantityToReturn);
        }

        // Cancel the order
        boolean cancelled = order.cancel();



        if (!cancelled) {
            throw new GroceryStoreException("Internal error: Failed to cancel order " + orderNumber + " from state " + initialStatus);
        }
    }
}