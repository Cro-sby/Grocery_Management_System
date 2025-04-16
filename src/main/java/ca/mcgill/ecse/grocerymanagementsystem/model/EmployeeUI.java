package ca.mcgill.ecse.grocerymanagementsystem.model;

//%% NEW FILE Employee BEGINS HERE %%


// line 2 "model.ump"
// line 10 "model.ump"
public class EmployeeUI
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Employee Attributes
    private String username;
    private String name;
    private String phoneNumber;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public EmployeeUI(String aUsername, String aName, String aPhoneNumber)
    {
        username = aUsername;
        name = aName;
        phoneNumber = aPhoneNumber;
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

    public String getUsername()
    {
        return username;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void delete()
    {}


    public String toString()
    {
        return super.toString() + "["+
                "username" + ":" + getUsername()+ "," +
                "name" + ":" + getName()+ "," +
                "phoneNumber" + ":" + getPhoneNumber()+ "]";
    }
}