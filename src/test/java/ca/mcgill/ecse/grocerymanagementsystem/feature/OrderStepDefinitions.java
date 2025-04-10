package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController; // Import OrderController
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class OrderStepDefinitions extends StepDefinitions {

	// Map string IDs to order numbers
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date format
	private static Map<Integer, List<Map<String, String>>> itemsForSetup = new HashMap<>();
	public static Map<String, Integer> orderIdMap = new HashMap<>();
	public static Map<String, String> orderStatusMap = new HashMap<>();
	public static Map<String, DeliveryDeadline> orderDeadlineMap = new HashMap<>();

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
			String assignee = orderData.get("assignee");
			String orderStatus = orderData.get("state");

			DeliveryDeadline deadline = DeliveryDeadline.valueOf(deadlineString);

			// --- REPLACEMENT CODE BLOCK ---
			Date datePlaced = null; // Keep this initialization
			LocalDate today = LocalDate.now(); // Add this line before the 'if'
			int datep = 0;
			if (datePlacedString != null && !datePlacedString.equalsIgnoreCase("NULL")) { // Uses equalsIgnoreCase now
				// Check for relative terms FIRST
				switch (datePlacedString.toLowerCase()) { // Converts to lowercase
					case "today":
						datePlaced = Date.valueOf(today);
						datep = 0;
						break;
					case "yesterday":
						datePlaced = Date.valueOf(today.minusDays(1));
						datep = -1;
						break;
					case "two days ago": // Add if used in other scenarios
						datePlaced = Date.valueOf(today.minusDays(2));
						datep = -2;
						break;
					case "three days ago": // Add if used in other scenarios
						datePlaced = Date.valueOf(today.minusDays(3));
						datep = -3;
						break;
					default:
						// Not a relative term, TRY parsing as yyyy-MM-dd (Original Logic)
						try {
							// Use the existing dateFormat field (make sure it's defined in your class)
							java.util.Date parsedUtilDate = dateFormat.parse(datePlacedString);
							datePlaced = new Date(parsedUtilDate.getTime());
						} catch (ParseException e) {
							// This error now only happens if the string is NOT relative AND NOT yyyy-MM-dd
							throw new RuntimeException("Error parsing specific date: '" + datePlacedString + "'. Expected yyyy-MM-dd format.", e);
						}
						break;
				}
			}

// --- END REPLACEMENT CODE BLOCK ---
			int dead;
			if (deadline == DeliveryDeadline.SameDay){
				dead = 0;
			} else if (deadline == DeliveryDeadline.InOneDay) {
				dead = 1;
			} else if (deadline == DeliveryDeadline.InTwoDays) {
				dead = 2;
			}else{
				dead = 3;
			}
			int deliverydelay = datep + dead;
			// Use helper method
			Customer customer = findCustomerByUsername(customerUsername);

			if (customer == null) {
				throw new IllegalArgumentException("Customer does not exist: " + customerUsername);
			}
			Order order = new Order(datePlaced, deadline, system, customer);
			order.setDeliveryDelay(-deliverydelay);
			if (!Objects.equals(assignee, "NULL")){
				order.setOrderAssignee(findEmployeeByUsername(assignee));
			}

			orderIdMap.put(id, order.getOrderNumber());
			orderStatusMap.put(id,orderStatus);
			orderDeadlineMap.put(id, deadline);

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

	@Given("the following items are part of orders")
	public void the_following_items_are_part_of_orders(List<Map<String, String>> orderItems) {
		GroceryManagementSystem system = getSystem();
		for (Map<String, String> orderItemData : orderItems) {
			String orderId = orderItemData.get("order");
			String itemName = orderItemData.get("item");
			String quantityStr = orderItemData.get("quantity");
			int quantity = Integer.parseInt(quantityStr);
			String orderStatus = orderStatusMap.get(orderId);
			DeliveryDeadline deadline = orderDeadlineMap.get(orderId);

			Item item = Item.getWithName(itemName);
			Integer orderNumber = orderIdMap.get(orderId); // Get actual order number using orderIdMap
			if (orderNumber == null) {
				throw new IllegalArgumentException("Invalid order ID: " + orderId);
			}
			Order order = findOrderByOrderNumberHelper(orderNumber); // Use helper to find by number
			if (item != null && order != null) {
				new OrderItem(quantity, system, order, item);
			} else {
				throw new IllegalArgumentException("Invalid item or order ID in 'the_following_items_are_part_of_orders'");
			}
		}

		for (Map<String, String> orderItemData : orderItems) {
			String orderId = orderItemData.get("order");
			int ordernum = orderIdMap.get(orderId);

			Order order = findOrderByOrderNumberHelper(ordernum);
			DeliveryDeadline deadline = orderDeadlineMap.get(orderId);


			for (Map.Entry<String, Integer> mapEntry : orderIdMap.entrySet()) {
				String targetStatus = orderStatusMap.get(orderId);


				if (order == null || targetStatus == null || deadline == null) {
					continue; // Skip if essential data is missing
				}

				try {
					// Apply necessary transitions sequentially to reach the target state
					// We rely on the model's event methods to handle guards.
					// If a transition fails (returns false), subsequent ones won't be attempted
					// if the state didn't change as expected.
					if (targetStatus.equalsIgnoreCase("pending")){
						if (order.getStatus() == Order.Status.under_construction){
							order.startOrder(deadline, order.getDeliveryDelay());
							order.checkout();
						}
					} else if (targetStatus.equalsIgnoreCase("placed")) {
						if (order.getStatus() == Order.Status.under_construction){
							order.startOrder(deadline, order.getDeliveryDelay());
							order.checkout();
						}
						int num_saver = order.getOrderPlacer().getNumberOfPoints();
						List<OrderItem> oi = order.getOrderItems();
						List<Integer> oq = new ArrayList<>();
						for (OrderItem orderItem : oi) {
							Item titem = orderItem.getItem();
							oq.add(titem.getQuantityInInventory());
						}

						Date dateplaced1 = order.getDatePlaced();
						if (order.getStatus() == Order.Status.pending)
							if (order.payOrder((order.getTotalCost() / 100.0),order.getOrderPlacer().getNumberOfPoints())){
								order.getOrderPlacer().setNumberOfPoints(num_saver);
								order.setDatePlaced(dateplaced1);

								int counter = 0;
								List<OrderItem> neo_oi = order.getOrderItems();
								for (OrderItem orderItem : neo_oi) {
									Item neoitem = orderItem.getItem();
									neoitem.setQuantityInInventory(oq.get(counter));
									counter += 1;
								}
							}else{
								order.setStatus(Order.Status.placed);
							}


					} else if (targetStatus.equalsIgnoreCase("in preparation")) {
						if (order.getStatus() == Order.Status.under_construction){
							order.startOrder(deadline, order.getDeliveryDelay());
							order.checkout();

						}

						int num_saver = order.getOrderPlacer().getNumberOfPoints();
						List<OrderItem> oi = order.getOrderItems();
						List<Integer> oq = new ArrayList<>();
						for (OrderItem orderItem : oi) {
							Item titem = orderItem.getItem();
							oq.add(titem.getQuantityInInventory());
						}

						Date dateplaced1 = order.getDatePlaced();
						if (order.getStatus() == Order.Status.pending) order.payOrder((order.getTotalCost() / 100.0),order.getOrderPlacer().getNumberOfPoints());
						order.getOrderPlacer().setNumberOfPoints(num_saver);
						order.setDatePlaced(dateplaced1);

						int counter = 0;
						List<OrderItem> neo_oi = order.getOrderItems();
						for (OrderItem orderItem : neo_oi) {
							Item neoitem = orderItem.getItem();
							neoitem.setQuantityInInventory(oq.get(counter));
							counter += 1;
						}

						Employee employee = order.getOrderAssignee();
						order.setOrderAssignee(null);
						if (order.getStatus() == Order.Status.placed) order.assignEmployee(system.getEmployee(0));
						order.setOrderAssignee(employee);
					}
					// Target: Delivered
					else if (targetStatus.equalsIgnoreCase("ready for delivery")) {
						if (order.getStatus() == Order.Status.under_construction){
							order.startOrder(deadline, order.getDeliveryDelay());
							order.checkout();
						}
						int num_saver = order.getOrderPlacer().getNumberOfPoints();
						List<OrderItem> oi = order.getOrderItems();
						List<Integer> oq = new ArrayList<>();
						for (OrderItem orderItem : oi) {
							Item titem = orderItem.getItem();
							oq.add(titem.getQuantityInInventory());
						}

						Date dateplaced1 = order.getDatePlaced();
						if (order.getStatus() == Order.Status.pending) order.payOrder((order.getTotalCost() / 100.0),order.getOrderPlacer().getNumberOfPoints());
						order.getOrderPlacer().setNumberOfPoints(num_saver);
						order.setDatePlaced(dateplaced1);

						int counter = 0;
						List<OrderItem> neo_oi = order.getOrderItems();
						for (OrderItem orderItem : neo_oi) {
							Item neoitem = orderItem.getItem();
							neoitem.setQuantityInInventory(oq.get(counter));
							counter += 1;
						}
						Employee employee = order.getOrderAssignee();
						order.setOrderAssignee(null);
						if (order.getStatus() == Order.Status.placed) order.assignEmployee(employee);
						order.setOrderAssignee(employee);
						System.out.print(order.getDatePlaced() + " ");
						System.out.print(order.getDeadline() + " ");
						System.out.print(order.getDeliveryDelay() + " ");
						System.out.print(order.calculateTargetDeliveryDate()+ " ");

						Date datePlacedt; // Keep this initialization
						LocalDate today = LocalDate.now();
						if (order.getDatePlaced() == null){
							if (order.getDeadline() == DeliveryDeadline.SameDay){
								datePlacedt = Date.valueOf(today);
							} else if (order.getDeadline() == DeliveryDeadline.InOneDay) {
								datePlacedt = Date.valueOf(today.minusDays(1));
							}else if (order.getDeadline() == DeliveryDeadline.InTwoDays) {
								datePlacedt = Date.valueOf(today.minusDays(2));
							}else {
								datePlacedt = Date.valueOf(today.minusDays(3));
							}
							order.setDatePlaced(datePlacedt);
							order.setDeliveryDelay(0);
							if (order.getStatus() == Order.Status.in_preparation) order.finishAssembly();
						}
						if (order.getStatus() == Order.Status.in_preparation) order.finishAssembly();

					}
					else if (targetStatus.equalsIgnoreCase("delivered")) {
						if (order.getStatus() == Order.Status.under_construction){
							order.startOrder(deadline, order.getDeliveryDelay());
							order.checkout();
						}
						int num_saver = order.getOrderPlacer().getNumberOfPoints();
						List<OrderItem> oi = order.getOrderItems();
						List<Integer> oq = new ArrayList<>();
						for (OrderItem orderItem : oi) {
							Item titem = orderItem.getItem();
							oq.add(titem.getQuantityInInventory());
						}

						Date dateplaced1 = order.getDatePlaced();
						if (order.getStatus() == Order.Status.pending) order.payOrder((order.getTotalCost() / 100.0),order.getOrderPlacer().getNumberOfPoints());
						order.getOrderPlacer().setNumberOfPoints(num_saver);
						order.setDatePlaced(dateplaced1);

						int counter = 0;
						List<OrderItem> neo_oi = order.getOrderItems();
						for (OrderItem orderItem : neo_oi) {
							Item neoitem = orderItem.getItem();
							neoitem.setQuantityInInventory(oq.get(counter));
							counter += 1;
						}
						Employee employee = order.getOrderAssignee();
						order.setOrderAssignee(null);
						if (order.getStatus() == Order.Status.placed) order.assignEmployee(employee);
						order.setOrderAssignee(employee);

						Date datePlacedt; // Keep this initialization
						LocalDate today = LocalDate.now();
						if (order.getDatePlaced() == null){
							if (order.getDeadline() == DeliveryDeadline.SameDay){
								datePlacedt = Date.valueOf(today);
							} else if (order.getDeadline() == DeliveryDeadline.InOneDay) {
								datePlacedt = Date.valueOf(today.minusDays(1));
							}else if (order.getDeadline() == DeliveryDeadline.InTwoDays) {
								datePlacedt = Date.valueOf(today.minusDays(2));
							}else {
								datePlacedt = Date.valueOf(today.minusDays(3));
							}
							order.setDatePlaced(datePlacedt);
							order.setDeliveryDelay(0);
							if (order.getStatus() == Order.Status.in_preparation) order.finishAssembly();
						}
						if (order.getStatus() == Order.Status.in_preparation) order.finishAssembly();


						if (order.getStatus() == Order.Status.ready_for_delivery) order.completeDelivery();
					}
					else if (targetStatus.equalsIgnoreCase("cancelled")) {
						if (order.getStatus() == Order.Status.under_construction){
							order.startOrder(deadline, order.getDeliveryDelay());
							order.checkout();
							int num_saver = order.getOrderPlacer().getNumberOfPoints();
							order.payOrder((order.getTotalCost() / 100.0),order.getOrderPlacer().getNumberOfPoints());
							order.getOrderPlacer().setNumberOfPoints(num_saver);
							order.cancel();
						}
					}
					// Add other target states like 'cancelled' if needed for setup
				} catch (RuntimeException e) {
					// Log unexpected errors during setup for debugging
					System.err.println("EXCEPTION during state setup for Order ID '" + orderId + "' to '" + targetStatus + "': " + e.getMessage());
					e.printStackTrace(); // Helps pinpoint the issue
				}
			}

		}


			// --- Part 2: Apply final states AFTER all items are added ---



	}
	private Employee findFirstEmployee(GroceryManagementSystem system) {
		return system.hasEmployees() ? system.getEmployee(0) : null;
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
			Integer orderNumber = orderIdMap.get(id); // Get orderNumber from map
			OrderController.deleteOrder(orderNumber);     // Call controller with orderNumber
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
			Integer orderNumber = orderIdMap.get(orderId);
			OrderController.updateQuantityInOrder(orderNumber, item, newQuantity);
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
		// 1. Get the ACTUAL order number associated with 'id' from the map
		Integer orderNumber = orderIdMap.get(id);

		// 2. Check if the ID was even in the map to begin with (test setup validation)
		assertNotNull(orderNumber, "Test setup issue: Order ID '" + id + "' was not found in orderIdMap.");

		// 3. Use the helper method to SEARCH for an order with this ACTUAL order number
		Order foundOrder = findOrderByOrderNumberHelper(orderNumber);

		// 4. Assert that the helper method returned NOT NULL, meaning an order WAS found
		assertNotNull(foundOrder, "Order with actual number " + orderNumber + " (ID '" + id + "') should exist, but was not found.");
	}

	@Then("no order shall exist with ID {string}")
	public void no_order_shall_exist_with_id(String id) {
		Integer targetOrderNumber = orderIdMap.get(id);
		assertNotNull(targetOrderNumber, "Test setup issue: ID '" + id + "' was not found in orderIdMap.");
		// *** Use the helper method here ***
		Order foundOrder = findOrderByOrderNumberHelper(targetOrderNumber);
		assertNull(foundOrder, "Order with actual number " + targetOrderNumber + " (ID '" + id + "') should NOT exist...");
	}

	// Helper method to find an order by its ACTUAL order number
	private Order findOrderByOrderNumberHelper(int orderNumber) {
		GroceryManagementSystem system = getSystem();
		for (Order order : system.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}
		}
		return null;
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
		Integer orderNumber = orderIdMap.get(orderId); // Get order number from map
		Order order = findOrderByOrderNumberHelper(orderNumber); // Use helper to find by number
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
		Integer orderNumber = orderIdMap.get(orderId); // Get order number from map
		Order order = findOrderByOrderNumberHelper(orderNumber); // Use helper to find by number
		assertNotNull(order, "Order with ID " + orderId + " not found");

		for (OrderItem orderItem : order.getOrderItems()) {
			assertFalse(orderItem.getItem().getName().equals(item),
					"Item " + item + " should not be in order " + orderId);
		}
	}

	@Then("the order with ID {string} shall include {int} distinct items")
	public void the_order_with_id_shall_include_distinct_items(String orderId, Integer n) {
		GroceryManagementSystem system = getSystem();
		Integer orderNumber = orderIdMap.get(orderId); // Get order number from map
		Order order = findOrderByOrderNumberHelper(orderNumber); // Use helper to find by number
		assertNotNull(order, "Order with ID " + orderId + " not found");
		assertEquals(n.intValue(), order.getOrderItems().size(),
				"Number of distinct items mismatch in order " + orderId);
	}

	@Then("the order with ID {string} shall include {int} distinct item")
	public void the_order_with_id_shall_include_distinct_item(String orderId, Integer n) {
		GroceryManagementSystem system = getSystem();
		Integer orderNumber = orderIdMap.get(orderId); // Get order number from map
		Order order = findOrderByOrderNumberHelper(orderNumber); // Use helper to find by number
		assertNotNull(order, "Order with ID " + orderId + " not found");
		assertEquals(n.intValue(), order.getOrderItems().size(),
				"Number of distinct items mismatch in order " + orderId);
	}

}