/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.util.*;
import java.sql.Date;

// line 26 "../../../../../../model.ump"
// line 103 "../../../../../../model.ump"
public class Customer extends UserRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Attributes
  private String address;
  private int numberOfPoints;

  //Customer Associations
  private GroceryManagementSystem groceryManagementSystem;
  private List<Order> ordersPlaced;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(User aUser, String aAddress, int aNumberOfPoints, GroceryManagementSystem aGroceryManagementSystem)
  {
    super(aUser);
    address = aAddress;
    numberOfPoints = aNumberOfPoints;
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create customer due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    ordersPlaced = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
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

  public String getAddress()
  {
    return address;
  }

  public int getNumberOfPoints()
  {
    return numberOfPoints;
  }
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
  }
  /* Code from template association_GetMany */
  public Order getOrdersPlaced(int index)
  {
    Order aOrdersPlaced = ordersPlaced.get(index);
    return aOrdersPlaced;
  }

  public List<Order> getOrdersPlaced()
  {
    List<Order> newOrdersPlaced = Collections.unmodifiableList(ordersPlaced);
    return newOrdersPlaced;
  }

  public int numberOfOrdersPlaced()
  {
    int number = ordersPlaced.size();
    return number;
  }

  public boolean hasOrdersPlaced()
  {
    boolean has = ordersPlaced.size() > 0;
    return has;
  }

  public int indexOfOrdersPlaced(Order aOrdersPlaced)
  {
    int index = ordersPlaced.indexOf(aOrdersPlaced);
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
      existingGroceryManagementSystem.removeCustomer(this);
    }
    groceryManagementSystem.addCustomer(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrdersPlaced()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrdersPlaced(Date aDatePlaced, Order.DeliveryDeadline aDeadline, GroceryManagementSystem aGroceryManagementSystem)
  {
    return new Order(aDatePlaced, aDeadline, aGroceryManagementSystem, this);
  }

  public boolean addOrdersPlaced(Order aOrdersPlaced)
  {
    boolean wasAdded = false;
    if (ordersPlaced.contains(aOrdersPlaced)) { return false; }
    Customer existingOrderPlacer = aOrdersPlaced.getOrderPlacer();
    boolean isNewOrderPlacer = existingOrderPlacer != null && !this.equals(existingOrderPlacer);
    if (isNewOrderPlacer)
    {
      aOrdersPlaced.setOrderPlacer(this);
    }
    else
    {
      ordersPlaced.add(aOrdersPlaced);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrdersPlaced(Order aOrdersPlaced)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrdersPlaced, as it must always have a orderPlacer
    if (!this.equals(aOrdersPlaced.getOrderPlacer()))
    {
      ordersPlaced.remove(aOrdersPlaced);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrdersPlacedAt(Order aOrdersPlaced, int index)
  {  
    boolean wasAdded = false;
    if(addOrdersPlaced(aOrdersPlaced))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrdersPlaced()) { index = numberOfOrdersPlaced() - 1; }
      ordersPlaced.remove(aOrdersPlaced);
      ordersPlaced.add(index, aOrdersPlaced);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrdersPlacedAt(Order aOrdersPlaced, int index)
  {
    boolean wasAdded = false;
    if(ordersPlaced.contains(aOrdersPlaced))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrdersPlaced()) { index = numberOfOrdersPlaced() - 1; }
      ordersPlaced.remove(aOrdersPlaced);
      ordersPlaced.add(index, aOrdersPlaced);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrdersPlacedAt(aOrdersPlaced, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeCustomer(this);
    }
    for(int i=ordersPlaced.size(); i > 0; i--)
    {
      Order aOrdersPlaced = ordersPlaced.get(i - 1);
      aOrdersPlaced.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "address" + ":" + getAddress()+ "," +
            "numberOfPoints" + ":" + getNumberOfPoints()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "groceryManagementSystem = "+(getGroceryManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getGroceryManagementSystem())):"null");
  }
}