package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao {

    // hardcoded test Product
    private Product testProduct;

    public ProductDaoStubImpl() {
        testProduct = new Product();
        testProduct.setProductType("Tile");
        testProduct.setCostPerSquareFoot(new BigDecimal("3.50"));
        testProduct.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(testProduct);
        return products;
    }

    @Override
    public BigDecimal getProductCostPerSquareFoot(String productType) {
        if (productType.equals(testProduct.getProductType())) {
            return testProduct.getCostPerSquareFoot();
        }
        return null;
    }

    @Override
    public BigDecimal getProductLaborPerSquareFoot(String productType) {
        if (productType.equals(testProduct.getProductType())) {
            return testProduct.getLaborCostPerSquareFoot();
        }
        return null;
    }

    @Override
    public Product getProduct(String productType) {
        return testProduct;
    }
}