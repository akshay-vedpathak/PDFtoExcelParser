package com.benefix.dao;

import java.util.Date;
import java.util.Map;

/**
 * Interface to access the attributes of Insurance Details object
 */
public interface IInsuranceDetailsDAO {
    String getStartDate();
    String getEndDate();
    String getProductName();
    String getStates();
    String getGroupRatingAreas();
    Map<String, Double> getAgeAndRates();
}
