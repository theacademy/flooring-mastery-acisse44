package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasterView {

    private UserIO io = new UserIOConsoleImpl();

    public FlooringMasterView(UserIO myIo) {
        this.io = myIo;
    }

    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<    Welcome to my Flooring Program     >>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public String getCustomerName() {
        String customerName = io.readString("Please enter your name");
        return customerName;
    }

    public LocalDate getOrderDate() {
        return io.readLocalDate("Please enter the order date (MM-DD-YYYY):");
    }

    public String getCustomerState() {
        String customerState = io.readString("Please enter your state abbreviation");
        return customerState;
    }

    public String getProductType() {
        String productType = io.readString("Please enter the product type");
        return productType;
    }

    public BigDecimal getArea() {
        BigDecimal area = io.readBigDecimal("Please enter the area");
        return area;
    }

    public int getOrderNumber() {
        int orderNumber = io.readInt("Please enter the order number");
        return orderNumber;
    }

    public void displayOrder(Order order) {
        io.print("Order Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Tax Rate: " + order.getTaxRate());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea());
        io.print("Cost Per Sq Ft: " + order.getCostPerSquareFoot());
        io.print("Labor Cost Per Sq Ft: " + order.getLaborCostPerSquareFoot());
        io.print("Material Cost: " + order.getMaterialCost());
        io.print("Labor Cost: " + order.getLaborCost());
        io.print("Tax: " + order.getTax());
        io.print("Total: " + order.getTotal());
        io.readString("Please hit enter to continue.");
    }

    public void displayOrderList(List<Order> orderList) {
        for (Order order : orderList) {
            io.print(String.format("#%s : %s - %s - %s",
                    order.getOrderNumber(),
                    order.getCustomerName(),
                    order.getState(),
                    order.getTotal()));
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayProducts(List<Product> products) {
        io.print("=== Available Products ===");
        for (Product product : products) {
            io.print(String.format("%s - Cost/SqFt: $%s, Labor/SqFt: $%s",
                    product.getProductType(),
                    product.getCostPerSquareFoot(),
                    product.getLaborCostPerSquareFoot()));
        }
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }

    public boolean confirmAddOrder() {
        String response = io.readString("Place this order? (Y/N):");
        return response.equalsIgnoreCase("Y");
    }

    public boolean confirmEditOrder() {
        String response = io.readString("Save changes to this order? (Y/N):");
        return response.equalsIgnoreCase("Y");
    }

    public boolean confirmRemoveOrder() {
        String response = io.readString("Are you sure you want to remove this order? (Y/N):");
        return response.equalsIgnoreCase("Y");
    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }

    public void displayEditOrderBanner() {
        io.print("=== Edit Order ===");
    }

    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }

    public void displayRemoveResult(Order order) {
        if (order != null) {
            io.print("Order successfully removed.");
        } else {
            io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayExitMessage() {
        io.print("Thank you, Goodbye :)");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!");
    }

    public void displaySaveSuccess() {
        io.readString("Order added to memory. Export (option 5) to save permanently. Hit enter to continue.");
    }

    public String getCustomerNameEdit(String currentName) {
        String input = io.readString("Enter customer name (" + currentName + "):");
        if (input.trim().isEmpty()) {
            return currentName; // keep existing
        }
        return input;
    }

    public String getCustomerStateEdit(String currentState) {
        String input = io.readString("Enter state (" + currentState + "):");
        if (input.trim().isEmpty()) {
            return currentState;
        }
        return input;
    }

    public String getProductTypeEdit(String currentProductType) {
        String input = io.readString("Enter product type (" + currentProductType + "):");
        if (input.trim().isEmpty()) {
            return currentProductType;
        }
        return input;
    }

    public BigDecimal getAreaEdit(BigDecimal currentArea) {
        String input = io.readString("Enter area (" + currentArea + "):");
        if (input.trim().isEmpty()) {
            return currentArea;
        }
        return new BigDecimal(input);
    }

    public String confirmExit() {
        return io.readString("You have unsaved changes. Are you sure you want to quit without exporting? (Y/N):");
    }


}