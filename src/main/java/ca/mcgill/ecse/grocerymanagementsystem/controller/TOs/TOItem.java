//%% NEW FILE TOItem BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller.TOs;

// line 3 "../../../../../../../model.ump"
// line 13 "../../../../../../../model.ump"
public class TOItem
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //TOItem Attributes
    private String name;
    private int quantityInInventory;
    private int price;
    private boolean isPerishable;
    private int numberOfPoints;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public TOItem(String aName, int aQuantityInInventory, int aPrice, boolean aIsPerishable, int aNumberOfPoints)
    {
        name = aName;
        quantityInInventory = aQuantityInInventory;
        price = aPrice;
        isPerishable = aIsPerishable;
        numberOfPoints = aNumberOfPoints;
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setName(String aName)
    {
        boolean wasSet = false;
        name = aName;
        wasSet = true;
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

    public int getQuantityInInventory()
    {
        return quantityInInventory;
    }

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
    {}


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