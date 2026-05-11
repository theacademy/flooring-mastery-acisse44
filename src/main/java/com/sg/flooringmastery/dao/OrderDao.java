package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> getOrdersByDate(LocalDate date) throws OrderNotFoundException;
    Order getOrder(LocalDate date, int orderNumber) throws OrderNotFoundException;
    int getNextOrderNumber();
    Order addOrder(LocalDate date, Order order) throws OrderPersistenceException;
    Order editOrder(LocalDate date, Order order) throws OrderNotFoundException;
    Order removeOrder(LocalDate date, int orderNumber) throws OrderNotFoundException;
    void exportOrders() throws OrderPersistenceException ;
}
