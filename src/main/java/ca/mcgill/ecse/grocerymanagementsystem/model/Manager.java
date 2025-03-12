/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;

// line 36 "../../../../../../model.ump"
// line 113 "../../../../../../model.ump"
public class Manager extends UserRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Manager Associations
  private GroceryManagementSystem groceryManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Manager(User aUser, GroceryManagementSystem aGroceryManagementSystem)
  {
    super(aUser);
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create manager due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setGroceryManagementSystem(GroceryManagementSystem aNewGroceryManagementSystem)
  {
    boolean wasSet = false;
    if (aNewGroceryManagementSystem == null)
    {
      //Unable to setGroceryManagementSystem to null, as manager must always be associated to a groceryManagementSystem
      return wasSet;
    }
    
    Manager existingManager = aNewGroceryManagementSystem.getManager();
    if (existingManager != null && !equals(existingManager))
    {
      //Unable to setGroceryManagementSystem, the current groceryManagementSystem already has a manager, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    GroceryManagementSystem anOldGroceryManagementSystem = groceryManagementSystem;
    groceryManagementSystem = aNewGroceryManagementSystem;
    groceryManagementSystem.setManager(this);

    if (anOldGroceryManagementSystem != null)
    {
      anOldGroceryManagementSystem.setManager(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    GroceryManagementSystem existingGroceryManagementSystem = groceryManagementSystem;
    groceryManagementSystem = null;
    if (existingGroceryManagementSystem != null)
    {
      existingGroceryManagementSystem.setManager(null);
    }
    super.delete();
  }

}