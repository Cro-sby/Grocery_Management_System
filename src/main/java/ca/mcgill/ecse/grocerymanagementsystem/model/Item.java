/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.util.*;

// line 47 "../../../../../../model.ump"
// line 123 "../../../../../../model.ump"
public class Item
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Item> itemsByName = new HashMap<String, Item>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Item Attributes
  private String name;
  private int quantityInInventory;
  private int price;
  private boolean isPerishable;
  private int numberOfPoints;

  //Item Associations
  private GroceryManagementSystem groceryManagementSystem;
  private List<OrderItem> orderItems;
  private List<ShipmentItem> shipmentItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Item(String aName, int aQuantityInInventory, int aPrice, boolean aIsPerishable, int aNumberOfPoints, GroceryManagementSystem aGroceryManagementSystem)
  {
    quantityInInventory = aQuantityInInventory;
    price = aPrice;
    isPerishable = aIsPerishable;
    numberOfPoints = aNumberOfPoints;
    if (!setName(aName))
    {
      throw new RuntimeException("Cannot create due to duplicate name. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create item due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderItems = new ArrayList<OrderItem>();
    shipmentItems = new ArrayList<ShipmentItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    String anOldName = getName();
    if (anOldName != null && anOldName.equals(aName)) {
      return true;
    }
    if (hasWithName(aName)) {
      return wasSet;
    }
    name = aName;
    wasSet = true;
    if (anOldName != null) {
      itemsByName.remove(anOldName);
    }
    itemsByName.put(aName, this);
    return wasSet;
  }

  public boolean setQuantityInInventory(int aQuantityInInventory)
  {
    boolean wasSet = false;
    quantityInInventory = aQuantityInInventory;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsPerishable(boolean aIsPerishable)
  {
    boolean wasSet = false;
    isPerishable = aIsPerishable;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberOfPoints(int aNumberOfPoints)
  {
    boolean wasSet = false;
    numberOfPoints = aNumberOfPoints;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template attribute_GetUnique */
  public static Item getWithName(String aName)
  {
    return itemsByName.get(aName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithName(String aName)
  {
    return getWithName(aName) != null;
  }

  public int getQuantityInInventory()
  {
    return quantityInInventory;
  }

  /**
   * in cents
   */
  public int getPrice()
  {
    return price;
  }

  public boolean getIsPerishable()
  {
    return isPerishable;
  }

  public int getNumberOfPoints()
  {
    return numberOfPoints;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsPerishable()
  {
    return isPerishable;
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
      existingGroceryManagementSystem.removeItem(this);
    }
    groceryManagementSystem.addItem(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public OrderItem addOrderItem(int aQuantity, GroceryManagementSystem aGroceryManagementSystem, Order aOrder)
  {
    return new OrderItem(aQuantity, aGroceryManagementSystem, aOrder, this);
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    Item existingItem = aOrderItem.getItem();
    boolean isNewItem = existingItem != null && !this.equals(existingItem);
    if (isNewItem)
    {
      aOrderItem.setItem(this);
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
    //Unable to remove aOrderItem, as it must always have a item
    if (!this.equals(aOrderItem.getItem()))
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
  public ShipmentItem addShipmentItem(int aQuantity, GroceryManagementSystem aGroceryManagementSystem, Shipment aShipment)
  {
    return new ShipmentItem(aQuantity, aGroceryManagementSystem, aShipment, this);
  }

  public boolean addShipmentItem(ShipmentItem aShipmentItem)
  {
    boolean wasAdded = false;
    if (shipmentItems.contains(aShipmentItem)) { return false; }
    Item existingItem = aShipmentItem.getItem();
    boolean isNewItem = existingItem != null && !this.equals(existingItem);
    if (isNewItem)
    {
      aShipmentItem.setItem(this);
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
    //Unable to remove aShipmentItem, as it must always have a item
    if (!this.equals(aShipmentItem.getItem()))
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
    itemsByName.remove(getName());
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeItem(this);
    }
    for(int i=orderItems.size(); i > 0; i--)
    {
      OrderItem aOrderItem = orderItems.get(i - 1);
      aOrderItem.delete();
    }
    for(int i=shipmentItems.size(); i > 0; i--)
    {
      ShipmentItem aShipmentItem = shipmentItems.get(i - 1);
      aShipmentItem.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "quantityInInventory" + ":" + getQuantityInInventory()+ "," +
            "price" + ":" + getPrice()+ "," +
            "isPerishable" + ":" + getIsPerishable()+ "," +
            "numberOfPoints" + ":" + getNumberOfPoints()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "groceryManagementSystem = "+(getGroceryManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getGroceryManagementSystem())):"null");
  }
}