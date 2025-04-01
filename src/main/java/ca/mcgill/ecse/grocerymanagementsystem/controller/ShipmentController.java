package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;

import java.sql.Date;

import static ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController.getGroceryManagementSystem;

public class ShipmentController {

	public static void createShipment() throws GroceryStoreException {
		GroceryManagementSystem system = getGroceryManagementSystem();
		new Shipment(null, system);
	}

	public static void deleteShipment(int shipmentNumber) throws GroceryStoreException {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		Shipment shipment;
		if (system.numberOfShipments()<shipmentNumber+1){

			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}
		else {
			shipment = system.getShipment(shipmentNumber);
			if (shipment == null) {
				throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
			}
		}

		if (shipment != null) { // Add null check here!
			if (shipment.getDateOrdered() != null) {
				throw new GroceryStoreException("cannot delete a shipment which has already been ordered");
			}
			shipment.delete(); // Umple generated delete
		}
	}

	private static Shipment findShipmentByShipmentNumber(int shipmentNumber) {
		GroceryManagementSystem system = getGroceryManagementSystem();
		for (Shipment shipment : system.getShipments()) {
			if (shipment.getShipmentNumber() == shipmentNumber) {
				return shipment;
			}
		}
		return null;
	}

	public static void addItemToShipment(int shipmentNumber, String itemName) throws GroceryStoreException {
		GroceryManagementSystem system = getGroceryManagementSystem();
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

		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			if (shipmentItem.getItem().getName().equals(item.getName())) {
				throw new GroceryStoreException("shipment already includes item \"" + itemName + "\"");
			}
		}

		new ShipmentItem(1,  system, shipment, item); 
	}


	public static void updateQuantityInShipment(int shipmentNumber, String itemName, int newQuantity) throws GroceryStoreException {
		if (newQuantity < 0) {
			throw new GroceryStoreException("quantity must be non-negative");
		}
		GroceryManagementSystem system = getGroceryManagementSystem();
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
			shipmentItem.delete(); 
		} else if(shipmentItem != null){
			shipmentItem.setQuantity(newQuantity);
		} else{
			new ShipmentItem(newQuantity, system, shipment, item); //CORRECT constructor call
		}
	}
}
