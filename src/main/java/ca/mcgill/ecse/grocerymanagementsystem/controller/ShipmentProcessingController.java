package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;

public class ShipmentProcessingController {
	public static void receiveShipment(int shipmentNumber) {
		Shipment s = findShipment(shipmentNumber);
		for (ShipmentItem si : s.getShipmentItems()) {
			Item item = si.getItem();
			int newQty = item.getQuantityInInventory() + si.getQuantity();
			item.setQuantityInInventory(newQty);
		}
		s.delete();
	}

	private static Shipment findShipment(int shipmentNumber) {
		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();
		for (Shipment s : sys.getShipments()) {
			if (s.getShipmentNumber() == shipmentNumber) {
				return s;
			}
		}
		throw new GroceryStoreException(String.format("there is no shipment with number \"%d\"", shipmentNumber));
	}
}
