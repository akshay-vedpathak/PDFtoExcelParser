package com.benefix.daoImpl;

import com.benefix.dao.IInsuranceDetailsDAO;

import java.util.Map;

/**
 * Simple Java Class that implements IInsuranceDetailsDAO interface and stores data extracted from PDF files.
 * This class uses Builder Pattern in Java in order to create InsuranceDetails objects
 */

public class InsuranceDetails implements IInsuranceDetailsDAO {
    private String startDate;
    private String endDate;
    private String productName;
    private String states;
    private String groupRatingAreas;
    private Map<String,Double> ageAndRates;

    private InsuranceDetails(String startDate, String endDate, String productName, String states, String groupRatingAreas, Map<String, Double> ageAndRates) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.productName = productName;
        this.states = states;
        this.groupRatingAreas = groupRatingAreas;
        this.ageAndRates = ageAndRates;
    }

    @Override
    public String getStartDate() {
        return this.startDate;
    }

    @Override
    public String getEndDate() {
        return this.endDate;
    }

    @Override
    public String getProductName() {
        return this.productName;
    }

    @Override
    public String getStates() {
        return this.states;
    }

    @Override
    public String getGroupRatingAreas() {
        return this.groupRatingAreas;
    }

    @Override
    public Map<String, Double> getAgeAndRates() {
        return this.ageAndRates;
    }

    @Override
    public String toString(){
        return "{Start Date: "+startDate+"\n" +
                "End Date: "+endDate+"\n" +
                "Product Name: "+productName+"\n" +
                "State: "+states+"\n" +
                "Group Rating Areas: "+groupRatingAreas+ "\n" +
                "Age and Rates: "+getAgeAndRates().toString()+" }";
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static class Builder{
        private String startDate;
        private String endDate;
        private String productName;
        private String states;
        private String groupRatingAreas;
        private Map<String,Double> ageAndRates;

        public Builder setStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setStates(String states) {
            this.states = states;
            return this;
        }

        public Builder setGroupRatingAreas(String groupRatingAreas) {
            this.groupRatingAreas = groupRatingAreas;
            return this;
        }

        public Builder setAgeAndRates(Map<String, Double> ageAndRates) {
            this.ageAndRates = ageAndRates;
            return this;
        }

        public InsuranceDetails build(){
            return new InsuranceDetails(startDate,endDate,productName,states,groupRatingAreas,ageAndRates);
        }
    }
}
