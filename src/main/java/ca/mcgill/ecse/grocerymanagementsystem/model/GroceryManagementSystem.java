/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.util.*;
import java.sql.Date;

// line 4 "../../../../../../model.ump"
// line 88 "../../../../../../model.ump"
public class GroceryManagementSystem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GroceryManagementSystem Associations
  private List<User> users;
  private List<Employee> employees;
  private List<Customer> customers;
  private Manager manager;
  private List<Order> orders;
  private List<Item> items;
  private List<Shipment> shipments;
  private List<OrderItem> orderItems;
  private List<ShipmentItem> shipmentItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GroceryManagementSystem()
  {
    users = new ArrayList<User>();
    employees = new ArrayList<Employee>();
    customers = new ArrayList<Customer>();
    orders = new ArrayList<Order>();
    items = new ArrayList<Item>();
    shipments = new ArrayList<Shipment>();
    orderItems = new ArrayList<OrderItem>();
    shipmentItems = new ArrayList<ShipmentItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public User getUser(int index)
  {
    User aUser = users.get(index);
    return aUser;
  }

  public List<User> getUsers()
  {
    List<User> newUsers = Collections.unmodifiableList(users);
    return newUsers;
  }

  public int numberOfUsers()
  {
    int number = users.size();
    return number;
  }

  public boolean hasUsers()
  {
    boolean has = users.size() > 0;
    return has;
  }

  public int indexOfUser(User aUser)
  {
    int index = users.indexOf(aUser);
    return index;
  }
  /* Code from template association_GetMany */
  public Employee getEmployee(int index)
  {
    Employee aEmployee = employees.get(index);
    return aEmployee;
  }

  public List<Employee> getEmployees()
  {
    List<Employee> newEmployees = Collections.unmodifiableList(employees);
    return newEmployees;
  }

  public int numberOfEmployees()
  {
    int number = employees.size();
    return number;
  }

  public boolean hasEmployees()
  {
    boolean has = employees.size() > 0;
    return has;
  }

  public int indexOfEmployee(Employee aEmployee)
  {
    int index = employees.indexOf(aEmployee);
    return index;
  }
  /* Code from template association_GetMany */
  public Customer getCustomer(int index)
  {
    Customer aCustomer = customers.get(index);
    return aCustomer;
  }

  public List<Customer> getCustomers()
  {
    List<Customer> newCustomers = Collections.unmodifiableList(customers);
    return newCustomers;
  }

  public int numberOfCustomers()
  {
    int number = customers.size();
    return number;
  }

  public boolean hasCustomers()
  {
    boolean has = customers.size() > 0;
    return has;
  }

  public int indexOfCustomer(Customer aCustomer)
  {
    int index = customers.indexOf(aCustomer);
    return index;
  }
  /* Code from template association_GetOne */
  public Manager getManager()
  {
    return manager;
  }

  public boolean hasManager()
  {
    boolean has = manager != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_GetMany */
  public Item getItem(int index)
  {
    Item aItem = items.get(index);
    return aItem;
  }

  public List<Item> getItems()
  {
    List<Item> newItems = Collections.unmodifiableList(items);
    return newItems;
  }

  public int numberOfItems()
  {
    int number = items.size();
    return number;
  }

  public boolean hasItems()
  {
    boolean has = items.size() > 0;
    return has;
  }

  public int indexOfItem(Item aItem)
  {
    int index = items.indexOf(aItem);
    return index;
  }
  /* Code from template association_GetMany */
  public Shipment getShipment(int index)
  {
    Shipment aShipment = shipments.get(index);
    return aShipment;
  }

  public List<Shipment> getShipments()
  {
    List<Shipment> newShipments = Collections.unmodifiableList(shipments);
    return newShipments;
  }

  public int numberOfShipments()
  {
    int number = shipments.size();
    return number;
  }

  public boolean hasShipments()
  {
    boolean has = shipments.size() > 0;
    return has;
  }

  public int indexOfShipment(Shipment aShipment)
  {
    int index = shipments.indexOf(aShipment);
    return index;
  }
  /* Code from template association_GetMany */
  public OrderItem getOrderItem(int index)
  {
    OrderItem aOrderItem = orderItems.get(index);
    return aOrderItem;
  }

  public List<OrderItem> getOrderItems()
  {
    List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
    return newOrderItems;
  }

  public int numberOfOrderItems()
  {
    int number = orderItems.size();
    return number;
  }

  public boolean hasOrderItems()
  {
    boolean has = orderItems.size() > 0;
    return has;
  }

  public int indexOfOrderItem(OrderItem aOrderItem)
  {
    int index = orderItems.indexOf(aOrderItem);
    return index;
  }
  /* Code from template association_GetMany */
  public ShipmentItem getShipmentItem(int index)
  {
    ShipmentItem aShipmentItem = shipmentItems.get(index);
    return aShipmentItem;
  }

  public List<ShipmentItem> getShipmentItems()
  {
    List<ShipmentItem> newShipmentItems = Collections.unmodifiableList(shipmentItems);
    return newShipmentItems;
  }

  public int numberOfShipmentItems()
  {
    int number = shipmentItems.size();
    return number;
  }

  public boolean hasShipmentItems()
  {
    boolean has = shipmentItems.size() > 0;
    return has;
  }

  public int indexOfShipmentItem(ShipmentItem aShipmentItem)
  {
    int index = shipmentItems.indexOf(aShipmentItem);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfUsers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public User addUser(String aUsername, String aPassword, String aName, String aPhoneNumber)
  {
    return new User(aUsername, aPassword, aName, aPhoneNumber, this);
  }

  public boolean addUser(User aUser)
  {
    boolean wasAdded = false;
    if (users.contains(aUser)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aUser.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aUser.setGroceryManagementSystem(this);
    }
    else
    {
      users.add(aUser);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUser(User aUser)
  {
    boolean wasRemoved = false;
    //Unable to remove aUser, as it must always have a groceryManagementSystem
    if (!this.equals(aUser.getGroceryManagementSystem()))
    {
      users.remove(aUser);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addUserAt(User aUser, int index)
  {  
    boolean wasAdded = false;
    if(addUser(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(users.contains(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addUserAt(aUser, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployees()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Employee addEmployee(User aUser)
  {
    return new Employee(aUser, this);
  }

  public boolean addEmployee(Employee aEmployee)
  {
    boolean wasAdded = false;
    if (employees.contains(aEmployee)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aEmployee.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aEmployee.setGroceryManagementSystem(this);
    }
    else
    {
      employees.add(aEmployee);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployee(Employee aEmployee)
  {
    boolean wasRemoved = false;
    //Unable to remove aEmployee, as it must always have a groceryManagementSystem
    if (!this.equals(aEmployee.getGroceryManagementSystem()))
    {
      employees.remove(aEmployee);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeAt(Employee aEmployee, int index)
  {  
    boolean wasAdded = false;
    if(addEmployee(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
      employees.remove(aEmployee);
      employees.add(index, aEmployee);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAt(Employee aEmployee, int index)
  {
    boolean wasAdded = false;
    if(employees.contains(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
      employees.remove(aEmployee);
      employees.add(index, aEmployee);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEmployeeAt(aEmployee, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Customer addCustomer(User aUser, String aAddress, int aNumberOfPoints)
  {
    return new Customer(aUser, aAddress, aNumberOfPoints, this);
  }

  public boolean addCustomer(Customer aCustomer)
  {
    boolean wasAdded = false;
    if (customers.contains(aCustomer)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aCustomer.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aCustomer.setGroceryManagementSystem(this);
    }
    else
    {
      customers.add(aCustomer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomer(Customer aCustomer)
  {
    boolean wasRemoved = false;
    //Unable to remove aCustomer, as it must always have a groceryManagementSystem
    if (!this.equals(aCustomer.getGroceryManagementSystem()))
    {
      customers.remove(aCustomer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerAt(Customer aCustomer, int index)
  {  
    boolean wasAdded = false;
    if(addCustomer(aCustomer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomers()) { index = numberOfCustomers() - 1; }
      customers.remove(aCustomer);
      customers.add(index, aCustomer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerAt(Customer aCustomer, int index)
  {
    boolean wasAdded = false;
    if(customers.contains(aCustomer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomers()) { index = numberOfCustomers() - 1; }
      customers.remove(aCustomer);
      customers.add(index, aCustomer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomerAt(aCustomer, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setManager(Manager aNewManager)
  {
    boolean wasSet = false;
    if (manager != null && !manager.equals(aNewManager) && equals(manager.getGroceryManagementSystem()))
    {
      //Unable to setManager, as existing manager would become an orphan
      return wasSet;
    }

    manager = aNewManager;
    GroceryManagementSystem anOldGroceryManagementSystem = aNewManager != null ? aNewManager.getGroceryManagementSystem() : null;

    if (!this.equals(anOldGroceryManagementSystem))
    {
      if (anOldGroceryManagementSystem != null)
      {
        anOldGroceryManagementSystem.manager = null;
      }
      if (manager != null)
      {
        manager.setGroceryManagementSystem(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(Date aDatePlaced, Order.DeliveryDeadline aDeadline, Customer aOrderPlacer)
  {
    return new Order(aDatePlaced, aDeadline, this, aOrderPlacer);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aOrder.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aOrder.setGroceryManagementSystem(this);
    }
    else
    {
      orders.add(aOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrder, as it must always have a groceryManagementSystem
    if (!this.equals(aOrder.getGroceryManagementSystem()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Item addItem(String aName, int aQuantityInInventory, int aPrice, boolean aIsPerishable, int aNumberOfPoints)
  {
    return new Item(aName, aQuantityInInventory, aPrice, aIsPerishable, aNumberOfPoints, this);
  }

  public boolean addItem(Item aItem)
  {
    boolean wasAdded = false;
    if (items.contains(aItem)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aItem.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aItem.setGroceryManagementSystem(this);
    }
    else
    {
      items.add(aItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeItem(Item aItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aItem, as it must always have a groceryManagementSystem
    if (!this.equals(aItem.getGroceryManagementSystem()))
    {
      items.remove(aItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addItemAt(Item aItem, int index)
  {  
    boolean wasAdded = false;
    if(addItem(aItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItems()) { index = numberOfItems() - 1; }
      items.remove(aItem);
      items.add(index, aItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveItemAt(Item aItem, int index)
  {
    boolean wasAdded = false;
    if(items.contains(aItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItems()) { index = numberOfItems() - 1; }
      items.remove(aItem);
      items.add(index, aItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addItemAt(aItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShipments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Shipment addShipment(Date aDateOrdered)
  {
    return new Shipment(aDateOrdered, this);
  }

  public boolean addShipment(Shipment aShipment)
  {
    boolean wasAdded = false;
    if (shipments.contains(aShipment)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aShipment.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aShipment.setGroceryManagementSystem(this);
    }
    else
    {
      shipments.add(aShipment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShipment(Shipment aShipment)
  {
    boolean wasRemoved = false;
    //Unable to remove aShipment, as it must always have a groceryManagementSystem
    if (!this.equals(aShipment.getGroceryManagementSystem()))
    {
      shipments.remove(aShipment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShipmentAt(Shipment aShipment, int index)
  {  
    boolean wasAdded = false;
    if(addShipment(aShipment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipments()) { index = numberOfShipments() - 1; }
      shipments.remove(aShipment);
      shipments.add(index, aShipment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShipmentAt(Shipment aShipment, int index)
  {
    boolean wasAdded = false;
    if(shipments.contains(aShipment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipments()) { index = numberOfShipments() - 1; }
      shipments.remove(aShipment);
      shipments.add(index, aShipment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShipmentAt(aShipment, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public OrderItem addOrderItem(int aQuantity, Order aOrder, Item aItem)
  {
    return new OrderItem(aQuantity, this, aOrder, aItem);
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aOrderItem.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aOrderItem.setGroceryManagementSystem(this);
    }
    else
    {
      orderItems.add(aOrderItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderItem(OrderItem aOrderItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderItem, as it must always have a groceryManagementSystem
    if (!this.equals(aOrderItem.getGroceryManagementSystem()))
    {
      orderItems.remove(aOrderItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderItemAt(OrderItem aOrderItem, int index)
  {  
    boolean wasAdded = false;
    if(addOrderItem(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderItemAt(OrderItem aOrderItem, int index)
  {
    boolean wasAdded = false;
    if(orderItems.contains(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderItemAt(aOrderItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShipmentItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShipmentItem addShipmentItem(int aQuantity, Shipment aShipment, Item aItem)
  {
    return new ShipmentItem(aQuantity, this, aShipment, aItem);
  }

  public boolean addShipmentItem(ShipmentItem aShipmentItem)
  {
    boolean wasAdded = false;
    if (shipmentItems.contains(aShipmentItem)) { return false; }
    GroceryManagementSystem existingGroceryManagementSystem = aShipmentItem.getGroceryManagementSystem();
    boolean isNewGroceryManagementSystem = existingGroceryManagementSystem != null && !this.equals(existingGroceryManagementSystem);
    if (isNewGroceryManagementSystem)
    {
      aShipmentItem.setGroceryManagementSystem(this);
    }
    else
    {
      shipmentItems.add(aShipmentItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShipmentItem(ShipmentItem aShipmentItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aShipmentItem, as it must always have a groceryManagementSystem
    if (!this.equals(aShipmentItem.getGroceryManagementSystem()))
    {
      shipmentItems.remove(aShipmentItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShipmentItemAt(ShipmentItem aShipmentItem, int index)
  {  
    boolean wasAdded = false;
    if(addShipmentItem(aShipmentItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipmentItems()) { index = numberOfShipmentItems() - 1; }
      shipmentItems.remove(aShipmentItem);
      shipmentItems.add(index, aShipmentItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShipmentItemAt(ShipmentItem aShipmentItem, int index)
  {
    boolean wasAdded = false;
    if(shipmentItems.contains(aShipmentItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipmentItems()) { index = numberOfShipmentItems() - 1; }
      shipmentItems.remove(aShipmentItem);
      shipmentItems.add(index, aShipmentItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShipmentItemAt(aShipmentItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (users.size() > 0)
    {
      User aUser = users.get(users.size() - 1);
      aUser.delete();
      users.remove(aUser);
    }
    
    while (employees.size() > 0)
    {
      Employee aEmployee = employees.get(employees.size() - 1);
      aEmployee.delete();
      employees.remove(aEmployee);
    }
    
    while (customers.size() > 0)
    {
      Customer aCustomer = customers.get(customers.size() - 1);
      aCustomer.delete();
      customers.remove(aCustomer);
    }
    
    Manager existingManager = manager;
    manager = null;
    if (existingManager != null)
    {
      existingManager.delete();
      existingManager.setGroceryManagementSystem(null);
    }
    while (orders.size() > 0)
    {
      Order aOrder = orders.get(orders.size() - 1);
      aOrder.delete();
      orders.remove(aOrder);
    }
    
    while (items.size() > 0)
    {
      Item aItem = items.get(items.size() - 1);
      aItem.delete();
      items.remove(aItem);
    }
    
    while (shipments.size() > 0)
    {
      Shipment aShipment = shipments.get(shipments.size() - 1);
      aShipment.delete();
      shipments.remove(aShipment);
    }
    
    while (orderItems.size() > 0)
    {
      OrderItem aOrderItem = orderItems.get(orderItems.size() - 1);
      aOrderItem.delete();
      orderItems.remove(aOrderItem);
    }
    
    while (shipmentItems.size() > 0)
    {
      ShipmentItem aShipmentItem = shipmentItems.get(shipmentItems.size() - 1);
      aShipmentItem.delete();
      shipmentItems.remove(aShipmentItem);
    }
    
  }

}