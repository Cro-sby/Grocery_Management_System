package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;


import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.Date;
import java.util.Map;


public class OrderProcessingStepDefinitions extends StepDefinitions {
	private static Integer lastAffectedOrderNumber = null;
	public Map<String, Integer> orderIdMap = OrderStepDefinitions.orderIdMap;


	@Before
	public void before() {
		super.before();
		orderIdMap.clear();
	}


	// Helper to find Order by number
	private Order findOrderByOrderNumberHelper(int orderNumber) {
		GroceryManagementSystem system = getSystem();
		for (Order order : system.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}
		}
		return null;
	}

	private String translateFeatureStateToModelState(String featureState) {
		switch (featureState) {
			case "Cart","Checkout":
				return "under construction";
            case "Pending":
				return "pending";
			case "OrderPlaced":
				return "placed";
			case "Delivered":
				return "delivered";
			case "Cancelled":
				return "cancelled";
			case "Assembling":
				return "in preparation";
			default:
				return "weird ting";
		}
	}


	@When("the user attempts to check out the order with ID {string}")
	public void the_user_attempts_to_check_out_the_order_with_id(String orderId) {
		Integer orderNumber = orderIdMap.get(orderId);
		assertNotNull(orderNumber, "Test setup error: Order ID '" + orderId + "' not found in map.");
		lastAffectedOrderNumber = orderNumber;
		try {
			OrderProcessingController.checkOut(orderNumber);
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to pay for the order with ID {string} {string} their points")
	public void the_user_attempts_to_pay_for_the_order_with_id_without_using_their_points(String orderId, String usingOrWithoutUsing) {
		Integer orderNumber = orderIdMap.get(orderId);
		assertNotNull(orderNumber, "Test setup error: Order ID '" + orderId + "' not found in map.");
		lastAffectedOrderNumber = orderNumber;
		boolean usePoints = usingOrWithoutUsing.equals("using");
		try {
			OrderProcessingController.payForOrder(orderNumber, usePoints);
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to assign the order with ID {string} to {string}")
	public void the_manager_attempts_to_assign_the_order_with_id_to(String orderId, String employeeUsername) {
		Integer orderNumber = orderIdMap.get(orderId);
		assertNotNull(orderNumber, "Test setup error: Order ID '" + orderId + "' not found in map.");
		lastAffectedOrderNumber = orderNumber;
		try {
			OrderProcessingController.assignOrderToEmployee(orderNumber, employeeUsername);
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to indicate that assembly of the order with ID {string} is finished")
	public void the_user_attempts_to_indicate_that_assembly_of_the_order_with_id_is_finished(String orderId) {
		Integer orderNumber = orderIdMap.get(orderId);
		assertNotNull(orderNumber, "Test setup error: Order ID '" + orderId + "' not found in map.");
		lastAffectedOrderNumber = orderNumber;
		try {
			OrderProcessingController.finishOrderAssembly(orderNumber);
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to cancel the order with ID {string}")
	public void the_user_attempts_to_cancel_the_order_with_id(String orderId) {
		Integer orderNumber = orderIdMap.get(orderId);
		assertNotNull(orderNumber, "Test setup error: Order ID '" + orderId + "' not found in map.");
		lastAffectedOrderNumber = orderNumber;
		try {
			OrderProcessingController.cancelOrder(orderNumber);
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to mark the order with ID {string} as delivered")
	public void the_manager_attempts_to_mark_the_order_with_id_as_delivered(String orderId) {
		Integer orderNumber = orderIdMap.get(orderId);
		assertNotNull(orderNumber, "Test setup error: Order ID '" + orderId + "' not found in map.");
		lastAffectedOrderNumber = orderNumber;
		try {
			OrderProcessingController.deliverOrder(orderNumber);
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	// ========== @Then Steps ==========

	@Then("the order shall be {string}")
	public void the_order_shall_be(String expectedState) {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");
		System.out.println(order.getStatus().toString());
		assertEquals(expectedState.toLowerCase(), translateFeatureStateToModelState(order.getStatus().toString()), "Order state mismatch.");
	}

	@Then("the order's placer shall be {string}")
	public void the_order_s_placer_shall_be(String customerUsername) {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");
		assertNotNull(order.getOrderPlacer(), "Order placer should not be null");
		assertNotNull(order.getOrderPlacer().getUser(), "Order placer's user should not be null");
		assertEquals(customerUsername, order.getOrderPlacer().getUser().getUsername());
	}

	@Then("the order's assignee shall be {string}")
	public void the_order_s_assignee_shall_be(String employeeUsername) {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");
		if (employeeUsername.equals("NULL")) {
			assertNull(order.getOrderAssignee(), "Order assignee should be null.");
		} else {
			assertNotNull(order.getOrderAssignee(), "Order assignee should not be null.");
			assertNotNull(order.getOrderAssignee().getUser(), "Order assignee's user should not be null.");
			assertEquals(employeeUsername, order.getOrderAssignee().getUser().getUsername());
		}
	}

	@Then("the order's date placed shall be today")
	public void the_order_s_date_placed_shall_be_today() {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");

		// Get today's date
		Date today = new Date(System.currentTimeMillis());

		// Compare the string representations of the dates
		assertEquals(today.toString(), order.getDatePlaced().toString(), "Order date placed does not match today's date.");
	}

	@Then("the total cost of the order shall be {int} cents")
	public void the_total_cost_of_the_order_shall_be_cents(Integer expectedCost) {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");
		assertEquals(expectedCost, order.getTotalCost());
	}

	@Then("the final cost of the order, after considering points, shall be {int} cents")
	public void the_final_cost_of_the_order_after_considering_points_shall_be_cents(Integer expectedCost) {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");

		// Calculate the total cost of items in the order
		int totalItemCost = order.getPricePaid();
		for (OrderItem item : order.getOrderItems()) {
			totalItemCost += item.getItem().getPrice() * item.getQuantity();
		}


		// Deduct points if the customer used them (if using points)
// POINTS to deduct
		int pointsToDeduct = totalItemCost - expectedCost;
		if (expectedCost < totalItemCost) {
			totalItemCost -= pointsToDeduct; // Deduct points
		}

		assertEquals(expectedCost, totalItemCost, "Final cost calculation mismatch.");
	}
}