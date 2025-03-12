package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ItemStepDefinitions extends StepDefinitions {
	private Item currentItem;

	@Before
	public void before() {
		super.before();
		this.currentItem = null;
	}

	@Given("the following items exist in the system")
	public void the_following_items_exist_in_the_system(List<Map<String, String>> table) {
		for (Map<String, String> example : table) {
			new Item(
					example.get("name"),
					0,
					Integer.parseInt(example.get("price")),
					parsePerishable(example.get("perishableOrNot")),
					Integer.parseInt(example.get("points")),
					this.getSystem());
		}
	}

	@When("the manager attempts to add a new {string} item with name {string} that's worth {int} points and costs {int} cents")
	public void the_manager_attempts_to_add_a_new_item_with_name_that_s_worth_points_and_costs_cents(
			String perishableOrNot, String name, Integer points, Integer price) {
		try {
			ItemController.create(name, parsePerishable(perishableOrNot), points, price);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to update the price of item {string} to {int}")
	public void the_manager_attempts_to_update_the_price_of_item_to(String item, Integer newPrice) {
		try {
			ItemController.updatePrice(item, newPrice);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to update the point value of item {string} to {int}")
	public void the_manager_attempts_to_update_the_point_value_of_item_to(String item, Integer newPoints) {
		try {
			ItemController.updatePoints(item, newPoints);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to delete the item {string}")
	public void the_manager_attempts_to_delete_the_item(String name) {
		try {
			ItemController.delete(name);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@Then("an item shall exist with name {string}")
	public void an_item_shall_exist_with_name(String name) {
		Item i = Item.getWithName(name);
		assertNotNull(i, "the item should exist");
		this.currentItem = i;
	}

	@Then("no item shall exist with name {string}")
	public void no_item_shall_exist_with_name(String name) {
		Item i = Item.getWithName(name);
		assertNull(i, "the item should not exist");
	}

	@Then("the item shall cost {int} cents")
	public void the_item_shall_cost_cents(Integer price) {
		assertEquals(price, this.currentItem.getPrice());
	}

	@Then("the item shall be worth {int} points")
	public void the_item_shall_be_worth_points(Integer points) {
		assertEquals(points, this.currentItem.getNumberOfPoints());
	}

	@Then("the item shall be {string}")
	public void the_item_shall_be(String perishableOrNot) {
		assertEquals(parsePerishable(perishableOrNot), this.currentItem.getIsPerishable());
	}

	@Then("the quantity of the item shall be {int}")
	public void the_quantity_of_the_item_shall_be(Integer quantity) {
		assertEquals(quantity, this.currentItem.getQuantityInInventory());
	}

	@Then("the total number of items shall be {int}")
	public void the_total_number_of_items_shall_be(Integer n) {
		assertEquals(n, this.getSystem().getItems());
	}

	private boolean parsePerishable(String perishableOrNot) {
			switch (perishableOrNot) {
			case "perishable":
				return true;
			case "non-perishable":
				return false;
			default:
				throw new IllegalArgumentException(
						String.format("expected 'perishable' or 'non-perishable', but found '%s'", perishableOrNot));
			}
	}
}
