package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.OrderNotFoundException;
import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.OrderValidationException;
import com.sg.flooringmastery.ui.FlooringMasterView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private FlooringMasteryServiceLayer service;
    private FlooringMasterView view;

    private boolean unsavedChanges = false;

    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasterView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {
                menuSelection = view.printMenuAndGetSelection();
                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        if (unsavedChanges) {
                            String response = view.confirmExit();
                            if (response.equalsIgnoreCase("Y")) {
                                keepGoing = false;
                            }
                        } else {
                            keepGoing = false;
                        }
                        break;
                    default:
                        view.displayUnknownCommandBanner();
                }
            }
            view.displayExitMessage();
        } catch (OrderPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (OrderValidationException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void displayOrders() throws OrderPersistenceException {
        LocalDate date = view.getOrderDate();
        try {
            List<Order> orders = service.getOrders(date);
            view.displayOrderList(orders);
        } catch (OrderNotFoundException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void addOrder() throws OrderPersistenceException, OrderValidationException {
        view.displayAddOrderBanner();
        LocalDate date = getValidDate();
        Order order = new Order();
        order.setCustomerName(getValidCustomerName());
        order.setState(getValidState());
        order.setProductType(getValidProductType());
        order.setArea(getValidArea());

        order.setOrderNumber(service.getNextOrderNumber());
        service.calculateOrderCosts(order);
        view.displayOrder(order);

        if (view.confirmAddOrder()) {
            service.addOrder(date, order);
            view.displaySaveSuccess();
            unsavedChanges = true;
        }
    }

    private void editOrder() throws OrderPersistenceException {
        view.displayEditOrderBanner();
        LocalDate date = view.getOrderDate();
        int orderNumber = view.getOrderNumber();
        try {
            Order existingOrder = service.getOrder(date, orderNumber);
            // ask for each field showing current value
            existingOrder.setCustomerName(getValidNameEdit(existingOrder.getCustomerName()));
            existingOrder.setState(getValidStateEdit(existingOrder.getState()));
            existingOrder.setProductType(getValidProductTypeEdit(existingOrder.getProductType()));
            existingOrder.setArea(getValidAreaEdit(existingOrder.getArea()));
            try {
                Order editedOrder = service.editOrder(date, existingOrder);
                // show summary AFTER edits
                view.displayOrder(editedOrder);
                if (view.confirmEditOrder()) {
                    view.displaySaveSuccess();
                    unsavedChanges = true;
                }
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } catch (OrderNotFoundException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void removeOrder() throws OrderPersistenceException {
        view.displayRemoveOrderBanner();
        LocalDate date = view.getOrderDate();
        int orderNumber = view.getOrderNumber();
        try {
            Order order = service.getOrder(date, orderNumber);
            view.displayOrder(order);
            if (view.confirmRemoveOrder()) {
                service.removeOrder(date, orderNumber);
                view.displayRemoveResult(order);
                unsavedChanges = true;
            }
        } catch (OrderNotFoundException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    private void exportData() throws OrderPersistenceException {
        service.saveData();
        unsavedChanges = false;
        view.displaySaveSuccess();
    }

    //helper methods
    private LocalDate getValidDate() {
        LocalDate date = null;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                date = view.getOrderDate();
                service.validateDate(date);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return date;
    }

    private String getValidCustomerName() {
        String name = null;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                name = view.getCustomerName();
                service.validateCustomerName(name);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return name;
    }

    private String getValidNameEdit(String currentName) {
        String name = currentName;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                name = view.getCustomerNameEdit(currentName);
                service.validateCustomerName(name);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return name;
    }

    private String getValidState() {
        String state = null;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                state = view.getCustomerState();
                service.validateCustomerState(state);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return state;
    }

    private String getValidStateEdit(String currentState) {
        String state = currentState;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                state = view.getCustomerStateEdit(currentState);
                service.validateCustomerState(state);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return state;
    }

    private String getValidProductType() throws OrderPersistenceException {
        String productType = null;
        boolean hasErrors = true;
        List<Product> products = service.getAllProducts();
        view.displayProducts(products);
        while (hasErrors) {
            try {
                productType = view.getProductType();
                service.validateProductSelection(productType);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return productType;
    }

    private String getValidProductTypeEdit(String currentProductType) {
        String productType = currentProductType;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                productType = view.getProductTypeEdit(currentProductType);
                service.validateProductSelection(productType);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return productType;
    }

    private BigDecimal getValidArea() {
        BigDecimal area = null;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                area = view.getArea();
                service.validateArea(area);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return area;
    }

    private BigDecimal getValidAreaEdit(BigDecimal currentArea) {
        BigDecimal area = currentArea;
        boolean hasErrors = true;
        while (hasErrors) {
            try {
                area = view.getAreaEdit(currentArea);
                service.validateArea(area);
                hasErrors = false;
            } catch (OrderValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        return area;
    }
}