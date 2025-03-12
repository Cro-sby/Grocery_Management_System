package ca.mcgill.ecse.grocerymanagementsystem.feature;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;

public class StepDefinitions {
	/**
	 * Set this field in <code>@When</code> steps if an error was raised.
	 */
	static GroceryStoreException error;
	
	protected void before() {
		GroceryManagementSystemController.resetSystem();
		error = null;
	}
}
