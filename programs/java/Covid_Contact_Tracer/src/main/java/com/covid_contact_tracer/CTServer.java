
package com.covid_contact_tracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * In a real world type application, CTServer.java would be run continuously so it is available
 * to accept requests from (and reply to) many clients 24/7. It prints "CTServer running..." to the 
 * console on startup simply as a visual confirmation, and then prints to the console each time 
 * a user successfully signs in, also showing their number of times signed in and risk factor.
 * There is almost no exception handling implemented in this class as it is simply a demo that 
 * needs completing.
 */
public class CTServer {

    private final static String SEED_DATA_FILE = "src/main/java/com/covid_contact_tracer/data/Seed_data.csv",//Seed data file name & location
                                SERVER_RUNNING = "CTServer running...",
                                CSV_DELIMITER = ",",
                                PKT_DELIMITER = ":",
                                LOGIN_CMD = "1",
                                LOGOUT_CMD = "2";
    private final static int BUFFER_SIZE = 1024;//Default packet buffer size

    private static int serverPort = 1001;//Server port number
    private static LinkedList<Person> people = new LinkedList<>();//List of people
    private static Person currentPerson;//The person being currently handled
    private static String responseString = "";//Response to be returned to clients
    private static ServerResponseMessage serverResponseMessage;
    
    
    public static void main(String[] args) {
        
        //Initialize currentPerson
        currentPerson = null;
        //Load seed data from file
        loadSeedData();
        //Socket will autoclose
        try(DatagramSocket udpSocket = new DatagramSocket(serverPort)){
            //packet buffers
            byte[] inBuffer = new byte[BUFFER_SIZE];
            byte[] outBuffer = new byte[BUFFER_SIZE];
            //Show server is running
            System.out.println(SERVER_RUNNING);
            //Todo: <Add timed events here>
            while(true){
                //Construct a new request packet ready for requests
                DatagramPacket requestPacket = new DatagramPacket(inBuffer, inBuffer.length);
                //Receive incoming packets
                udpSocket.receive(requestPacket);
                //Extract the packet data
                String request = new String(requestPacket.getData()).trim();
                //Split the request up into sections separated by ":"
                String[] vals = request.split(PKT_DELIMITER);
                //Allocate sections
                String  username = vals[0], 
                        password = vals[1], 
                        menuAction = vals[2];
                //Validate person
                boolean userValidated = validateUser(username, password);
                if(userValidated){
                    doAction(menuAction);//Only process the menu action if the person is validated
                }
                else{
                    responseString = serverResponseMessage.loginError();//Invalid person reply
                }
                //Scale the response message buffer
                outBuffer = responseString.getBytes();
                //Construct a new response packet
    			DatagramPacket responseMsgPacket = new DatagramPacket(outBuffer, outBuffer.length, requestPacket.getAddress(), requestPacket.getPort());
                //Send the packet back to the requesting client
    			udpSocket.send(responseMsgPacket);
                //Flush the buffers
                Arrays.fill(outBuffer,(byte) 0);
                    Arrays.fill(inBuffer,(byte) 0);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * validateUser checks a username and password against records kept in the "people" list
     * @param username The username suppied by the client user
     * @param password The password given to be compared with the associated username
     * @return A true or false value indicating whether or not the username & password are
     * stored and match the records kept
     */
    public static boolean validateUser(String username, String password){
        boolean personFound = false;
        for(Person person: people) {
            if(person.getUsername().equals(username) && person.getPassword().equals(password)){
                personFound = true;
                currentPerson = person;
                serverResponseMessage = new ServerResponseMessage(currentPerson);
            }
        }
        return personFound;
    }

    /**
     * doAction acts on menu commands and sets the appropriate response message to be returned to the client
     * @param action A string representation of the action to be performed. 1 for 'Sign in', 2 for 'Sign out' etc
     */
    public static void doAction(String action){
        //Action should only be done if it is a number
        if(action.equals(LOGIN_CMD)){
            if(!currentPerson.signedIn()){//User wants to log in
                //Sign in & greet user
                currentPerson.setSignedIn(true);
                responseString = serverResponseMessage.greet();
                //Risk level warnings
                if(currentPerson.getRiskFactor() == 2)
                    responseString = serverResponseMessage.greet() + serverResponseMessage.mediumRisk();
                if(currentPerson.getRiskFactor() == 3)
                    responseString = serverResponseMessage.greet() + serverResponseMessage.highRisk();
                serverResponseMessage.logActivity();
            }
            else //User is already signed in
                responseString = serverResponseMessage.alreadySignedIn();
        }
        if(action.equals(LOGOUT_CMD)){//User wants to log out
            if(currentPerson.signedIn()){
                currentPerson.setSignedIn(false);
                responseString = serverResponseMessage.farewell();
                serverResponseMessage.logUserOut();
            }
            else //User is already signed out
                responseString = serverResponseMessage.alreadySignedOut();
        }
    }
    
    /**
     * This method is only called when the server first starts up, and is only intended for a 
     * one-time-use to ensure there is some 'test people' to put in the LinkedList called "people"
     */
    public static void loadSeedData(){
        File seedFile = new File(SEED_DATA_FILE);
        if(seedFile.exists()){
            //BufferedReader & FileReader will auto close
            try(BufferedReader fileReader = new BufferedReader(new FileReader(seedFile))){
                String currentLine;
                while((currentLine = fileReader.readLine()) != null){
                    String [] vals = currentLine.trim().split(CSV_DELIMITER);
                    for(int i = 0 ; i < vals.length / 2 ; i += 2){
                        people.add(new Person(vals[i], vals[i+1] , 0, false));
                    }
                }
            }
            catch(IndexOutOfBoundsException | IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println(SEED_DATA_FILE + " not found");
            System.exit(0);
        }
    }
}