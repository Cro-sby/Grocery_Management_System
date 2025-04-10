//%% NEW FILE Order BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.sql.Date;
import java.util.*;

// line 3 "../../../../../../Untitled.ump"
// line 354 "../../../../../../Untitled.ump"
// line 42 "../../../../../../model.ump"
// line 125 "../../../../../../model.ump"
public class Order
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DeliveryDeadline { SameDay, InOneDay, InTwoDays, InThreeDays }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextOrderNumber = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int deliveryDelay;
  private Date datePlaced;
  private DeliveryDeadline deadline;
  private int totalCost;
  private int pricePaid;

  //Autounique Attributes
  private int orderNumber;

  //Order State Machines
  public enum Status { under_construction, pending, placed, in_preparation, ready_for_delivery, delivered, cancelled }
  private Status status;

  //Order Associations
  private GroceryManagementSystem groceryManagementSystem;
  private List<OrderItem> orderItems;
  private Customer orderPlacer;
  private Employee orderAssignee;

  //Helper Variables
  private boolean canSetTotalCost;
  private boolean canSetPricePaid;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aDatePlaced, DeliveryDeadline aDeadline, GroceryManagementSystem aGroceryManagementSystem, Customer aOrderPlacer)
  {
    deliveryDelay = 0;
    datePlaced = aDatePlaced;
    deadline = aDeadline;
    canSetTotalCost = true;
    canSetPricePaid = true;
    orderNumber = nextOrderNumber++;
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create order due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderItems = new ArrayList<OrderItem>();
    boolean didAddOrderPlacer = setOrderPlacer(aOrderPlacer);
    if (!didAddOrderPlacer)
    {
      throw new RuntimeException("Unable to create ordersPlaced due to orderPlacer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.under_construction);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDeliveryDelay(int aDeliveryDelay)
  {
    boolean wasSet = false;
    deliveryDelay = aDeliveryDelay;
    wasSet = true;
    return wasSet;
  }

  public boolean setDatePlaced(Date aDatePlaced)
  {
    boolean wasSet = false;
    datePlaced = aDatePlaced;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeadline(DeliveryDeadline aDeadline)
  {
    boolean wasSet = false;
    deadline = aDeadline;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetImmutable */
  public boolean setTotalCost(int aTotalCost)
  {
    boolean wasSet = false;
    if (!canSetTotalCost) { return false; }
    canSetTotalCost = false;
    totalCost = aTotalCost;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetImmutable */
  public boolean setPricePaid(int aPricePaid)
  {
    boolean wasSet = false;
    if (!canSetPricePaid) { return false; }
    canSetPricePaid = false;
    pricePaid = aPricePaid;
    wasSet = true;
    return wasSet;
  }

  /**
   * Define deliveryDelay here. It will be merged into the Order class during generation.
   * Default to 0
   */
  public int getDeliveryDelay()
  {
    return deliveryDelay;
  }

  public Date getDatePlaced()
  {
    return datePlaced;
  }

  public DeliveryDeadline getDeadline()
  {
    return deadline;
  }

  /**
   * Total cost of the order, without considering points.
   */
  public int getTotalCost()
  {
    return totalCost;
  }

  /**
   * Amount that the customer actually had to pay for the order.
   * This depends on both the total cost and whether or not the customer decided to use their points.
   */
  public int getPricePaid()
  {
    return pricePaid;
  }

  public int getOrderNumber()
  {
    return orderNumber;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean removeItem(Item item)
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case under_construction:
        if (isItemInOrder(item))
        {
          // line 13 "../../../../../../Untitled.ump"
          removeOrderItem(findOrderItem(item));
          setStatus(Status.under_construction);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startOrder(DeliveryDeadline chosenDeadline,int delayDays)
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case under_construction:
        // line 18 "../../../../../../Untitled.ump"
        setDeadline(chosenDeadline);
        setDeliveryDelay(delayDays);
        setStatus(Status.under_construction);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addItem(Item item,int quantity)
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case under_construction:
        if (!(isItemInOrder(item))&&isValidQuantity(item,quantity)&&isInventorySufficient(item,quantity)&&isCartNotFull())
        {
          // line 26 "../../../../../../Untitled.ump"
          addOrderItem(quantity, getGroceryManagementSystem(), item);
          setStatus(Status.under_construction);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean changeQuantity(Item item,int newQuantity)
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case under_construction:
        if (isItemInOrder(item)&&isValidQuantityForChange(item,newQuantity))
        {
          // line 33 "../../../../../../Untitled.ump"
          doUpdateItemQuantityAction(item, newQuantity);
          setStatus(Status.under_construction);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean checkout()
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case under_construction:
        if (hasItems())
        {
          // line 39 "../../../../../../Untitled.ump"
          calculateOrderCost();
          setStatus(Status.pending);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean payOrder(double amount,int points)
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case pending:
        if (isPaymentValid(amount,points)&&isInventorySufficientForAllItems())
        {
          // line 51 "../../../../../../Untitled.ump"
          doProcessPaymentAction(amount, points);
          setStatus(Status.placed);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean paymentDeclined()
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case pending:
        setStatus(Status.pending);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancel()
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case pending:
        // line 57 "../../../../../../Untitled.ump"
        doCancelOrder();
        setStatus(Status.cancelled);
        wasEventProcessed = true;
        break;
      case placed:
        // line 68 "../../../../../../Untitled.ump"
        doCancelOrder();
        setStatus(Status.cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean assignEmployee(Employee employee)
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case placed:
        if (!(isEmployeeAssigned()))
        {
          // line 65 "../../../../../../Untitled.ump"
          setOrderAssignee(employee);
          setStatus(Status.in_preparation);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finishAssembly()
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case in_preparation:
        if (canAssemble())
        {
          setStatus(Status.ready_for_delivery);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean completeDelivery()
  {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus)
    {
      case ready_for_delivery:
        if (isDeliveryDateValid())
        {
          setStatus(Status.delivered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public void setStatus(Status aStatus)
  {
    status = aStatus;
  }
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
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
  /* Code from template association_GetOne */
  public Customer getOrderPlacer()
  {
    return orderPlacer;
  }
  /* Code from template association_GetOne */
  public Employee getOrderAssignee()
  {
    return orderAssignee;
  }

  public boolean hasOrderAssignee()
  {
    boolean has = orderAssignee != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGroceryManagementSystem(GroceryManagementSystem aGroceryManagementSystem)
  {
    boolean wasSet = false;
    if (aGroceryManagementSystem == null)
    {
      return wasSet;
    }

    GroceryManagementSystem existingGroceryManagementSystem = groceryManagementSystem;
    groceryManagementSystem = aGroceryManagementSystem;
    if (existingGroceryManagementSystem != null && !existingGroceryManagementSystem.equals(aGroceryManagementSystem))
    {
      existingGroceryManagementSystem.removeOrder(this);
    }
    groceryManagementSystem.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public OrderItem addOrderItem(int aQuantity, GroceryManagementSystem aGroceryManagementSystem, Item aItem)
  {
    return new OrderItem(aQuantity, aGroceryManagementSystem, this, aItem);
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    Order existingOrder = aOrderItem.getOrder();
    boolean isNewOrder = existingOrder != null && !this.equals(existingOrder);
    if (isNewOrder)
    {
      aOrderItem.setOrder(this);
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
    //Unable to remove aOrderItem, as it must always have a order
    if (!this.equals(aOrderItem.getOrder()))
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
  /* Code from template association_SetOneToMany */
  public boolean setOrderPlacer(Customer aOrderPlacer)
  {
    boolean wasSet = false;
    if (aOrderPlacer == null)
    {
      return wasSet;
    }

    Customer existingOrderPlacer = orderPlacer;
    orderPlacer = aOrderPlacer;
    if (existingOrderPlacer != null && !existingOrderPlacer.equals(aOrderPlacer))
    {
      existingOrderPlacer.removeOrdersPlaced(this);
    }
    orderPlacer.addOrdersPlaced(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrderAssignee(Employee aOrderAssignee)
  {
    boolean wasSet = false;
    Employee existingOrderAssignee = orderAssignee;
    orderAssignee = aOrderAssignee;
    if (existingOrderAssignee != null && !existingOrderAssignee.equals(aOrderAssignee))
    {
      existingOrderAssignee.removeOrdersAssigned(this);
    }
    if (aOrderAssignee != null)
    {
      aOrderAssignee.addOrdersAssigned(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeOrder(this);
    }
    for(int i=orderItems.size(); i > 0; i--)
    {
      OrderItem aOrderItem = orderItems.get(i - 1);
      aOrderItem.delete();
    }
    Customer placeholderOrderPlacer = orderPlacer;
    this.orderPlacer = null;
    if(placeholderOrderPlacer != null)
    {
      placeholderOrderPlacer.removeOrdersPlaced(this);
    }
    if (orderAssignee != null)
    {
      Employee placeholderOrderAssignee = orderAssignee;
      this.orderAssignee = null;
      placeholderOrderAssignee.removeOrdersAssigned(this);
    }
  }


  /**
   * ========== GUARD METHODS ==========
   * Implementations use generated model methods where possible
   */
  // line 100 "../../../../../../Untitled.ump"
  private boolean isItemInOrder(Item item){
    return findOrderItem(item) != null;
  }

  // line 104 "../../../../../../Untitled.ump"
  private boolean isValidQuantity(Item item, int quantity){
    return quantity > 0 && quantity <= 10;
  }

  // line 108 "../../../../../../Untitled.ump"
  private boolean isValidQuantityForChange(Item item, int quantity){
    return quantity >= 0 && quantity <= 10;  // Allows 0 for removal
  }

  // line 113 "../../../../../../Untitled.ump"
  private boolean isInventorySufficient(Item item, int requestedQuantity){
    if (item == null) return false;
    // Uses generated Item.getQuantityInInventory()
    return item.getQuantityInInventory() >= requestedQuantity;
  }

  // line 120 "../../../../../../Untitled.ump"
  private boolean isCartNotFull(){
    // Uses generated Order.numberOfOrderItems()
    return numberOfOrderItems() < 50;
  }

  // line 125 "../../../../../../Untitled.ump"
  public boolean hasItems(){
    // Uses generated Order.hasOrderItems()
    return hasOrderItems();
  }

  // line 131 "../../../../../../Untitled.ump"
  private boolean isPaymentValid(double amount, int points){
    // Uses generated Order.canSetTotalCost and Order.getTotalCost()
    boolean costIsSet = !canSetTotalCost;

    if (costIsSet) {
      double totalCostDollars = getTotalCost() / 100.0;
      double pointsValueDollars = points * 0.01;
      // Use tolerance for float comparison
      return amount + pointsValueDollars >= totalCostDollars - 0.001;
    }
    else {
      return false; // Cannot validate if cost not set
    }
  }

  // line 148 "../../../../../../Untitled.ump"
  private boolean isEmployeeAssigned(){
    // Uses generated Order.hasOrderAssignee()
    return hasOrderAssignee();
  }

  // line 154 "../../../../../../Untitled.ump"
  private boolean canAssemble(){
    if (!containsPerishableItems()) {
      return true;
    }
    return isDeliveryDateValid();
  }

  // line 163 "../../../../../../Untitled.ump"
  private boolean isDeliveryDateValid(){
    java.sql.Date deliveryDate = calculateTargetDeliveryDate();
    if (deliveryDate == null) return false;
    java.time.LocalDate today = java.time.LocalDate.now();
    java.time.LocalDate deliveryLocalDate = deliveryDate.toLocalDate();
    return today.equals(deliveryLocalDate);
  }


  /**
   * Uses the deliveryDelay attribute defined in this file
   */
  // line 174 "../../../../../../Untitled.ump"
  public java.sql.Date calculateTargetDeliveryDate(){
    // Uses generated Order.getDatePlaced(), Order.getDeadline()
    java.sql.Date placed = getDatePlaced();
    DeliveryDeadline deadline = getDeadline();
    // Uses getter for deliveryDelay defined here
    int delay = getDeliveryDelay();

    if (placed == null || deadline == null) return null;

    java.time.LocalDate placedLocalDate = placed.toLocalDate();
    java.time.LocalDate targetLocalDate = placedLocalDate.plusDays(delay); // Apply integer delay first

    switch (deadline) { // Apply enum delay
      case InOneDay:  targetLocalDate = targetLocalDate.plusDays(1);
        break;
      case InTwoDays: targetLocalDate = targetLocalDate.plusDays(2);
        break;
      case InThreeDays: targetLocalDate = targetLocalDate.plusDays(3);
        break;
      case SameDay: default: break;
    }
    return java.sql.Date.valueOf(targetLocalDate);
  }

  // line 200 "../../../../../../Untitled.ump"
  private boolean containsPerishableItems(){
    // Uses generated Order.getOrderItems(), OrderItem.getItem(), Item.getIsPerishable()
    for (OrderItem orderItem : getOrderItems()) {
      if (orderItem.getItem().getIsPerishable()) {
        return true;
      }
    }
    return false;
  }

  // line 212 "../../../../../../Untitled.ump"
  private boolean isInventorySufficientForAllItems(){
    // Uses generated Order.getOrderItems(), OrderItem.getItem(), Item.getQuantityInInventory(), OrderItem.getQuantity()
    for (OrderItem oi : getOrderItems()) {
      if (oi.getItem().getQuantityInInventory() < oi.getQuantity()) {
        return false;
      }
    }
    return true;
  }


  /**
   * ========== ACTION METHODS ==========
   * Necessary helpers with logic implemented using model methods
   */
  // line 227 "../../../../../../Untitled.ump"
  private void doUpdateItemQuantityAction(Item item, int newQuantity){
    OrderItem orderItemToUpdate = findOrderItem(item);
    if (orderItemToUpdate != null) {
      if (newQuantity == 0) {
        // Uses generated Order.removeOrderItem()
        removeOrderItem(orderItemToUpdate);
      }
      else {
        // Uses generated OrderItem.setQuantity()
        orderItemToUpdate.setQuantity(newQuantity);
      }
    }
  }


  /**
   * Uses the deliveryDelay attribute defined in this file
   */
  // line 244 "../../../../../../Untitled.ump"
  private void calculateOrderCost(){
    double totalCostCents = 0;
    // Uses generated Order.getOrderItems(), OrderItem.getItem(), OrderItem.getQuantity(), Item.getPrice()
    for (OrderItem orderItem : getOrderItems()) {
      Item item = orderItem.getItem();
      int quantity = orderItem.getQuantity();
      int itemPriceCents = item.getPrice();
      double discountPercentage = Math.min(0.45, (quantity - 1) * 0.05);
      double discountedPriceCents = itemPriceCents * (1.0 - discountPercentage);
      totalCostCents += quantity * discountedPriceCents;
    }

    // Uses getter for deliveryDelay defined here and generated Order.getDeadline()
    if (getDeliveryDelay() == 0 && getDeadline() == DeliveryDeadline.SameDay) {
      totalCostCents += 500;
    }
    setTotalCost((int)Math.round(totalCostCents));
    // Uses generated Order.setTotalCost()

  }
  private void doProcessPaymentAction(double amount, int points){
    // Uses generated Order.getOrderPlacer()
    Customer customer = getOrderPlacer();
    if (customer == null) return;

    // Uses generated Order.getTotalCost()
    int calculatedTotalCost = getTotalCost();
    int priceToPay = calculatedTotalCost;
    int pointsUsed = 0;

    if (points > 0) {
      // Uses generated Customer.getNumberOfPoints()
      int availablePoints = customer.getNumberOfPoints();
      int discountInCents = Math.min(calculatedTotalCost, availablePoints);
      priceToPay -= discountInCents;
      pointsUsed = discountInCents;
      // Uses generated Customer.setNumberOfPoints()
      customer.setNumberOfPoints(availablePoints - pointsUsed);
    }
    // Uses generated Order.setPricePaid()
    setPricePaid(priceToPay);

    int pointsAwarded = 0;
    // Uses generated Order.getOrderItems(), OrderItem.ggeetItem(), Item.getNumberOfPoints(), OrderItem.getQuantity()
    for (OrderItem oi : getOrderItems()) {
      pointsAwarded += oi.getItem().getNumberOfPoints() * oi.getQuantity();
    }
    // Uses generated Customer.setNumberOfPoints(), Customer.getNumberOfPoints()
    customer.setNumberOfPoints(customer.getNumberOfPoints() + pointsAwarded);

    updateInventory(); // Calls helper action method

    // Uses generated Order.setDatePlaced()
    setDatePlaced(new java.sql.Date(System.currentTimeMillis()));
  }

  // line 305 "../../../../../../Untitled.ump"
  private void updateInventory(){
    // Uses generated Order.getOrderItems(), OrderItem.getItem(), OrderItem.getQuantity(), Item.setQuantityInInventory(), Item.getQuantityInInventory()
    for (OrderItem orderItem : getOrderItems()) {
      Item item = orderItem.getItem();
      int quantity = orderItem.getQuantity();
      item.setQuantityInInventory(item.getQuantityInInventory() - quantity);
    }
  }

  // line 316 "../../../../../../Untitled.ump"
  private void restoreInventory(){
    // Uses generated Order.getOrderItems(), OrderItem.getItem(), OrderItem.getQuantity(), Item.setQuantityInInventory(), Item.getQuantityInInventory()
    for (OrderItem orderItem : getOrderItems()) {
      Item item = orderItem.getItem();
      int quantity = orderItem.getQuantity();
      item.setQuantityInInventory(item.getQuantityInInventory() + quantity);
    }
  }

  // line 327 "../../../../../../Untitled.ump"
  private void doCancelOrder(){
    // Uses generated Order.getDatePlaced()
    if (getDatePlaced() != null) {
      restoreInventory(); // Calls helper action method
    }
  }


  /**
   * Helper method kept as it's used by multiple guards/actions
   */
  // line 337 "../../../../../../Untitled.ump"
  private OrderItem findOrderItem(Item item){
    if (item == null) return null;
    // Uses generated Order.getOrderItems(), OrderItem.getItem()
    for (OrderItem oi : getOrderItems()) {
      if (item.equals(oi.getItem())) {
        return oi;
      }
    }
    return null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "orderNumber" + ":" + getOrderNumber()+ "," +
            "deliveryDelay" + ":" + getDeliveryDelay()+ "," +
            "totalCost" + ":" + getTotalCost()+ "," +
            "pricePaid" + ":" + getPricePaid()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "datePlaced" + "=" + (getDatePlaced() != null ? !getDatePlaced().equals(this)  ? getDatePlaced().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deadline" + "=" + (getDeadline() != null ? !getDeadline().equals(this)  ? getDeadline().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "groceryManagementSystem = "+(getGroceryManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getGroceryManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderPlacer = "+(getOrderPlacer()!=null?Integer.toHexString(System.identityHashCode(getOrderPlacer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderAssignee = "+(getOrderAssignee()!=null?Integer.toHexString(System.identityHashCode(getOrderAssignee())):"null");
  }
}
