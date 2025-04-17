package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem; // Import the system
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOs.TOItem;

import java.util.ArrayList;
import java.util.List;

public class ItemController {



	// --- Conversion Method (Now inside ItemController) ---
	public static TOItem toTOItem(Item item) {
		if (item == null) {
			return null;
		}
		// Ensure the TOItem constructor matches the order and types of parameters
		return new TOItem(
				item.getName(),
				item.getQuantityInInventory(),
				item.getPrice(),
				item.getIsPerishable(),
				item.getNumberOfPoints()
		);
	}

	// --- Existing Controller Methods ---

	public static List<TOItem> getAllItems() throws GroceryStoreException {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		if (system == null) {
			throw new GroceryStoreException("GroceryManagementSystem instance is null.");
		}

		List<TOItem> items = new ArrayList<>();
		for (Item item : system.getItems()) {
			items.add(toTOItem(item)); // Call the local conversion method
		}
		return items;
	}

	public static void create(String name, boolean isPerishable, int points, int price) throws GroceryStoreException {
		if (name == null || name.trim().isEmpty()) {
			throw new GroceryStoreException("name is required"); 
		}
		if (points < 1 || points > 5) {
			throw new GroceryStoreException("points must be between one and five");

		}
		if (price <= 0) {
			throw new GroceryStoreException("price must be positive");
		}
		Item item_valid = Item.getWithName(name);
		if (item_valid != null) {
			throw new GroceryStoreException("an item called \"" + name + "\" already exists");
		}

		// Get the GroceryManagementSystem instance
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();

		// Create the item and add it to the system
		new Item(name, 0, price, isPerishable, points, system);
	}


	public static void updatePrice(String name, int newPrice) throws GroceryStoreException {
		Item item_valid = Item.getWithName(name);
		if (item_valid == null) {
			throw new GroceryStoreException("there is no item called \"" + name + "\"");
		}
		if (newPrice <= 0) {
			throw new GroceryStoreException("price must be positive");
		}
		item_valid.setPrice(newPrice);
	}

	public static void updatePoints(String name, int newPoints) throws GroceryStoreException {
		Item item_valid = Item.getWithName(name);
		if (item_valid == null) {
			throw new GroceryStoreException("there is no item called \"" + name + "\"");
		}
		if (newPoints < 1 || newPoints > 5) {
			throw new GroceryStoreException("points must be between one and five");
		}
		item_valid.setNumberOfPoints(newPoints);
	}


	public static void delete(String name) throws GroceryStoreException {
		Item item_valid = Item.getWithName(name);
		if (item_valid == null) {
			throw new GroceryStoreException("there is no item called \"" + name + "\"");
		}
		item_valid.delete(); 
	}

}