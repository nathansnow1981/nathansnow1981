package com.covid_contact_tracer;

public class ServerResponseMessage {
    
    private Person user;
    private final String HELLO = "Welcome",
                         BYE = "Goodbye",
                         SIGNOUT_OK = "logged out",
                         //Risk warnings
                         RISK_MED = ": Warning! you are at medium risk",
                         RISK_HIGH = ": Warning! you are at high risk",
                         //Errors
                         ERR_ALREADY_IN = "is already signed in",
                         ERR_ALREADY_OUT = "is already signed out",
                         ERR_INCORRECT_LOGIN = "Sorry, the username and/or password is incorrect";
                         

    public ServerResponseMessage(Person person) {
        this.user = person;
    }                 

    public String greet(){
        return HELLO +" "+ user.getUsername();
    }

    public String farewell(){
        return BYE +" "+ user.getUsername();
    }

    public void logUserOut(){
        System.out.println(user.getUsername() +" "+ SIGNOUT_OK);
    }

    public String mediumRisk(){
        return RISK_MED;
    }

    public String highRisk(){
        return RISK_HIGH;
    }

    public String alreadySignedIn(){
        return user.getUsername() +" "+ ERR_ALREADY_IN;
    }

    public String alreadySignedOut(){
        return user.getUsername() +" "+ ERR_ALREADY_OUT;
    }

    public String loginError(){
        return ERR_INCORRECT_LOGIN;
    }
    
    public void logActivity(){
         System.out.println("Username: " + user.getUsername() + 
                            "; Times signed in = " + user.getTimesSignedIn()+
                            "; Risk Factor = " + user.getRiskFactor());
    }
}
