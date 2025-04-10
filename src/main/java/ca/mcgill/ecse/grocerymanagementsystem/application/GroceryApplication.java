package ca.mcgill.ecse.grocerymanagementsystem.application;

import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;

import java.sql.Date;
import java.time.LocalDate;


public class GroceryApplication {
	public static void main(String[] args) {
		//Create the GroceryManagementSystem instance
		//Create the GroceryManagementSystem instance
		GroceryManagementSystem groceryManagementSystem = new GroceryManagementSystem();

		//Create  customers
		User user1 = new User("coby123", "cora()773287zon", "Coby", "5142576484", groceryManagementSystem);
		Customer customer1 = new Customer(user1, "cobysantos@gmail.com", 12, groceryManagementSystem);
		User user2 = new User("john456", "tuiashir464dh*93#$", "John Doe", "5149876543", groceryManagementSystem);
		Customer customer2 = new Customer(user2, "john.doe@example.com", 25, groceryManagementSystem);

		//Create items and add them to the system
		Item item1 = new Item("Apple", 100, 50, true, 10, groceryManagementSystem);
		Item item2 = new Item("Milk", 200, 120, true, 5, groceryManagementSystem);
		Item item3 = new Item("Bread", 50, 80, true, 2, groceryManagementSystem);
		Item item4 = new Item("Cheese", 300, 150, true, 6, groceryManagementSystem); // New item
		Item item5 = new Item("Orange", 150, 90, true, 8, groceryManagementSystem); // New item
		Item item6 = new Item("Yogurt", 120, 70, true, 4, groceryManagementSystem); // New item

		//Create orders for both customers
		Date orderDate = new Date(System.currentTimeMillis());
		Order.DeliveryDeadline deliveryDeadline = Order.DeliveryDeadline.InTwoDays;  // Changed deadline for variety

		// First customer orders
		Order order1 = new Order(orderDate, deliveryDeadline, groceryManagementSystem, customer1);
		Order order2 = new Order(orderDate, deliveryDeadline, groceryManagementSystem, customer1);

		// Second customer orders
		Order order3 = new Order(orderDate, deliveryDeadline, groceryManagementSystem, customer2);
		Order order4 = new Order(orderDate, deliveryDeadline, groceryManagementSystem, customer2);

		//Add items to the orders

		item1.addOrderItem(3, groceryManagementSystem, order1);
		item2.addOrderItem(5, groceryManagementSystem, order1);
		item3.addOrderItem(2, groceryManagementSystem, order2);
		item6.addOrderItem(6,groceryManagementSystem,order1);
		item1.addOrderItem(2, groceryManagementSystem, order3);
		item2.addOrderItem(6, groceryManagementSystem, order3);
		item3.addOrderItem(1, groceryManagementSystem, order4);
		item4.addOrderItem(3, groceryManagementSystem, order4);
		item5.addOrderItem(4, groceryManagementSystem, order4);
		item6.addOrderItem(6,groceryManagementSystem,order2);



		//Create shipments and associate items with them
		Shipment shipment1 = new Shipment(orderDate, groceryManagementSystem);
		Shipment shipment2 = new Shipment(orderDate, groceryManagementSystem);
		Shipment shipment3 = new Shipment(orderDate, groceryManagementSystem);
		Shipment shipment4 = new Shipment(orderDate, groceryManagementSystem);
		Shipment shipment5 = new Shipment(orderDate, groceryManagementSystem);

		item1.addShipmentItem(4, groceryManagementSystem, shipment1);
		item2.addShipmentItem(7, groceryManagementSystem, shipment2);
		item3.addShipmentItem(2, groceryManagementSystem, shipment3);
		item1.addShipmentItem(3, groceryManagementSystem, shipment4);
		item2.addShipmentItem(5, groceryManagementSystem, shipment4);
		item4.addShipmentItem(2, groceryManagementSystem, shipment5);
		item5.addShipmentItem(3, groceryManagementSystem, shipment5);

		System.out.println("Grocery Management System State:");
		System.out.println("---------------------------------");

		System.out.println("Customer 1 Details:");
		printUserDetails(user1);

		System.out.println("\nOrders for Customer 1:");
		printOrderDetails(order1);
		printOrderDetails(order2);

		System.out.println("\nShipments for Customer 1:");
		printShipmentDetails(shipment1);
		printShipmentDetails(shipment2);

		System.out.println("\nCustomer 2 Details:");
		printUserDetails(user2);

		System.out.println("\nOrders for Customer 2:");
		printOrderDetails(order3);
		printOrderDetails(order4);

		System.out.println("\nShipments for Customer 2:");
		printShipmentDetails(shipment3);
		printShipmentDetails(shipment4);
		printShipmentDetails(shipment5);

		//Update inventory after order and shipment
		order1.checkout();
		order2.checkout();
		order3.checkout();
		order4.checkout();


		System.out.println("\nUpdated States after checking out orders:");
		System.out.println("order1's status: "+ order1.getStatusFullName());
		System.out.println("order2's status: "+ order2.getStatusFullName());
		System.out.println("order3's status: "+ order3.getStatusFullName());
		System.out.println("order4's status: "+ order4.getStatusFullName());

		order1.payOrder(order1.getTotalCost(),order1.getOrderPlacer().getNumberOfPoints());
		order2.payOrder(order2.getTotalCost(),order2.getOrderPlacer().getNumberOfPoints());


		System.out.println("\nUpdated States after paying for orders 1 and 2:");
		System.out.println("order1's status: "+ order1.getStatusFullName());
		System.out.println("order2's status: "+ order2.getStatusFullName());
		System.out.println("order3's status: "+ order3.getStatusFullName());
		System.out.println("order4's status: "+ order4.getStatusFullName());
		// New item details
	}

	private static void printUserDetails(User user) {
		System.out.println("User Details:");
		System.out.printf("  Username: %s\n", user.getUsername());
		System.out.printf("  Password: %s\n", user.getPassword());
		System.out.printf("  Full Name: %s\n", user.getName());
		System.out.printf("  Phone Number: %s\n", user.getPhoneNumber());
		System.out.println();
	}

	private static void printItemDetails(Item item) {
		System.out.printf("Item: %s\n", item.getName());
		System.out.printf("  Quantity in Inventory: %d\n", item.getQuantityInInventory());
		System.out.printf("  Price: $%d\n", item.getPrice());  // Use %d for integers
		System.out.printf("  Number of Points: %d\n", item.getNumberOfPoints());
		System.out.println();
	}

	private static void printOrderDetails(Order order) {
		System.out.printf("Order #%d\n", order.getOrderNumber());
		System.out.printf("  Date Placed: %s\n", order.getDatePlaced());
		System.out.printf("  Delivery Deadline: %s\n", order.getDeadline());
		System.out.printf("  Total Cost: $%d\n", order.getTotalCost());  // For integer values
		System.out.printf("  Price Paid: $%d\n", order.getPricePaid());  // For integer values
		System.out.print("  Status: " + order.getStatusFullName() + "\n");
		System.out.println("  Ordered Items:");

		// Print ordered items for the order
		for (OrderItem orderItem : order.getOrderItems()) {
			System.out.printf("    Item: %s, Quantity: %d\n", orderItem.getItem().getName(), orderItem.getQuantity());
		}
		System.out.println();
	}

	private static void printShipmentDetails(Shipment shipment) {
		System.out.printf("Shipment #%d\n", shipment.getShipmentNumber());
		System.out.printf("  Date Ordered: %s\n", shipment.getDateOrdered());
		System.out.println();
	}
}
