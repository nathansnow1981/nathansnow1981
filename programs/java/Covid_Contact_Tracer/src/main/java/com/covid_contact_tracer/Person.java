
package com.covid_contact_tracer;

public class Person {
    
    private String  username,       //Username and password are for signing in to public places
                    password;
    private int     timesSignedIn;  //Number of times the person has signed in to a public place
    private boolean signedIn;       //Whether or not the person is currently signed in to a public place

    /**
     * Person constructor
     * @param username The person's username
     * @param password The person's password
     * @param timesSignedIn The number of times the person has signed in to a public place
     * @param signedIn A true/false value indicating whether the person is currently signed in to a public place
     */
    public Person(String username, String password, int timesSignedIn, boolean signedIn) {
        this.username = username;
        this.password = password;
        this.timesSignedIn = timesSignedIn;
        this.signedIn = signedIn;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimesSignedIn(int timesSignedIn) {
        this.timesSignedIn = timesSignedIn;
    }

    /**
     * Sets the status of whether or not a person is signed in to a public place, and increments the number of
     * times signed in by 1 each time the person sucessfully signs in
     * @param status A true/false value depending on the person's signedIn status
     */
    public void setSignedIn(boolean status) {
        if(status == true){
            this.timesSignedIn ++;
        }
        this.signedIn = status;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getTimesSignedIn() {
        return this.timesSignedIn;
    }
    /**
     * Calls a RiskFactor instance passing it the number of times the person has signed in
     * @return An integer value representing the exposure risk level
     * @see RiskFactor
     */
    public int getRiskFactor() {
        return new RiskFactor(timesSignedIn).getLevel();
    }

    public boolean signedIn() {
        return this.signedIn;
    }        
}