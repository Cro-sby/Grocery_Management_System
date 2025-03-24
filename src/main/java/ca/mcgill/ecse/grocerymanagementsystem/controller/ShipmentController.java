package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;

import java.sql.Date;

public class ShipmentController {
	private static GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();

	public static void createShipment() throws GroceryStoreException {
		// Create a new shipment with the current date.
		new Shipment(null, system);
	}

	public static void deleteShipment(int shipmentNumber) throws GroceryStoreException {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		Shipment shipment = system.getShipment(shipmentNumber);  // Assumes a getShipment method
		if (shipment == null) {
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}
		if (system.numberOfShipments()<shipmentNumber+1){
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}

		//ADD THIS NULL CHECK:
		if (shipment != null) { // Add null check here!
			if (shipment.getDateOrdered() != null) {
				throw new GroceryStoreException("cannot delete a shipment which has already been ordered");
			}
			shipment.delete(); // Umple generated delete
		}
	}

	// Helper method to find a shipment by shipmentNumber
	private static Shipment findShipmentByShipmentNumber(int shipmentNumber) {
		for (Shipment shipment : system.getShipments()) {
			if (shipment.getShipmentNumber() == shipmentNumber) {
				return shipment;
			}
		}
		return null;
	}

	public static void addItemToShipment(int shipmentNumber, String itemName) throws GroceryStoreException {
		Shipment shipment = system.getShipment(shipmentNumber); // Assumes you have a getShipment method
		if (shipment == null) {
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}

		// Check if the shipment has already been ordered (assuming dateOrdered != null means ordered)
		if (shipment.getDateOrdered() != null) {
			throw new GroceryStoreException("shipment has already been ordered");
		}

		Item item = Item.getWithName(itemName);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}

		// Check if the item is already in the shipment
		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			if (shipmentItem.getItem().getName().equals(item.getName())) {
				throw new GroceryStoreException("shipment already includes item \"" + itemName + "\"");
			}
		}

		// Add the item to the shipment with quantity 1
		new ShipmentItem(1,  system, shipment, item); //CORRECT constructor call
	}


	public static void updateQuantityInShipment(int shipmentNumber, String itemName, int newQuantity) throws GroceryStoreException {
		if (newQuantity < 0) {
			throw new GroceryStoreException("quantity must be non-negative");
		}

		Shipment shipment = system.getShipment(shipmentNumber); // Assumes you have a getShipment method
		if (shipment == null) {
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}
		if (shipment.getDateOrdered() != null) {
			throw new GroceryStoreException("shipment has already been ordered");
		}

		Item item = Item.getWithName(itemName);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}

		ShipmentItem shipmentItem = null;
		for (ShipmentItem si : shipment.getShipmentItems()) {
			if (si.getItem().getName().equals(itemName)) {
				shipmentItem = si;
				break;
			}
		}

		if (shipmentItem == null && newQuantity !=0) {
			throw new GroceryStoreException("shipment does not include item \"" + itemName + "\"");
		}

		if (newQuantity == 0 && shipmentItem != null) {
			shipmentItem.delete();  // Remove the ShipmentItem if quantity is 0
		} else if(shipmentItem != null){
			shipmentItem.setQuantity(newQuantity);
		} else{
			new ShipmentItem(newQuantity, system, shipment, item); //CORRECT constructor call
		}
	}
}