package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.*; // Import all model classes

import java.util.Objects;

import static ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController.getGroceryManagementSystem;

public class OrderController {

	public static void createOrder(String creatorUsername, DeliveryDeadline deadline) throws GroceryStoreException {
		// Input Validation
		if (creatorUsername == null || creatorUsername.isEmpty()) {
			throw new GroceryStoreException("customer username cannot be empty");
		}
		if (deadline == null) {
			throw new GroceryStoreException("delivery deadline is required");
		}
		if (creatorUsername.equals("NULL")){
			throw new GroceryStoreException("customer is required" );
		}

		GroceryManagementSystem system = getGroceryManagementSystem();
		User user = User.getWithUsername(creatorUsername);
		if (user==null){
			throw new GroceryStoreException("there is no user with username \"" + creatorUsername + "\"" );
		}
		Customer customer = findCustomerByUsername(creatorUsername);

		if (customer == null) {
			throw new GroceryStoreException("\"" + creatorUsername + "\" is not a customer");
		}

		// Create the Order
		new Order(null, deadline, system, customer);
	}
	// Helper method to find a customer
	private static Customer findCustomerByUsername(String username) {
		GroceryManagementSystem system = getGroceryManagementSystem();
		if (Objects.equals(username, "NULL")){
			return null;
		}
		for (Customer customer : system.getCustomers()) {
			if (customer.getUser().getUsername().equals(username)) {
				return customer;
			}
		}
		return null;
	}


	public static void deleteOrder(int orderNumber) throws GroceryStoreException {
		GroceryManagementSystem system = getGroceryManagementSystem();

		// Use the helper method to FIND the order by its orderNumber
		Order orderToDelete = findOrderByOrderNumber(orderNumber);

		// Check if the order was found (findOrderByOrderNumber returns null if not found)
		if (orderToDelete == null) {
			throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
		}
		// Check if the order has already been placed
		if (orderToDelete.getDatePlaced() != null) {
			throw new GroceryStoreException("cannot delete an order which has already been placed");
		}
		// If order exists and is not placed, delete it
		orderToDelete.delete();
	}
	// Helper method to find an order

	private static Order findOrderByOrderNumber(int orderNumber) {
		GroceryManagementSystem system = getGroceryManagementSystem();
		for (Order order : system.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order; // Found the order with matching orderNumber
			}
		}
		return null; // Order with that orderNumber not found
	}

	public static void addItemToOrder(int orderNumber, String itemName) throws GroceryStoreException {
		GroceryManagementSystem system = getGroceryManagementSystem();

		Order order = findOrderByOrderNumber(orderNumber); // Use helper to find by number


		if (order == null) {
			throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
		}

		if (order.getDatePlaced() != null) {
			throw new GroceryStoreException("order has already been placed");
		}

		Item item = Item.getWithName(itemName);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}

		if (item.numberOfOrderItems() <= 0) {
			throw new GroceryStoreException("item \"" + itemName + "\" is out of stock");
		}

		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(item.getName())) {
				throw new GroceryStoreException("order already includes item \"" + itemName + "\"");
			}
		}
		System.out.println(order.getStatus());
		System.out.println(order.getStatus());
		if (!Objects.equals(order.getStatus().toString(), "under_construction")){
			throw new GroceryStoreException("order has already been checked out");
		}

		// Add the item to the order with quantity 1
		new OrderItem(1, system, order, item);
	}


	public static void updateQuantityInOrder(int orderNumber, String itemName, int newQuantity)
			throws GroceryStoreException {
		if (newQuantity < 0) {
			throw new GroceryStoreException("quantity must be non-negative");
		}
		if (newQuantity > 10) { // <-- ADD THIS CHECK: Quantity limit
			throw new GroceryStoreException("quantity cannot exceed 10");
		}
		GroceryManagementSystem system = getGroceryManagementSystem();

		Order order = findOrderByOrderNumber(orderNumber); // Use helper to find by number


		if (order == null) {
			throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
		}
		if (order.getDatePlaced() != null) {
			throw new GroceryStoreException("order has already been placed");
		}

		Item item = Item.getWithName(itemName);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}

		OrderItem orderItem = null;
		for (OrderItem oi : order.getOrderItems()) {
			if (oi.getItem().getName().equals(itemName)) {
				orderItem = oi;
				break;
			}
		}

		if (orderItem == null && newQuantity != 0) {
			throw new GroceryStoreException("order does not include item \"" + itemName + "\"");
		}

		if (!Objects.equals(order.getStatusFullName(), "under_construction")){
			throw new GroceryStoreException("order has already been checked out");
		}

		if (newQuantity == 0 && orderItem != null) {
			orderItem.delete(); // Remove if quantity is 0
		} else if (orderItem != null) {
			orderItem.setQuantity(newQuantity);
		} else {
			new OrderItem(newQuantity, system, order, item);
		}
	}
}