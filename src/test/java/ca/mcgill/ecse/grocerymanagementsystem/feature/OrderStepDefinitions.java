package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.util.List;
import java.util.Map;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrderStepDefinitions extends StepDefinitions {
	@Before
	public void before() {
		super.before();
	}

	@Given("the following orders exist in the system")
	public void the_following_orders_exist_in_the_system(List<Map<String, String>> orders) {
		throw new io.cucumber.java.PendingException();
	}

	@Given("the following items are part of orders")
	public void the_following_items_are_part_of_orders(List<Map<String, String>> orderItems) {
		throw new io.cucumber.java.PendingException();
	}

	@When("{string} attempts to create an order with deadline {string}")
	public void attempts_to_create_an_order_with_deadline(String name, String deadline) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to delete the order with ID {string}")
	public void the_user_attempts_to_delete_the_order_with_id(String id) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to delete the non-existent order with order number {int}")
	public void the_user_attempts_to_delete_the_non_existent_order_with_order_number(Integer orderNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to add item {string} to the order with ID {string}")
	public void the_user_attempts_to_add_item_to_the_order_with_id(String item, String orderId) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to add item {string} to the non-existent order with order number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_order_with_order_number(String item, Integer orderNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to set the quantity of item {string} in the order with ID {string} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_order_with_id_to(String item, String orderId, int newQuantity) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to set the quantity of item {string} in the non-existent order {int} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_nonexistent_order_to(String item, int orderNumber, int newQuantity) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the total number of orders shall be {int}")
	public void the_total_number_of_orders_shall_be(Integer n) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("{string} shall have a new order")
	public void shall_have_a_new_order(String name) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("an order shall exist with ID {string}")
	public void an_order_shall_exist_with_id(String id) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("no order shall exist with ID {string}")
	public void no_order_shall_exist_with_id(String id) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("no order shall exist with order number {int}")
	public void no_order_shall_exist_with_order_number(Integer orderNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the newly-created order shall have deadline {string}")
	public void the_newly_created_order_shall_have_deadline(String deadline) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the newly-created order shall have {int} items")
	public void the_newly_created_order_shall_have_items(Integer n) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the newly-created order shall not have been placed")
	public void the_newly_created_order_shall_not_have_been_placed() {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the order with ID {string} shall include {int} {string}")
	public void the_order_with_id_shall_include(String orderId, Integer quantity, String item) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the order with ID {string} shall not include any items called {string}")
	public void the_order_with_id_shall_not_include_any_items_called(String orderId, String item) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the order with ID {string} shall include {int} distinct item(s)")
	public void the_order_with_id_shall_include_distinct_items(String orderId, Integer n) {
		throw new io.cucumber.java.PendingException();
	}
}
