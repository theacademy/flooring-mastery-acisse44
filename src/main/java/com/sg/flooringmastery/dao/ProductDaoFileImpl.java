package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao{

    private static final String PRODUCT_FILE = "Data/Products.txt";
    public static final String DELIMITER = "::";
    private Map<String, Product> allProducts = new HashMap<>();

    public ProductDaoFileImpl() throws OrderPersistenceException {
        loadProducts();
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(allProducts.values());
    }

    @Override
    public Product getProduct(String productType) {
        return allProducts.get(productType);
    }

    @Override
    public BigDecimal getProductCostPerSquareFoot(String productType) {
        Product product = allProducts.get(productType);
        if (product == null) return null;
        return product.getCostPerSquareFoot();
    }

    @Override
    public BigDecimal getProductLaborPerSquareFoot(String productType) {
        Product product = allProducts.get(productType);
        if (product == null) return null;
        return product.getLaborCostPerSquareFoot();
    }

    private void loadProducts() throws OrderPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderPersistenceException("Could not load product data.", e);
        }
        scanner.nextLine(); // skip header
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Product currentProduct = unmarshallProduct(currentLine);
            allProducts.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
    }

    private Product unmarshallProduct(String productAsText) {
        String[] tokens = productAsText.split(DELIMITER);
        Product product = new Product();
        product.setProductType(tokens[0]);
        product.setCostPerSquareFoot(new BigDecimal(tokens[1]));
        product.setLaborCostPerSquareFoot(new BigDecimal(tokens[2]));
        return product;
    }
}