package ca.mcgill.ecse.grocerymanagementsystem.model;

//%% NEW FILE Customer BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 2 "model.ump"
public class CustomerUI
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Customer Attributes
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private int loyaltyPoints;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public CustomerUI(String aUsername, String aPassword, String aName, String aPhoneNumber, String aAddress, int aLoyaltyPoints)
    {
        username = aUsername;
        password = aPassword;
        name = aName;
        phoneNumber = aPhoneNumber;
        address = aAddress;
        loyaltyPoints = aLoyaltyPoints;
    }



    //------------------------
    // INTERFACE
    //------------------------

    public boolean setUsername(String aUsername)
    {
        boolean wasSet = false;
        username = aUsername;
        wasSet = true;
        return wasSet;
    }

    public boolean setPassword(String aPassword)
    {
        boolean wasSet = false;
        password = aPassword;
        wasSet = true;
        return wasSet;
    }

    public boolean setName(String aName)
    {
        boolean wasSet = false;
        name = aName;
        wasSet = true;
        return wasSet;
    }

    public boolean setPhoneNumber(String aPhoneNumber)
    {
        boolean wasSet = false;
        phoneNumber = aPhoneNumber;
        wasSet = true;
        return wasSet;
    }

    public boolean setAddress(String aAddress)
    {
        boolean wasSet = false;
        address = aAddress;
        wasSet = true;
        return wasSet;
    }

    public boolean setLoyaltyPoints(int aLoyaltyPoints)
    {
        boolean wasSet = false;
        loyaltyPoints = aLoyaltyPoints;
        wasSet = true;
        return wasSet;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getAddress()
    {
        return address;
    }

    public int getLoyaltyPoints()
    {
        return loyaltyPoints;
    }

    public void delete()
    {}


    public String toString()
    {
        return super.toString() + "["+
                "username" + ":" + getUsername()+ "," +
                "password" + ":" + getPassword()+ "," +
                "name" + ":" + getName()+ "," +
                "phoneNumber" + ":" + getPhoneNumber()+ "," +
                "address" + ":" + getAddress()+ "," +
                "loyaltyPoints" + ":" + getLoyaltyPoints()+ "]";
    }
}