package ca.mcgill.ecse.grocerymanagementsystem.feature;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrderProcessingStepDefinitions {
	@When("the user attempts to check out the order with ID {string}")
	public void the_user_attempts_to_check_out_the_order_with_id(String orderId) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to pay for the order with ID {string} {string} their points")
	public void the_user_attempts_to_pay_for_the_order_with_id_without_using_their_points(String orderId,
			String usingOrWithoutUsing) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to assign the order with ID {string} to {string}")
	public void the_manager_attempts_to_assign_the_order_with_id_to(String orderId, String employeeUsername) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to indicate that assembly of the order with ID {string} is finished")
	public void the_user_attempts_to_indicate_that_assembly_of_the_order_with_id_is_finished(String orderId) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to cancel the order with ID {string}")
	public void the_user_attempts_to_cancel_the_order_with_id(String orderId) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to mark the order with ID {string} as delivered")
	public void the_manager_attempts_to_mark_the_order_with_id_as_delivered(String orderId) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the order shall be {string}")
	public void the_order_shall_be(String expectedState) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the order's assignee shall be {string}")
	public void the_order_s_assignee_shall_be(String employeeUsername) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the order's date placed shall be today")
	public void the_order_s_date_placed_shall_be_today() {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the total cost of the order shall be {int} cents")
	public void the_total_cost_of_the_order_shall_be_cents(Integer expectedCost) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the final cost of the order, after considering points, shall be {int} cents")
	public void the_final_cost_of_the_order_after_considering_points_shall_be_cents(Integer expectedCost) {
		throw new io.cucumber.java.PendingException();
	}
}
