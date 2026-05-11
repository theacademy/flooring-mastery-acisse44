package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts();
    BigDecimal getProductCostPerSquareFoot(String productType);
    BigDecimal getProductLaborPerSquareFoot(String productType);
    Product getProduct(String productType);

}