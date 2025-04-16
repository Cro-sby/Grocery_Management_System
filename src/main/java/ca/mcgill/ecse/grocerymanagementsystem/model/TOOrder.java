package ca.mcgill.ecse.grocerymanagementsystem.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TOOrder {

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum DeliveryDeadline {SameDay, InOneDay, InTwoDays, InThreeDays}

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
    public enum Status {under_construction, pending, placed, in_preparation, ready_for_delivery, delivered, cancelled}

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

    public TOOrder(Date aDatePlaced, DeliveryDeadline aDeadline, GroceryManagementSystem aGroceryManagementSystem, Customer aOrderPlacer) {
        deliveryDelay = 0;
        datePlaced = aDatePlaced;
        deadline = aDeadline;
        canSetTotalCost = true;
        canSetPricePaid = true;
        orderNumber = nextOrderNumber++;
        orderPlacer = aOrderPlacer;
        orderItems = new ArrayList<OrderItem>();
        groceryManagementSystem = aGroceryManagementSystem;
        status = Status.under_construction;
    }

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
    public GroceryManagementSystem getGroceryManagementSystem()
    {
        return groceryManagementSystem;
    }
    public List<OrderItem> getOrderItems()
    {
        List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
        return newOrderItems;
    }
    public Customer getOrderPlacer()
    {
        return orderPlacer;
    }
    /* Code from template association_GetOne */
    public Employee getOrderAssignee()
    {
        return orderAssignee;
    }
}