package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderNotFoundException;
import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.OrderValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryServiceLayer {
    Order addOrder(LocalDate date, Order order) throws OrderValidationException, OrderPersistenceException;
    Order editOrder(LocalDate date, Order order) throws OrderNotFoundException, OrderValidationException, OrderPersistenceException;
    Order getOrder(LocalDate date, int orderNumber) throws OrderNotFoundException, OrderPersistenceException;
    int getNextOrderNumber();
    List<Order> getOrders(LocalDate date) throws OrderNotFoundException, OrderPersistenceException;
    Order removeOrder(LocalDate date, int orderNumber) throws OrderNotFoundException, OrderPersistenceException;
    void saveData() throws OrderPersistenceException;
    List<Product> getAllProducts() throws OrderPersistenceException;
    void calculateOrderCosts(Order order);
    void validateDate(LocalDate date) throws OrderValidationException;
    void validateCustomerName(String name) throws OrderValidationException;
    void validateCustomerState(String state) throws OrderValidationException;
    void validateProductSelection(String productType) throws OrderValidationException;
    void validateArea(BigDecimal area) throws OrderValidationException;
}
