package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;

import ca.mcgill.ecse.grocerymanagementsystem.model.TOOrder;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class OrderPageController {
    public TextField CustomerUsername;
    public TextField DeadlineCreate;
    public TextField OrderNumberDel;
    public TextField OrderNumberAdd;
    public TextField ItemNameAdd;
    public TextField OrderNumberUp;
    public TextField ItemNameUp;
    public TextField QuantityUp;
    public TextField OrderNumberDelItem;
    public TextField ItemNameDelItem;
    public TextField OrderNumberCheck;
    public TextField OrderNumberPay;
    public TextField UsePoints;
    public TextField OrderNumberAss;
    public TextField EmployeeUsername;
    public TextField OrderNumberFinish;
    public TextField OrderNumberDeliver;
    public TextField OrderNumberCancel;

    @FXML
    private TableView<TOOrder> OrdersTable;
    @FXML
    private TableColumn<TOOrder, Integer> OrderNumCol;
    @FXML
    private TableColumn<TOOrder, String> CustomerCol;
    @FXML
    private TableColumn<TOOrder, Integer> TotalAmountCol;
    @FXML
    private TableColumn<TOOrder, TOOrder.DeliveryDeadline> DeadlineCol;
    @FXML
    private TableColumn<TOOrder, TOOrder.Status> StatusCol;
    @FXML
    private TableColumn<TOOrder, Employee> AssigneeCol;

    @FXML
    private void initialize() {
        // Explain how the table should populate the cells given a TOFlight
        OrderNumCol.setCellValueFactory(new PropertyValueFactory<>("OrderNumber"));
        CustomerCol.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getOrderPlacer();
            String username = (customer != null) ? customer.getUser().getUsername() : "Unknown";
            return new ReadOnlyStringWrapper(username);
        });
        TotalAmountCol.setCellValueFactory(new PropertyValueFactory<>("TotalCost"));
        DeadlineCol.setCellValueFactory(new PropertyValueFactory<>("Deadline"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        AssigneeCol.setCellValueFactory(new PropertyValueFactory<>("OrderAssignee"));

        // Handle refresh events
        OrdersTable.addEventHandler(GroceryStoreView.REFRESH, e -> {
            List<TOOrder> orders = OrderController.getAllOrders();
            OrdersTable.setItems(FXCollections.observableList(orders));
        });
        GroceryStoreView.registerRefreshableNode(OrdersTable);
    }


    public void handleCreateClick() {
        String creatorusername = CustomerUsername.getText();
        String deadline = DeadlineCreate.getText();
        Order.DeliveryDeadline deadline1 = null;
        if (Objects.equals(deadline, "sd")){
            deadline1 = Order.DeliveryDeadline.SameDay;
        } else if (Objects.equals(deadline, "1d")) {
            deadline1 = Order.DeliveryDeadline.InOneDay;
        } else if (Objects.equals(deadline, "2d")) {
            deadline1 = Order.DeliveryDeadline.InTwoDays;
        }else if(Objects.equals(deadline, "3d")){
            deadline1 = Order.DeliveryDeadline.InThreeDays;
        }
        try {
            OrderController.createOrder(creatorusername, deadline1);
            GroceryStoreView.refresh();
            CustomerUsername.setText("");
            DeadlineCreate.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleDeleteClick() {
        int ordernum = Integer.parseInt(OrderNumberDel.getText());
        try {
            OrderController.deleteOrder(ordernum);
            GroceryStoreView.refresh();
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleAddClick() {
        int ordernum = Integer.parseInt(OrderNumberAdd.getText());
        String itemname = ItemNameAdd.getText();
        try {
            OrderController.addItemToOrder(ordernum,itemname);
            GroceryStoreView.refresh();
            OrderNumberAdd.setText("");
            ItemNameAdd.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleUpdateClick() {
        int ordernum = Integer.parseInt(OrderNumberUp.getText());
        String itemname = ItemNameUp.getText();
        int quantity = Integer.parseInt(QuantityUp.getText());
        try {
            OrderController.updateQuantityInOrder(ordernum, itemname, quantity);
            GroceryStoreView.refresh();
            OrderNumberUp.setText("");
            ItemNameUp.setText("");
            QuantityUp.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleDeleteItemClick() {
        int ordernum = Integer.parseInt(OrderNumberDelItem.getText());
        String itemname = ItemNameDelItem.getText();
        try {
            OrderController.updateQuantityInOrder(ordernum, itemname, 0);
            GroceryStoreView.refresh();
            OrderNumberDelItem.setText("");
            ItemNameDelItem.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleCheckoutClick() {
        int ordernum = Integer.parseInt(OrderNumberCheck.getText());
        try {
            OrderProcessingController.checkOut(ordernum);
            GroceryStoreView.refresh();
            OrderNumberCheck.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handlePayClick() {
        int ordernum = Integer.parseInt(OrderNumberPay.getText());
        boolean UsingPoints;
        if (Objects.equals(UsePoints.getText(), "true")){
            UsingPoints = true;
        } else {
            UsingPoints = false;
        }
        try {
            OrderProcessingController.payForOrder(ordernum, UsingPoints);
            GroceryStoreView.refresh();
            OrderNumberPay.setText("");
            UsePoints.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleAssignClick() {
        int ordernum = Integer.parseInt(OrderNumberAss.getText());
        String employee = EmployeeUsername.getText();
        try {
            OrderProcessingController.assignOrderToEmployee(ordernum, employee);
            GroceryStoreView.refresh();
            OrderNumberAss.setText("");
            EmployeeUsername.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleAssembleClick() {
        int ordernum = Integer.parseInt(OrderNumberFinish.getText());
        try {
            OrderProcessingController.finishOrderAssembly(ordernum);
            GroceryStoreView.refresh();
            OrderNumberFinish.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handledeliverClick() {
        int ordernum = Integer.parseInt(OrderNumberDeliver.getText());
        try {
            OrderProcessingController.deliverOrder(ordernum);
            GroceryStoreView.refresh();
            OrderNumberDeliver.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public void handleCancelClick() {
        int ordernum = Integer.parseInt(OrderNumberCancel.getText());
        try {
            OrderProcessingController.cancelOrder(ordernum);
            GroceryStoreView.refresh();
            OrderNumberCancel.setText("");
        } catch (GroceryStoreException e) {
            ViewUtils.showErrorMessage(String.valueOf(e));
        }
    }

    public class ViewUtils {

        public static void showErrorMessage(String message) {
            makePopupWindow("Error", message);
        }


        public static void makePopupWindow(String title, String message) {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogPane = new VBox();

            // create UI elements
            Text text = new Text(message);
            Button okButton = new Button("OK");
            okButton.setOnAction(a -> dialog.close());

            // display the popup window
            int innerPadding = 10; // inner padding/spacing
            int outerPadding = 100; // outer padding
            dialogPane.setSpacing(innerPadding);
            dialogPane.setAlignment(Pos.CENTER);
            dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
            dialogPane.getChildren().addAll(text, okButton);
            Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
            dialog.setScene(dialogScene);
            dialog.setTitle(title);
            dialog.show();
        }
    }

}