package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController; 
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentStepDefinitions extends StepDefinitions {

	private Map<String, Integer> shipmentIdMap = new HashMap<>(); 
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


	@Before
	public void before() {
		super.before();
		shipmentIdMap.clear();  
	}

	@Given("the following shipments exist")
	public void the_following_shipments_exist(List<Map<String, String>> shipments) {
		GroceryManagementSystem system = getSystem();
		for (Map<String, String> shipmentData : shipments) {
			String id = shipmentData.get("id");
			String dateOrderedString = shipmentData.get("dateOrdered");

			java.sql.Date dateOrdered = null;
			if (dateOrderedString != null && !dateOrderedString.equals("NULL")) {
				try {
					java.util.Date parsedDate = dateFormat.parse(dateOrderedString);
					dateOrdered = new java.sql.Date(parsedDate.getTime());
				} catch (ParseException e) {
					throw new RuntimeException("Error parsing date: " + dateOrderedString, e);
				}
			}

			Shipment shipment = new Shipment(dateOrdered, system);
			system.addShipment(shipment);
			shipmentIdMap.put(id, shipment.getShipmentNumber());
		}
	}


	@Given("the following items are part of shipments")
	public void the_following_items_are_part_of_shipments(List<Map<String, String>> shipmentItems) {
		GroceryManagementSystem system = getSystem();
		for (Map<String, String> shipmentItemData : shipmentItems) {
			String shipmentId = shipmentItemData.get("shipment");
			String itemName = shipmentItemData.get("item");
			String quantityStr = shipmentItemData.get("quantity");
			int quantity = Integer.parseInt(quantityStr);

			Item item = Item.getWithName(itemName); 
			Shipment shipment = system.getShipment(shipmentIdMap.get(shipmentId)); 

			if (item != null && shipment != null) {
				new ShipmentItem(quantity,  system, shipment, item); 
			} else {
				throw new IllegalArgumentException("Invalid item or shipment ID in 'the_following_items_are_part_of_shipments'");
			}
		}
	}




	@When("the manager attempts to create a new shipment")
	public void the_manager_attempts_to_create_a_new_shipment() {
		try {
			ShipmentController.createShipment();
			error = null; //Clear error
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to delete the shipment with ID {string}")
	public void the_manager_attempts_to_delete_the_shipment_with_id(String id) {
		try {
			Integer shipmentNumber = shipmentIdMap.get(id); 
			if (shipmentNumber != null) {
				ShipmentController.deleteShipment(shipmentNumber);
			} else {
				error = new GroceryStoreException("Shipment ID not found: " + id);
			}
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to delete the non-existent shipment with shipment number {int}")
	public void the_manager_attempts_to_delete_the_non_existent_shipment_with_shipment_number(Integer shipmentNumber) {
		try {
			ShipmentController.deleteShipment(shipmentNumber);
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to add item {string} to the shipment with ID {string}")
	public void the_manager_attempts_to_add_item_to_the_shipment_with_id(String item, String shipmentId) {
		try {
			Integer shipmentNumber = shipmentIdMap.get(shipmentId);
			ShipmentController.addItemToShipment(shipmentNumber, item);
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the user attempts to add item {string} to the non-existent shipment with number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_shipment_with_number(String item, Integer shipmentNumber) {
		try {
			ShipmentController.addItemToShipment(shipmentNumber, item);
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to set the quantity of item {string} in the shipment with ID {string} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_shipment_with_id_to(String item, String shipmentId, Integer quantity) {
		try {
			Integer shipmentNumber = shipmentIdMap.get(shipmentId);
			ShipmentController.updateQuantityInShipment(shipmentNumber, item, quantity);
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@When("the manager attempts to set the quantity of item {string} in the non-existent shipment {int} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_nonexistent_shipment_to(String item, Integer shipmentNumber, Integer quantity) {
		try {
			ShipmentController.updateQuantityInShipment(shipmentNumber, item, quantity);
			error = null;
		} catch (GroceryStoreException e) {
			error = e;
		}
	}

	@Then("a new shipment shall exist")
	public void a_new_shipment_shall_exist() {
		GroceryManagementSystem system = getSystem();
		assertTrue(system.getShipments().size() > 0, "A new shipment should exist");
	}

	@Then("no shipment shall exist with ID {string}")
	public void no_shipment_shall_exist_with_id(String id) {
		Integer shipmentNumber = shipmentIdMap.get(id);
		if (shipmentNumber != null) { 
			GroceryManagementSystem system = getSystem();
			Shipment shipment = system.getShipment(shipmentNumber); 
			assertNull(shipment, "Shipment with ID " + id + " should not exist");
		}
	}

	@Then("no shipment shall exist with number {int}")
	public void no_shipment_shall_exist_with_number(Integer shipmentNumber) {
		GroceryManagementSystem system = getSystem();
		Shipment shipment = system.getShipment(shipmentNumber); // Use getShipment
		assertNull(shipment, "Shipment with number " + shipmentNumber + " should not exist");
	}

	@Then("a shipment shall exist with ID {string}") 
	public void a_shipment_shall_exist_with_id(String id) {
		Integer shipmentNumber = shipmentIdMap.get(id);
		assertNotNull(shipmentNumber, "Shipment ID " + id + " not found in map");
		GroceryManagementSystem system = getSystem();
		Shipment shipment = system.getShipment(shipmentNumber); // Use getShipment
		assertNotNull(shipment, "Shipment with ID " + id + " does not exist");
	}


	@Then("the newly-created shipment shall have {int} items")
	public void the_newly_created_shipment_shall_have_items(Integer n) {
		GroceryManagementSystem system = getSystem();
		Shipment latestShipment = system.getShipments().get(system.getShipments().size() - 1); 
		assertNotNull(latestShipment);
		assertEquals(n.intValue(), latestShipment.getShipmentItems().size());
	}

	@Then("the newly-created shipment shall not have been ordered yet")
	public void the_newly_created_shipment_shall_not_have_been_ordered_yet() {
		GroceryManagementSystem system = getSystem();
		Shipment latestShipment = system.getShipments().get(system.getShipments().size() - 1);
		assertNull(latestShipment.getDateOrdered(), "The new shipment should not have a date ordered");
	}

	@Then("the total number of shipments shall be {int}")
	public void the_total_number_of_shipments_shall_be(Integer n) {
		GroceryManagementSystem system = getSystem();
		assertEquals(n.intValue(), system.getShipments().size(), "Incorrect total number of shipments");
	}

	@Then("the shipment with ID {string} shall include {int} {string}")
	public void the_shipment_with_id_shall_include(String shipmentId, Integer quantity, String item) {
		GroceryManagementSystem system = getSystem();
		Shipment shipment = system.getShipment(shipmentIdMap.get(shipmentId));
		assertNotNull(shipment, "Shipment with ID " + shipmentId + " not found");

		boolean found = false;
		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			if (shipmentItem.getItem().getName().equals(item)) {
				assertEquals(quantity.intValue(), shipmentItem.getQuantity(),
						"Quantity mismatch for item " + item + " in shipment " + shipmentId);
				found = true;
				break;
			}
		}
		assertTrue(found, "Item " + item + " not found in shipment " + shipmentId);
	}

	@Then("the shipment with ID {string} shall not include any items called {string}")
	public void the_shipment_with_id_shall_not_include_any_items_called(String shipmentId, String item) {
		GroceryManagementSystem system = getSystem();
		Shipment shipment = system.getShipment(shipmentIdMap.get(shipmentId)); 
		assertNotNull(shipment, "Shipment with ID " + shipmentId + " not found");

		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			assertFalse(shipmentItem.getItem().getName().equals(item),
					"Item " + item + " should not be in shipment " + shipmentId);
		}
	}

	@Then("the shipment with ID {string} shall include {int} distinct item(s)")
	public void the_shipment_with_id_shall_include_distinct_items(String shipmentId, Integer n) {
		GroceryManagementSystem system = getSystem();
		Shipment shipment = system.getShipment(shipmentIdMap.get(shipmentId));
		assertNotNull(shipment, "Shipment with ID " + shipmentId + " not found");
		assertEquals(n.intValue(), shipment.getShipmentItems().size(), "Distinct item count mismatch");
	}
}
