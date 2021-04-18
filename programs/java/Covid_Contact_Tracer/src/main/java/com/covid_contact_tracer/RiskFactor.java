package com.covid_contact_tracer;

public class RiskFactor {

    //Define risk factor levels
    private final int   LOW = 1,
                        MED = 2,
                        HIGH = 3;
    //Define risk factor limits
    private final int   LIMIT_LOW = 5,
                        LIMIT_HIGH = 10;
    private int riskLevel;
    
    public RiskFactor(int timesSignedIn){
        riskLevel = (timesSignedIn < LIMIT_LOW)? 
            LOW : (timesSignedIn >= LIMIT_LOW && timesSignedIn <= LIMIT_HIGH)?  
                MED : HIGH;
    }

    public int getLevel(){
        return this.riskLevel;
    }
}
