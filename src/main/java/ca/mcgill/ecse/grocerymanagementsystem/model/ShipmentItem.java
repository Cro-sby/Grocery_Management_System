/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;

// line 66 "../../../../../model.ump"
// line 138 "../../../../../model.ump"
public class ShipmentItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ShipmentItem Attributes
  private int quantity;

  //ShipmentItem Associations
  private GroceryManagementSystem groceryManagementSystem;
  private Shipment shipment;
  private Item item;

  //Helper Variables
  private int cachedHashCode;
  private boolean canSetShipment;
  private boolean canSetItem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ShipmentItem(int aQuantity, GroceryManagementSystem aGroceryManagementSystem, Shipment aShipment, Item aItem)
  {
    cachedHashCode = -1;
    canSetShipment = true;
    canSetItem = true;
    quantity = aQuantity;
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create shipmentItem due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddShipment = setShipment(aShipment);
    if (!didAddShipment)
    {
      throw new RuntimeException("Unable to create shipmentItem due to shipment. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddItem = setItem(aItem);
    if (!didAddItem)
    {
      throw new RuntimeException("Unable to create shipmentItem due to item. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setQuantity(int aQuantity)
  {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public int getQuantity()
  {
    return quantity;
  }
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
  }
  /* Code from template association_GetOne */
  public Shipment getShipment()
  {
    return shipment;
  }
  /* Code from template association_GetOne */
  public Item getItem()
  {
    return item;
  }
  /* Code from template association_SetOneToManyAssociationClass */
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
      existingGroceryManagementSystem.removeShipmentItem(this);
    }
    if (!groceryManagementSystem.addShipmentItem(this))
    {
      groceryManagementSystem = existingGroceryManagementSystem;
      wasSet = false;
    }
    else
    {
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToManyAssociationClass */
  public boolean setShipment(Shipment aShipment)
  {
    boolean wasSet = false;
    if (!canSetShipment) { return false; }
    if (aShipment == null)
    {
      return wasSet;
    }

    Shipment existingShipment = shipment;
    shipment = aShipment;
    if (existingShipment != null && !existingShipment.equals(aShipment))
    {
      existingShipment.removeShipmentItem(this);
    }
    if (!shipment.addShipmentItem(this))
    {
      shipment = existingShipment;
      wasSet = false;
    }
    else
    {
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToManyAssociationClass */
  public boolean setItem(Item aItem)
  {
    boolean wasSet = false;
    if (!canSetItem) { return false; }
    if (aItem == null)
    {
      return wasSet;
    }

    Item existingItem = item;
    item = aItem;
    if (existingItem != null && !existingItem.equals(aItem))
    {
      existingItem.removeShipmentItem(this);
    }
    if (!item.addShipmentItem(this))
    {
      item = existingItem;
      wasSet = false;
    }
    else
    {
      wasSet = true;
    }
    return wasSet;
  }

  public boolean equals(Object obj)
  {
    if (obj == null) { return false; }
    if (!getClass().equals(obj.getClass())) { return false; }

    ShipmentItem compareTo = (ShipmentItem)obj;
  
    if (getShipment() == null && compareTo.getShipment() != null)
    {
      return false;
    }
    else if (getShipment() != null && !getShipment().equals(compareTo.getShipment()))
    {
      return false;
    }

    if (getItem() == null && compareTo.getItem() != null)
    {
      return false;
    }
    else if (getItem() != null && !getItem().equals(compareTo.getItem()))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    if (cachedHashCode != -1)
    {
      return cachedHashCode;
    }
    cachedHashCode = 17;
    if (getShipment() != null)
    {
      cachedHashCode = cachedHashCode * 23 + getShipment().hashCode();
    }
    else
    {
      cachedHashCode = cachedHashCode * 23;
    }
    if (getItem() != null)
    {
      cachedHashCode = cachedHashCode * 23 + getItem().hashCode();
    }
    else
    {
      cachedHashCode = cachedHashCode * 23;
    }

    canSetShipment = false;
    canSetItem = false;
    return cachedHashCode;
  }

  public void delete()
  {
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeShipmentItem(this);
    }
    Shipment placeholderShipment = shipment;
    this.shipment = null;
    if(placeholderShipment != null)
    {
      placeholderShipment.removeShipmentItem(this);
    }
    Item placeholderItem = item;
    this.item = null;
    if(placeholderItem != null)
    {
      placeholderItem.removeShipmentItem(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "quantity" + ":" + getQuantity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "groceryManagementSystem = "+(getGroceryManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getGroceryManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "shipment = "+(getShipment()!=null?Integer.toHexString(System.identityHashCode(getShipment())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "item = "+(getItem()!=null?Integer.toHexString(System.identityHashCode(getItem())):"null");
  }
}