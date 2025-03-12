/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.util.*;

// line 32 "../../../../../../model.ump"
// line 108 "../../../../../../model.ump"
public class Employee extends UserRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Employee Associations
  private GroceryManagementSystem groceryManagementSystem;
  private List<Order> ordersAssigned;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Employee(User aUser, GroceryManagementSystem aGroceryManagementSystem)
  {
    super(aUser);
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create employee due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    ordersAssigned = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
  }
  /* Code from template association_GetMany */
  public Order getOrdersAssigned(int index)
  {
    Order aOrdersAssigned = ordersAssigned.get(index);
    return aOrdersAssigned;
  }

  public List<Order> getOrdersAssigned()
  {
    List<Order> newOrdersAssigned = Collections.unmodifiableList(ordersAssigned);
    return newOrdersAssigned;
  }

  public int numberOfOrdersAssigned()
  {
    int number = ordersAssigned.size();
    return number;
  }

  public boolean hasOrdersAssigned()
  {
    boolean has = ordersAssigned.size() > 0;
    return has;
  }

  public int indexOfOrdersAssigned(Order aOrdersAssigned)
  {
    int index = ordersAssigned.indexOf(aOrdersAssigned);
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
      existingGroceryManagementSystem.removeEmployee(this);
    }
    groceryManagementSystem.addEmployee(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrdersAssigned()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addOrdersAssigned(Order aOrdersAssigned)
  {
    boolean wasAdded = false;
    if (ordersAssigned.contains(aOrdersAssigned)) { return false; }
    Employee existingOrderAssignee = aOrdersAssigned.getOrderAssignee();
    if (existingOrderAssignee == null)
    {
      aOrdersAssigned.setOrderAssignee(this);
    }
    else if (!this.equals(existingOrderAssignee))
    {
      existingOrderAssignee.removeOrdersAssigned(aOrdersAssigned);
      addOrdersAssigned(aOrdersAssigned);
    }
    else
    {
      ordersAssigned.add(aOrdersAssigned);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrdersAssigned(Order aOrdersAssigned)
  {
    boolean wasRemoved = false;
    if (ordersAssigned.contains(aOrdersAssigned))
    {
      ordersAssigned.remove(aOrdersAssigned);
      aOrdersAssigned.setOrderAssignee(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrdersAssignedAt(Order aOrdersAssigned, int index)
  {  
    boolean wasAdded = false;
    if(addOrdersAssigned(aOrdersAssigned))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrdersAssigned()) { index = numberOfOrdersAssigned() - 1; }
      ordersAssigned.remove(aOrdersAssigned);
      ordersAssigned.add(index, aOrdersAssigned);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrdersAssignedAt(Order aOrdersAssigned, int index)
  {
    boolean wasAdded = false;
    if(ordersAssigned.contains(aOrdersAssigned))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrdersAssigned()) { index = numberOfOrdersAssigned() - 1; }
      ordersAssigned.remove(aOrdersAssigned);
      ordersAssigned.add(index, aOrdersAssigned);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrdersAssignedAt(aOrdersAssigned, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeEmployee(this);
    }
    while( !ordersAssigned.isEmpty() )
    {
      ordersAssigned.get(0).setOrderAssignee(null);
    }
    super.delete();
  }

}