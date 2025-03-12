package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Manager;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;

public class GroceryManagementSystemController {
	private static GroceryManagementSystem system;
	
	public static GroceryManagementSystem getGroceryManagementSystem() {
		if (system == null) {
			system = new GroceryManagementSystem();
			User managerUser = new User("manager", "manager", "", "", system);
			new Manager(managerUser, system);
		}
		return system;
	}
	
	public static void resetSystem() {
		if (system != null) {
			system.delete();
			system = null;
		}
	}
}
