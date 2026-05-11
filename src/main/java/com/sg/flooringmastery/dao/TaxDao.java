package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.util.List;

public interface TaxDao {

    List<Tax> getAllTaxes();
    BigDecimal getTaxRate(String stateAbbreviation);
    Tax getTax(String stateAbbreviation);


}
