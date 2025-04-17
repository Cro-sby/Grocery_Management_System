//%% NEW FILE TOItem BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller.TOs;
import java.util.*;

// line 4 "../../../../../../../model.ump"
// line 20 "../../../../../../../model.ump"
public class TOItem
{

    //------------------------
    // STATIC VARIABLES
    //------------------------

    private static Map<String, TOItem> toitemsByName = new HashMap<String, TOItem>();

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //TOItem Attributes
    private String name;
    private int quantityInInventory;
    private int price;
    private boolean isPerishable;
    private int numberOfPoints;

    //Helper Variables
    private boolean canSetName;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public TOItem(String aName, int aQuantityInInventory, int aPrice, boolean aIsPerishable, int aNumberOfPoints)
    {
        canSetName = true;
        quantityInInventory = aQuantityInInventory;
        price = aPrice;
        isPerishable = aIsPerishable;
        numberOfPoints = aNumberOfPoints;
        if (!setName(aName))
        {
            throw new RuntimeException("Cannot create due to duplicate name. See https://manual.umple.org?RE003ViolationofUniqueness.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template attribute_SetImmutable */
    public boolean setName(String aName)
    {
        boolean wasSet = false;
        if (!canSetName) { return false; }
        String anOldName = getName();
        if (anOldName != null && anOldName.equals(aName)) {
            return true;
        }
        if (hasWithName(aName)) {
            return wasSet;
        }
        canSetName = false;
        name = aName;
        wasSet = true;
        if (anOldName != null) {
            toitemsByName.remove(anOldName);
        }
        toitemsByName.put(aName, this);
        return wasSet;
    }

    public String getName()
    {
        return name;
    }
    /* Code from template attribute_GetUnique */
    public static TOItem getWithName(String aName)
    {
        return toitemsByName.get(aName);
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

    public void delete()
    {
        toitemsByName.remove(getName());
    }


    public String toString()
    {
        return super.toString() + "["+
                "name" + ":" + getName()+ "," +
                "quantityInInventory" + ":" + getQuantityInInventory()+ "," +
                "price" + ":" + getPrice()+ "," +
                "isPerishable" + ":" + getIsPerishable()+ "," +
                "numberOfPoints" + ":" + getNumberOfPoints()+ "]";
    }
}