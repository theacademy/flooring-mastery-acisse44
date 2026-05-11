package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private static final String ORDERS_FOLDER = "Orders/";
    public static final String DELIMITER = "::";
    private Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();
    private int orderIdCounter;


    public OrderDaoFileImpl() throws OrderPersistenceException {
        loadOrders(); //loading when we start program
        orderIdCounter = getNextOrderNumber();
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderNotFoundException {
        Map<Integer, Order> innerMap = orders.get(date);
        if (innerMap == null) {
            throw new OrderNotFoundException("No orders found for date: " + date);
        }
        return new ArrayList<>(innerMap.values());
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws OrderNotFoundException {
        Map<Integer, Order> innerMap = getInnerMap(date, orderNumber);
        Order order = innerMap.get(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException("Order not found for date: " + date + " and order number: " + orderNumber);
        }
        return order;
    }

    //helper method to avoid repeatedly getting inner map
    private Map<Integer, Order> getInnerMap(LocalDate date, int orderNumber) throws OrderNotFoundException {
        Map<Integer, Order> innerMap = orders.get(date);
        if (innerMap == null) {
            throw new OrderNotFoundException("Order not found for date: " + date + " and order number: " + orderNumber);
        }
        return innerMap;
    }

    public int getNextOrderNumber() {
        int maxOrderNumber = 0;
        for (Map<Integer, Order> innerMap : orders.values()) {
            for (int orderNumber : innerMap.keySet()) {
                if (orderNumber > maxOrderNumber) {
                    maxOrderNumber = orderNumber;
                }
            }
        }
        return maxOrderNumber;
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws OrderPersistenceException {
        orderIdCounter++; // just increment, number already set by controller since we ensure that user wants to make change
        orders.computeIfAbsent(date, k -> new HashMap<>()).put(order.getOrderNumber(), order);
        return order;
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws OrderNotFoundException {

        int orderNumber = order.getOrderNumber();
        Map<Integer, Order> innerMap = getInnerMap(date, orderNumber);
        Order existingOrder = innerMap.get(orderNumber);
        if (existingOrder == null) {
            throw new OrderNotFoundException("Order not found for date: " + date + " and order number: " + orderNumber);
        }
        innerMap.put(orderNumber, order);
        return order;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws OrderNotFoundException {
        Map<Integer, Order> innerMap = getInnerMap(date, orderNumber);
        Order order = innerMap.get(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException("Order not found for date: " + date + " and order number: " + orderNumber);
        }
        return innerMap.remove(orderNumber);
    }


    private void loadOrders() throws OrderPersistenceException {

        //locating older folder and order date files
        File folder = new File(ORDERS_FOLDER);
        File[] files = folder.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            String dateStr = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);

            Scanner scanner;
            try {
                scanner = new Scanner(new BufferedReader(new FileReader(file)));
            } catch (FileNotFoundException e) {
                throw new OrderPersistenceException("-_- Could not load order data into memory.", e);
            }

            scanner.nextLine(); // skip header
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Order currentOrder = unmarshallOrder(currentLine);
                // lambda: if date key doesn't exist, create new HashMap, then put order in
                orders.computeIfAbsent(localDate, k -> new HashMap<>()).put(currentOrder.getOrderNumber(), currentOrder);
            }
            scanner.close();
        }
    }

    private Order unmarshallOrder(String orderAsText) {

        String[] orderTokens = orderAsText.split(DELIMITER);

        Order order = new Order();
        order.setOrderNumber(Integer.parseInt(orderTokens[0]));
        order.setCustomerName(orderTokens[1]);
        order.setState(orderTokens[2]);
        order.setTaxRate(new BigDecimal(orderTokens[3]));
        order.setProductType(orderTokens[4]);
        order.setArea(new BigDecimal(orderTokens[5]));
        order.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        order.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        order.setMaterialCost(new BigDecimal(orderTokens[8]));
        order.setLaborCost(new BigDecimal(orderTokens[9]));
        order.setTax(new BigDecimal(orderTokens[10]));
        order.setTotal(new BigDecimal(orderTokens[11]));

        return order;
    }

    private String marshallOrder(Order order) { //takes a String and turns it into an Order object

        // 1::Ada Lovelace::CA::25.00::Tile::249.00::3.50::4.15::871.50::1033.35::476.21::2381.06
        return order.getOrderNumber() + DELIMITER +
                order.getCustomerName() + DELIMITER +
                order.getState() + DELIMITER +
                order.getTaxRate() + DELIMITER +
                order.getProductType() + DELIMITER +
                order.getArea() + DELIMITER +
                order.getCostPerSquareFoot() + DELIMITER +
                order.getLaborCostPerSquareFoot() + DELIMITER +
                order.getMaterialCost() + DELIMITER +
                order.getLaborCost() + DELIMITER +
                order.getTax() + DELIMITER +
                order.getTotal();

    }

    @Override
    public void exportOrders() throws OrderPersistenceException {
        PrintWriter out;

        for (Map.Entry<LocalDate, Map<Integer, Order>> entry : orders.entrySet()) {
            LocalDate date = entry.getKey();
            Map<Integer, Order> innerMap = entry.getValue();

            // build the file name from the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
            String fileName = ORDERS_FOLDER + "Orders_" + date.format(formatter) + ".txt";

            try {
                out = new PrintWriter(new FileWriter(fileName));
            } catch (IOException e) {
                throw new OrderPersistenceException("Could not save order data.", e);
            }

            // write header
            out.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total");
            //flush() forces everything in the buffer to be written to disk immediately
            out.flush();

            // write each order
            for (Order order : innerMap.values()) {
                out.println(marshallOrder(order));
                out.flush();
            }

            out.close();
        }
    }
}