package ca.mcgill.ecse.grocerymanagementsystem.feature;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;

public class StepDefinitions {
	/**
	 * Set this field in <code>@When</code> steps if an error was raised.
	 */
	static GroceryStoreException error;
	
	protected void before() {
		GroceryManagementSystemController.resetSystem();
		error = null;
	}

	protected GroceryManagementSystem getSystem() {
		return GroceryManagementSystemController.getGroceryManagementSystem();
	}
}
