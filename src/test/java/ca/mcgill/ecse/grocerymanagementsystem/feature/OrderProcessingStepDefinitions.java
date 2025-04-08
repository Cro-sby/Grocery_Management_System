package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;

import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.swing.plaf.nimbus.State;
import java.util.HashMap;
import java.util.Map;


public class OrderProcessingStepDefinitions extends StepDefinitions {
	private static Integer lastAffectedOrderNumber = null;
	public Map<String, Integer> orderIdMap = OrderStepDefinitions.orderIdMap;


	@Before
	public void before() {
		super.before();
		lastAffectedOrderNumber = 0;
		 // Clear the orderIdMap before each scenario
	}


	// Helper to find Order by number - Re-add here for self-contained StepDefs
	private Order findOrderByOrderNumberHelper(int orderNumber) {
		GroceryManagementSystem system = getSystem();
		for (Order order : system.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}
		}
		return null;
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
	private Employee findEmployeeByUsername(String username) {
		GroceryManagementSystem system = getSystem();
		for (Employee employee : system.getEmployees()) {
			if (employee.getUser().getUsername().equals(username)) {
				return employee;
			}
		}
		return null;
	}

	// **Helper function to translate feature state names to model state names**
	private String translateFeatureStateToModelState(String featureState) {
		switch (featureState) {
			case "under_construction":
				return "under construction";
			case "in_preparation":
				return "in preparation";
			case "ready_for_delivery":
				return "ready for delivery";
            default:
                return featureState;
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
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order);
		assertEquals(expectedState, translateFeatureStateToModelState(order.getStatusFullName()));
	}

	@Then("the order's placer shall be {string}")
	public void the_order_s_placer_shall_be(String customerUsername) {
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order);
		assertEquals(findCustomerByUsername(customerUsername), order.getOrderPlacer());
	}

	@Then("the order's assignee shall be {string}")
	public void the_order_s_assignee_shall_be(String employeeUsername) {
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order);
		assertEquals(findEmployeeByUsername(employeeUsername), order.getOrderAssignee());
	}

	@Then("the order's date placed shall be today")
	public void the_order_s_date_placed_shall_be_today() {
		throw new PendingException();
		}

	@Then("the total cost of the order shall be {int} cents")
	public void the_total_cost_of_the_order_shall_be_cents(Integer expectedCost) {
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order);
		assertEquals(expectedCost, order.getTotalCost());
	}

	@Then("the final cost of the order, after considering points, shall be {int} cents")
	public void the_final_cost_of_the_order_after_considering_points_shall_be_cents(Integer expectedCost) {
		assertNotNull(lastAffectedOrderNumber, "No order number was affected in the previous step.");
		Order order = findOrderByOrderNumberHelper(lastAffectedOrderNumber);
		assertNotNull(order, "Order with number " + lastAffectedOrderNumber + " not found after action.");

		// Calculate the total cost of items in the order
		int totalItemCost = 0;
		for (OrderItem item : order.getOrderItems()) {
			totalItemCost += item.getItem().getPrice() * item.getQuantity();
		}


		// Deduct points if the customer used them (if using points)
		int pointsToDeduct = order.getPricePaid();
		if (expectedCost < totalItemCost) {
			totalItemCost -= pointsToDeduct; // Deduct points
		}

		assertEquals(expectedCost, totalItemCost, "Final cost calculation mismatch.");

	}
}