package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;
import ca.mcgill.ecse.grocerymanagementsystem.model.UserRole;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions related to users, customers, employees, and managers.
 */
public class UserStepDefinitions extends StepDefinitions {
	private Customer currentCustomer;
	private Employee currentEmployee;
	
	@Before
	public void before() {
		super.before();
		this.currentCustomer = null;
		this.currentEmployee = null;
	}

	@Given("the following employees exist in the system")
	public void the_following_employees_exist_in_the_system(List<Map<String, String>> employees) {
		for (Map<String, String> example : employees) {
			String username = example.get("username");
			User u = User.getWithUsername(username);
			if (u == null) {
				u = new User(
						example.get("username"),
						example.get("password"),
						example.get("name"),
						example.get("phone"),
						this.getSystem());
			}
			new Employee(u, this.getSystem());
		}
	}

	@Given("the following customers exist in the system")
	public void the_following_customers_exist_in_the_system(List<Map<String, String>> customers) {
		for (Map<String, String> example : customers) {
			String username = example.get("username");
			User u = User.getWithUsername(username);
			if (u == null) {
				u = new User(
						example.get("username"),
						example.get("password"),
						example.get("name"),
						example.get("phone"),
						this.getSystem());
			}
			new Customer(
					u,
					example.get("address"),
					Integer.parseInt(example.get("points")),
					this.getSystem());
		}
	}

	@When("a customer attempts to register with username {string}, password {string}, and address {string}")
	public void a_customer_attempts_to_register_with_username_password_and_address(
			String username, String password, String address) {
		try {
			UserController.registerNewCustomer(username, password, address);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("{string} attempts to add a customer account with address {string}")
	public void attempts_to_add_a_customer_account_with_address(String username, String address) {
		try {
			UserController.addCustomerAccount(username, address);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to add a new employee with username {string}")
	public void the_manager_attempts_to_add_a_new_employee_with_username(String username) {
		try {
			UserController.registerNewEmployee(username);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to create an employee account for {string}")
	public void the_manager_attempts_to_create_an_employee_account_for(String username) {
		try {
			UserController.addEmployeeAccount(username);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("{string} attempts to change their password to {string}")
	public void attempts_to_change_their_password_to(String username, String password) {
		try {
			UserController.updatePassword(username, password);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("{string} attempts to change their name to {string}")
	public void attempts_to_change_their_name_to(String username, String name) {
		try {
			UserController.updateName(username, name);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("{string} attempts to change their phone number to {string}")
	public void attempts_to_change_their_phone_number_to(String username, String phoneNumber) {
		try {
			UserController.updatePhoneNumber(username, phoneNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("{string} attempts to change their address to {string}")
	public void attempts_to_change_their_address_to(String username, String address) {
		try {
			UserController.updateAddress(username, address);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("{string} attempts to delete their customer account")
	public void attempts_to_delete_their_customer_account(String username) {
		try {
			UserController.deleteCustomer(username);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to delete employee {string}")
	public void the_manager_attempts_to_delete_employee(String username) {
		try {
			UserController.deleteEmployee(username);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@Then("no user shall exist with username {string}")
	public void no_user_shall_exist_with_username(String username) {
		assertTrue(!User.hasWithUsername(username), "no user should have the given username");
	}

	@Then("a customer shall exist with username {string}")
	public void a_customer_shall_exist_with_username(String username) {
		User u = User.getWithUsername(username);
		assertNotNull(u, "there should be a user with the given username");
		Customer c = getCustomerAccount(u);
		assertNotNull(c, "the user should be a customer");
		this.currentCustomer = c;
	}

	@Then("no customer shall exist with username {string}")
	public void no_customer_shall_exist_with_username(String username) {
		User u = User.getWithUsername(username);
		Customer c = getCustomerAccount(u);
		assertNull(c, "the user should not be a customer");
	}

	@Then("an employee shall exist with username {string}")
	public void an_employee_shall_exist_with_username(String username) {
		User u = User.getWithUsername(username);
		assertNotNull(u, "there should be a user with the given username");
		Employee c = getEmployeeAccount(u);
		assertNotNull(c, "the user should be an employee");
		this.currentEmployee = c;
	}

	@Then("no employee shall exist with username {string}")
	public void no_employee_shall_exist_with_username(String username) {
		User u = User.getWithUsername(username);
		Employee e = getEmployeeAccount(u);
		assertNull(e, "the user should not be an employee");
	}

	@Then("the customer's password shall be {string}")
	public void the_customer_s_password_shall_be(String password) {
		assertEquals(password, this.currentCustomer.getUser().getPassword());
	}

	@Then("the customer's address shall be {string}")
	public void the_customer_s_address_shall_be(String address) {
		assertEquals(address, this.currentCustomer.getAddress());
	}

	@Then("the customer's name shall be {string}")
	public void the_customer_s_name_shall_be(String name) {
		assertEquals(name, this.currentCustomer.getUser().getName());
	}

	@Then("the customer's phone number shall be {string}")
	public void the_customer_s_phone_number_shall_be(String phone) {
		assertEquals(phone, this.currentCustomer.getUser().getPhoneNumber());
	}

	@Then("the employee's password shall be {string}")
	public void the_employee_s_password_shall_be(String password) {
		assertEquals(password, this.currentEmployee.getUser().getPassword());
	}

	@Then("the employee's name shall be {string}")
	public void the_employee_s_name_shall_be(String name) {
		assertEquals(name, this.currentEmployee.getUser().getName());
	}

	@Then("the employee's phone number shall be {string}")
	public void the_employee_s_phone_number_shall_be(String phoneNumber) {
		assertEquals(phoneNumber, this.currentEmployee.getUser().getPhoneNumber());
	}

	@Then("the customer shall have {int} points")
	public void the_customer_shall_have_points(Integer points) {
		assertEquals(points, this.currentCustomer.getNumberOfPoints());
	}
	
	@Then("the total number of users shall be {int}")
	public void the_total_number_of_users_shall_be(Integer n) {
		assertEquals(n, this.getSystem().getUsers().size());
	}

	@Then("the total number of customers shall be {int}")
	public void the_total_number_of_customers_shall_be(Integer n) {
		assertEquals(n, this.getSystem().getCustomers().size());
	}

	@Then("the total number of employees shall be {int}")
	public void the_total_number_of_employees_shall_be(Integer n) {
		assertEquals(n, this.getSystem().getEmployees().size());
	}

	@Then("there shall be one manager")
	public void there_shall_be_one_manager() {
		assertNotNull(this.getSystem().getManager(), "the manager should exist");
	}
	
	private Customer getCustomerAccount(User u) {
		if (u == null) {
			return null;
		}
		for (UserRole r : u.getRoles()) {
			if (r instanceof Customer) {
				return (Customer) r;
			}
		}
		return null;
	}
	
	private Employee getEmployeeAccount(User u) {
		if (u == null) {
			return null;
		}
		for (UserRole r : u.getRoles()) {
			if (r instanceof Employee) {
				return (Employee) r;
			}
		}
		return null;
	}
}
