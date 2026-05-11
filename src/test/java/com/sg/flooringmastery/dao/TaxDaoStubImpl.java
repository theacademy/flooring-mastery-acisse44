package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDaoStubImpl implements TaxDao {

    private Tax testTax;

    public TaxDaoStubImpl() {
        testTax = new Tax();
        testTax.setStateAbbreviation("TX");
        testTax.setStateName("Texas");
        testTax.setTaxRate(new BigDecimal("25.00"));
    }

    @Override
    public List<Tax> getAllTaxes() {
        List<Tax> taxes = new ArrayList<>();
        taxes.add(testTax);
        return taxes;
    }

    @Override
    public BigDecimal getTaxRate(String stateAbbreviation) {
        if (stateAbbreviation.equals(testTax.getStateAbbreviation())) {
            return testTax.getTaxRate();
        }
        return null;
    }

    @Override
    public Tax getTax(String stateAbbreviation) {
        return testTax;
    }
}