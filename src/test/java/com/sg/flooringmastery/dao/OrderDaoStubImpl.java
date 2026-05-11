package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoStubImpl implements OrderDao {

    // hardcoded test order
    private Order testOrder;
    private LocalDate testDate = LocalDate.of(2026, 6, 1);

    public OrderDaoStubImpl() {
        testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Ada Lovelace");
        testOrder.setState("CA");
        testOrder.setTaxRate(new BigDecimal("25.00"));
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249.00"));
        testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
        testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        testOrder.setMaterialCost(new BigDecimal("871.50"));
        testOrder.setLaborCost(new BigDecimal("1033.35"));
        testOrder.setTax(new BigDecimal("476.21"));
        testOrder.setTotal(new BigDecimal("2381.06"));
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderNotFoundException {
        if (date.equals(LocalDate.of(2026, 6, 1))) {
            List<Order> orders = new ArrayList<>();
            orders.add(testOrder);
            return orders;
        }
        throw new OrderNotFoundException("No orders found for date: " + date);
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws OrderNotFoundException {
        if (orderNumber == testOrder.getOrderNumber()) {
            return testOrder;
        }
        throw new OrderNotFoundException("Order not found");
    }

    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws OrderPersistenceException {
        return testOrder;
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws OrderNotFoundException {
        if (order.getOrderNumber() == testOrder.getOrderNumber()) {
            return order;
        }
        throw new OrderNotFoundException("Order not found");
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws OrderNotFoundException {
        if (orderNumber == testOrder.getOrderNumber()) {
            return testOrder;
        }
        throw new OrderNotFoundException("Order not found");
    }

    @Override
    public void exportOrders() throws OrderPersistenceException {
        // doing nothing
    }
}