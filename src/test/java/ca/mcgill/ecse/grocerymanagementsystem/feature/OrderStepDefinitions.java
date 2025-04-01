package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController; // Import OrderController
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.OrderItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderStepDefinitions extends StepDefinitions {

	private Map<String, Integer> orderIdMap = new HashMap<>(); // Map string IDs to order numbers
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date format

	@Before
	public void before() {
		super.before();
		orderIdMap.clear(); // Clear the orderIdMap before each scenario
	}

	@Given("the following orders exist in the system")
	public void the_following_orders_exist_in_the_system(List<Map<String, String>> orders) {
		GroceryManagementSystem system = getSystem();
		for (Map<String, String> orderData : orders) {
			String id = orderData.get("id");
			String datePlacedString = orderData.get("datePlaced");
			String deadlineString = orderData.get("deadline");
			String customerUsername = orderData.get("customer");

			java.sql.Date datePlaced = null;
			if (datePlacedString != null && !datePlacedString.equals("NULL")) {
				try {
					java.util.Date parsedDate = dateFormat.parse(datePlacedString);
					datePlaced = new java.sql.Date(parsedDate.getTime());
				} catch (ParseException e) {
					throw new RuntimeException("Error parsing date: " + datePlacedString, e);
				}
			}

			DeliveryDeadline deadline = DeliveryDeadline.valueOf(deadlineString);

			// Use helper method
			Customer customer = findCustomerByUsername(customerUsername); 
			if (customer == null) {
				throw new IllegalArgumentException("Customer does not exist: " + customerUsername);
			}
			Order order = new Order(datePlaced, deadline, system, customer); 
			system.addOrder(order);
			orderIdMap.put(id, system.indexOfOrder(order));
		}
	}
	private Customer findCustomerByUsername(String username) {
		GroceryManagementSystem system = getSystem();
		for (Customer customer : system.getCustomers()) {
			if (customer.getUser().getUsername().equals(username)) {
				return customer;
			}
		}
		return null;
	}

	@Given("the following items are part of orders")
	public void the_following_items_are_part_of_orders(List<Map<String, String>> orderItems) {
		GroceryManagementSystem system = getSystem();
		List<String> orderids = new ArrayList<String>();
		for (Map<String, String> orderItemData : orderItems) {
			String orderId = orderItemData.get("order");
			String itemName = orderItemData.get("item");
			String quantityStr = orderItemData.get("quantity");
			int quantity = Integer.parseInt(quantityStr);

			boolean found = false;
			for (String ids : orderids) {
				if (Objects.equals(ids, orderId)){
					found = true;
				}
			}
			if (!found){
				orderids.add(orderId);
			}
			Item item = Item.getWithName(itemName);
			Order order = system.getOrder(orderids.indexOf(orderId));
			if (item != null && order != null) {
				new OrderItem(quantity, system, order, item); // Create the association class, include system in constructor
			} else {
				// Handle cases where item or order is not found (should not happen with valid test data)
				throw new IllegalArgumentException("Invalid item or order ID in 'the_following_items_are_part_of_orders'");
			}
		}
	}



	@When("{string} attempts to create an order with deadline {string}")
	public void attempts_to_create_an_order_with_deadline(String username, String deadline) {
		try {
			if (!(Objects.equals(deadline, "NULL"))){
				DeliveryDeadline deliveryDeadline = DeliveryDeadline.valueOf(deadline);
				OrderController.createOrder(username, deliveryDeadline); 
				error = null;
			}
			else {
				DeliveryDeadline deliveryDeadline = null;
				OrderController.createOrder(username, deliveryDeadline); 
				error = null;
			}

		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to delete the order with ID {string}")
	public void the_user_attempts_to_delete_the_order_with_id(String id) {
		try {
			Integer orderNumber = orderIdMap.get(id);
			OrderController.deleteOrder(orderNumber); // Call OrderController
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to delete the non-existent order with order number {int}")
	public void the_user_attempts_to_delete_the_non_existent_order_with_order_number(Integer orderNumber) {
		try {
			OrderController.deleteOrder(orderNumber); // Call OrderController
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to add item {string} to the order with ID {string}")
	public void the_user_attempts_to_add_item_to_the_order_with_id(String item, String orderId) {
		try {
			Integer orderNumber = orderIdMap.get(orderId);
			OrderController.addItemToOrder(orderNumber, item); // Call OrderController
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to add item {string} to the non-existent order with order number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_order_with_order_number(String item, Integer orderNumber) {
		try {
			OrderController.addItemToOrder(orderNumber, item); // Call OrderController
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to set the quantity of item {string} in the order with ID {string} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_order_with_id_to(String item, String orderId, int newQuantity) {
		try {
			Integer orderindex = orderIdMap.get(orderId);
			OrderController.updateQuantityInOrder(orderindex, item, newQuantity);
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to set the quantity of item {string} in the non-existent order {int} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_nonexistent_order_to(String item, int orderNumber, int newQuantity) {
		try {
			OrderController.updateQuantityInOrder(orderNumber, item, newQuantity); 
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@Then("the total number of orders shall be {int}")
	public void the_total_number_of_orders_shall_be(Integer n) {
		GroceryManagementSystem system = getSystem();
		assertEquals(n.intValue(), system.getOrders().size());
	}

	@Then("{string} shall have a new order") 
	public void shall_have_a_new_order(String username) {
		GroceryManagementSystem system = getSystem();
		Customer customer = findCustomerByUsername(username);
		assertNotNull(customer);
		int orderCount = 0;
		for(Order order : system.getOrders()){//count how many orders has this customer placed
			if(order.getOrderPlacer().equals(customer)){
				orderCount++;
			}
		}
		assertEquals(customer.getOrdersPlaced().size(), orderCount); // Assuming only creating one order at a time
	}

	@Then("an order shall exist with ID {string}")
	public void an_order_shall_exist_with_id(String id) {
		Integer orderNumber = orderIdMap.get(id);
		assertNotNull(orderNumber, "Order ID " + id + " not found in map");
		GroceryManagementSystem system = getSystem();
		Order order;
		if (orderNumber < 0 || orderNumber >= system.numberOfOrders()){
			order = null;
		}
		else {
			order = system.getOrder(orderNumber);
		}
		assertNull(order, "Order with ID " + id + " does not exist in the system");
	}

	@Then("no order shall exist with ID {string}")
	public void no_order_shall_exist_with_id(String id) {
		Integer orderNumber = orderIdMap.get(id);
		if (orderNumber != null) { 
			GroceryManagementSystem system = getSystem();
			Order order;
			if (system.numberOfOrders()<orderNumber+1){
				order = null;
			}
			else {
				order = system.getOrder(orderNumber);
			}
			assertNull(order, "Order with ID " + id + " should not exist");
		}
	}

	@Then("no order shall exist with order number {int}")
	public void no_order_shall_exist_with_order_number(Integer orderNumber) {
		GroceryManagementSystem system = getSystem();
		Order order;
		if (system.numberOfOrders()<orderNumber+1){
			order = null;
		}
		else {
			order = system.getOrder(orderNumber);
		}
		assertNull(order, "Order with number " + orderNumber + " should not exist");
	}

	@Then("the newly-created order shall have deadline {string}")
	public void the_newly_created_order_shall_have_deadline(String deadline) {
		GroceryManagementSystem system = getSystem();
		Order latestOrder = system.getOrders().get(system.getOrders().size() - 1); 
		assertNotNull(latestOrder);
		assertEquals(DeliveryDeadline.valueOf(deadline), latestOrder.getDeadline());
	}

	@Then("the newly-created order shall have {int} items")
	public void the_newly_created_order_shall_have_items(Integer n) {
		GroceryManagementSystem system = getSystem();
		Order latestOrder = system.getOrders().get(system.getOrders().size() - 1); 
		assertNotNull(latestOrder);
		assertEquals(n.intValue(), latestOrder.getOrderItems().size());
	}

	@Then("the newly-created order shall not have been placed")
	public void the_newly_created_order_shall_not_have_been_placed() {
		GroceryManagementSystem system = getSystem();
		Order latestOrder = system.getOrders().get(system.getOrders().size() - 1); 
		assertNotNull(latestOrder);
		assertNull(latestOrder.getDatePlaced()); 
	}

	@Then("the order with ID {string} shall include {int} {string}")
	public void the_order_with_id_shall_include(String orderId, Integer quantity, String item) {
		GroceryManagementSystem system = getSystem();
		Order order = system.getOrder(orderIdMap.get(orderId)); // Use orderIdMap and getOrder
		assertNotNull(order, "Order with ID " + orderId + " not found");

		boolean found = false;
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(item)) {
				assertEquals(quantity.intValue(), orderItem.getQuantity(),
						"Quantity mismatch for item " + item + " in order " + orderId);
				found = true;
				break;
			}
		}
		assertTrue(found, "Item " + item + " not found in order " + orderId);
	}

	@Then("the order with ID {string} shall not include any items called {string}")
	public void the_order_with_id_shall_not_include_any_items_called(String orderId, String item) {
		GroceryManagementSystem system = getSystem();
		System.out.println(orderIdMap);
		int ordernum = orderIdMap.get(orderId);
		System.out.print(ordernum);
		Order order = system.getOrder(ordernum);
		assertNotNull(order, "Order with ID " + orderId + " not found");

		for (OrderItem orderItem : order.getOrderItems()) {
			assertFalse(orderItem.getItem().getName().equals(item),
					"Item " + item + " should not be in order " + orderId);
		}
	}

	@Then("the order with ID {string} shall include {int} distinct items")
	public void the_order_with_id_shall_include_distinct_items(String orderId, Integer n) {
		GroceryManagementSystem system = getSystem();
		Order order = system.getOrder(orderIdMap.get(orderId));
		assertNotNull(order, "Order with ID " + orderId + " not found");
		assertEquals(n.intValue(), order.getOrderItems().size(),
				"Number of distinct items mismatch in order " + orderId);
	}

	@Then("the order with ID {string} shall include {int} distinct item")
	public void the_order_with_id_shall_include_distinct_item(String orderId, Integer n) {
		GroceryManagementSystem system = getSystem();
		Order order = system.getOrder(orderIdMap.get(orderId));
		assertNotNull(order, "Order with ID " + orderId + " not found");
		assertEquals(n.intValue(), order.getOrderItems().size(),
				"Number of distinct items mismatch in order " + orderId);
	}
}
