package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.dto.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FlooringMasteryServiceLayerImplTest {

    private FlooringMasteryServiceLayer service;

    public FlooringMasteryServiceLayerImplTest() {
        OrderDao orderDao = new OrderDaoStubImpl();
        ProductDao productDao = new ProductDaoStubImpl();
        TaxDao taxDao = new TaxDaoStubImpl();
        service = new FlooringMasteryServiceLayerImpl(orderDao, productDao, taxDao);
    }

    @Test
    public void testGetOrdersValidDate() {

        // ARRANGE
        LocalDate date = LocalDate.of(2026, 6, 1);
        // ACT & ASSERT
        try {
            List<Order> orders = service.getOrders(date);
            assertNotNull(orders, "Orders list shouldn't be null.");
            assertEquals(1, orders.size(), "Should have one order.");
        } catch (Exception e) {
            fail("No exception should have been thrown.");
        }
    }

    @Test
    public void testAddValidOrder() {

        // ARRANGE
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        // ACT & ASSERT
        try {
            Order added = service.addOrder(date, order);
            assertNotNull(added, "Added order should not be null.");
        } catch (Exception e) {
            fail("No exception should have been thrown.");
        }
    }

    @Test
    public void testAddOrderPastDate() {

        // ARRANGE
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        // ACT & ASSERT
        try {
            service.addOrder(pastDate, order);
            fail("Expected OrderValidationException was not thrown.");
        } catch (OrderValidationException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testAddOrderInvalidArea() {
        // ARRANGE
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("50.00")); // less than 100
        // ACT & ASSERT
        try {
            service.addOrder(date, order);
            fail("Expected OrderValidationException was not thrown.");
        } catch (OrderValidationException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testAddOrderInvalidName() {
        // ARRANGE
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setCustomerName("");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        // ACT & ASSERT
        try {
            service.addOrder(date, order);
            fail("Expected OrderValidationException was not thrown.");
        } catch (OrderValidationException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testRemoveOrder() {
        // ARRANGE
        LocalDate date = LocalDate.of(2026, 6, 1);
        // ACT & ASSERT
        try {
            Order removed = service.removeOrder(date, 1);
            assertNotNull(removed, "Removed order should not be null.");
        } catch (Exception e) {
            fail("No exception should have been thrown.");
        }
    }
    @Test
    public void testAddOrderInvalidState() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("GH"); // invalid state
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        try {
            service.addOrder(date, order);
            fail("Expected OrderValidationException was not thrown.");
        } catch (OrderValidationException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testAddOrderInvalidProductType() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setProductType("InvalidProduct");
        order.setArea(new BigDecimal("249.00"));
        try {
            service.addOrder(date, order);
            fail("Expected OrderValidationException was not thrown.");
        } catch (OrderValidationException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testEditOrder() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setOrderNumber(1);
        order.setCustomerName("Updated Name");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("369.00"));
        try {
            Order edited = service.editOrder(date, order);
            assertNotNull(edited, "Edited order should not be null.");
        } catch (Exception e) {
            fail("No exception should have been thrown.");
        }
    }

    @Test
    public void testEditOrderNotFound() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setOrderNumber(999); // non-existent
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        try {
            service.editOrder(date, order);
            fail("Expected OrderNotFoundException was not thrown.");
        } catch (OrderNotFoundException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testRemoveOrderNotFound() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        try {
            service.removeOrder(date, 999); // non-existent order number
            fail("Expected OrderNotFoundException was not thrown.");
        } catch (OrderNotFoundException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testGetOrdersNotOrdersFound() {
        LocalDate date = LocalDate.of(2099, 1, 1); // date with no orders
        try {
            service.getOrders(date);
            fail("Expected OrderNotFoundException was not thrown.");
        } catch (OrderNotFoundException e) {
            return;
        } catch (Exception e) {
            fail("Incorrect exception was thrown.");
        }
    }

    @Test
    public void testValidInput() {
        assertDoesNotThrow(() -> service.validateDate(LocalDate.now().plusDays(1)));
        assertDoesNotThrow(() -> service.validateCustomerName("Ada Lovelace"));
        assertDoesNotThrow(() -> service.validateCustomerState("TX"));
        assertDoesNotThrow(() -> service.validateProductSelection("Tile"));
        assertDoesNotThrow(() -> service.validateArea(new BigDecimal("100")));
    }

    @Test
    public void testInvalidInput() {
        assertThrows(OrderValidationException.class, () -> service.validateDate(LocalDate.now()));
        assertThrows(OrderValidationException.class, () -> service.validateCustomerName(""));
        assertThrows(OrderValidationException.class, () -> service.validateArea(new BigDecimal("50")));
    }
}