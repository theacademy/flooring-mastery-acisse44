package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer{

    private OrderDao orderDao;
    private ProductDao productDao;
    private TaxDao taxDao;


    public FlooringMasteryServiceLayerImpl( OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws OrderValidationException, OrderPersistenceException{
        validateDate(date);
        validateCustomerName(order.getCustomerName());
        validateCustomerState(order.getState());
        validateProductSelection(order.getProductType());
        validateArea(order.getArea());
        calculateOrderCosts(order);
        return orderDao.addOrder(date, order);
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws OrderNotFoundException, OrderValidationException, OrderPersistenceException {
        validateCustomerName(order.getCustomerName());
        validateCustomerState(order.getState());
        validateProductSelection(order.getProductType());
        validateArea(order.getArea());
        calculateOrderCosts(order);
        return orderDao.editOrder(date, order);
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws OrderNotFoundException, OrderPersistenceException {
        return orderDao.getOrder(date, orderNumber);
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws OrderNotFoundException, OrderPersistenceException {
        return orderDao.getOrdersByDate(date);
    }

    public int getNextOrderNumber() {
        return orderDao.getNextOrderNumber();
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws OrderNotFoundException, OrderPersistenceException {
        return orderDao.removeOrder(date, orderNumber);
    }

    @Override
    public List<Product> getAllProducts() throws OrderPersistenceException {
        return productDao.getAllProducts();
    }

    @Override
    public void saveData() throws OrderPersistenceException {
        orderDao.exportOrders();
    }

    //validate methods TO ensure proper input is given upon prompting
    public void validateCustomerName(String name) throws OrderValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new OrderValidationException("Name cannot be blank.");
        }
        if (!name.matches("[a-zA-Z0-9., ]+")) {
            throw new OrderValidationException("Name contains invalid characters.");
        }
    }

    public void validateDate(LocalDate date) throws OrderValidationException {
        if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
            throw new OrderValidationException("Order date must be in the future.");
        }
    }

    public void validateCustomerState(String state) throws OrderValidationException {
        if (taxDao.getTaxRate(state) == null) {
            throw new OrderValidationException("State " + state + " is not available for orders.");
        }
    }

    public void validateProductSelection(String product) throws OrderValidationException {
        if (productDao.getProductCostPerSquareFoot(product) == null) {
            throw new OrderValidationException("Product " + product + " is not in stock.");
        }
    }

    public void validateArea(BigDecimal area) throws OrderValidationException {
        if (area.compareTo(new BigDecimal("100")) < 0) {
            throw new OrderValidationException("Area must be at least 100 sq ft.");
        }
    }

    public void calculateOrderCosts(Order order) {
        // MaterialCost = Area * CostPerSquareFoot
        // LaborCost = Area * LaborCostPerSquareFoot
        // Tax = (MaterialCost + LaborCost) * (TaxRate/100)
        // Total = MaterialCost + LaborCost + Tax
        Product product = productDao.getProduct(order.getProductType());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

        Tax tax = taxDao.getTax(order.getState());
        order.setTaxRate(tax.getTaxRate());

        order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP));

        order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP));

        order.setTax((order.getMaterialCost().add(order.getLaborCost()))
                .multiply(order.getTaxRate().divide(new BigDecimal("100")))
                .setScale(2, RoundingMode.HALF_UP));

        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax())
                .setScale(2, RoundingMode.HALF_UP));
    }

}
