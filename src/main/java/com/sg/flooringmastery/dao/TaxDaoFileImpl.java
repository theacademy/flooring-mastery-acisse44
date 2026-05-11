package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoFileImpl implements TaxDao {

    //TX,Texas,4.45
        private static final String TAX_FILE = "Data/Taxes.txt";
        public static final String DELIMITER = "::";
        private Map<String, Tax> allTaxes = new HashMap<>();


        public TaxDaoFileImpl() throws OrderPersistenceException {
            loadTax();
        }

        @Override
        public List<Tax> getAllTaxes() {
            return new ArrayList<>(allTaxes.values());
        }

        @Override
        public Tax getTax(String stateAbbreviation) {
            return allTaxes.get(stateAbbreviation);
        }

        @Override
        public BigDecimal getTaxRate(String stateAbbreviation) {
            Tax tax = allTaxes.get(stateAbbreviation);
            if (tax == null) return null;
            return tax.getTaxRate();
        }

        private void loadTax() throws OrderPersistenceException {
            Scanner scanner;
            try {
                scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
            } catch (FileNotFoundException e) {
                throw new OrderPersistenceException("Could not load tax data.", e);
            }
            scanner.nextLine(); // skip header
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Tax currentTax = unmarshallTax(currentLine);
                allTaxes.put(currentTax.getStateAbbreviation(), currentTax);
            }
            scanner.close();
        }

        private Tax unmarshallTax(String taxAsText) {
            String[] tokens = taxAsText.split(DELIMITER);
            Tax tax = new Tax();
            tax.setStateAbbreviation(tokens[0]);
            tax.setStateName(tokens[1]);
            tax.setTaxRate(new BigDecimal(tokens[2]));
            return tax;
        }
    }

