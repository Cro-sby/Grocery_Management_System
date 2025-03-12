package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.util.List;
import java.util.Map;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ShipmentStepDefinitions extends StepDefinitions {
	@Before
	public void before() {
		super.before();
	}

	@Given("the following shipments exist")
	public void the_following_shipments_exist(List<Map<String, String>> shipments) {
		throw new io.cucumber.java.PendingException();
	}

	@Given("the following items are part of shipments")
	public void the_following_items_are_part_of_shipments(List<Map<String, String>> shipmentItems) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to create a new shipment")
	public void the_manager_attempts_to_create_a_new_shipment() {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to delete the shipment with ID {string}")
	public void the_manager_attempts_to_delete_the_shipment_with_id(String id) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to delete the non-existent shipment with shipment number {int}")
	public void the_manager_attempts_to_delete_the_non_existent_shipment_with_shipment_number(Integer shipmentNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to add item {string} to the shipment with ID {string}")
	public void the_manager_attempts_to_add_item_to_the_shipment_with_id(String item, String shipmentId) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the user attempts to add item {string} to the non-existent shipment with number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_shipment_with_number(String item, Integer shipmentNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to set the quantity of item {string} in the shipment with ID {string} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_shipment_with_id_to(String item, String shipmentId, Integer quantity) {
		throw new io.cucumber.java.PendingException();
	}

	@When("the manager attempts to set the quantity of item {string} in the non-existent shipment {int} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_non_existent_shipment_to(String item, Integer shipmentNumber, Integer quantity) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("a new shipment shall exist")
	public void a_new_shipment_shall_exist() {
		throw new io.cucumber.java.PendingException();
	}

	@Then("no shipment shall exist with ID {string}")
	public void no_shipment_shall_exist_with_id(String id) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("no shipment shall exist with number {int}")
	public void no_shipment_shall_exist_with_number(Integer shipmentNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("a shipment shall exist with ID {string}")
	public void a_shipment_shall_exist_with_id(String id) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("no shipment shall exist with shipment number {int}")
	public void no_shipment_shall_exist_with_shipment_number(Integer shipmentNumber) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the newly-created shipment shall have {int} items")
	public void the_newly_created_shipment_shall_have_items(Integer n) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the newly-created shipment shall not have been ordered yet")
	public void the_newly_created_shipment_shall_not_have_been_ordered_yet() {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the total number of shipments shall be {int}")
	public void the_total_number_of_shipments_shall_be(Integer n) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the shipment with ID {string} shall include {int} {string}")
	public void the_shipment_with_id_shall_include(String shipmentId, Integer quantity, String item) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the shipment with ID {string} shall not include any items called {string}")
	public void the_shipment_with_id_shall_not_include_any_items_called(String shipmentId, String item) {
		throw new io.cucumber.java.PendingException();
	}

	@Then("the shipment with ID {string} shall include {int} distinct item(s)")
	public void the_shipment_with_id_shall_include_distinct_items(String shipmentId, Integer n) {
		throw new io.cucumber.java.PendingException();
	}
}
