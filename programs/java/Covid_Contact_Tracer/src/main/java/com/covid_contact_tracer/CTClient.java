
package com.covid_contact_tracer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Contact Trace Client
 * This class does no input validation besides detecting the exit menu command. 
 * All other validation is done by the server.
 */
public class CTClient {

    //Variables
    private static int serverPort = 1001;//Server port number
    private static String serverHost = "localhost";//Server host name
    private static byte[]   inBuffer, outBuffer;
    private static String menuHeader = "~~~ COVID CONTACT TRACER ~~~";//Menu header
    private static String[] menuOptions = {//Array of menu options (these can be extended/modified here)
        "1. Sign In",
        "2. Sign Out",
        "3. Exit"//Currently the default exit option
    };
    //Constants
    private final static int BUFFER_SIZE = 1024;//Default packet buffer size
    private final static String GOODBYE_MSG = "Thanks for using the Covid Contact Tracer... See you next time";//Default exit message
    private final static String NULL_MENU_CMD = "ERROR - You have not chosen a menu option\nPlease make your selection",
                                USERNAME_REQ = "Please enter your username",
                                PASSWORD_REQ = "Please enter your password",
                                PACKET_DELIMITER = ":";
    private final static int DEFAULT_EXIT_OPTION = 3;//The number associated with the exit option
    
    public static void main(String[] args) {
        
        //Try with autoclose scanner & socket 
        try(Scanner scan = new Scanner(System.in); DatagramSocket udpSocket = new DatagramSocket()){
            //Server host address
            InetAddress serverAddress = InetAddress.getByName(serverHost);
            //Initialize packet buffers
            outBuffer = new byte[BUFFER_SIZE];
            inBuffer = new byte[BUFFER_SIZE];
            //User command
            String cmd = "";
            while(true){
                //Prints the menu (with centered menu options)
                printMenu();
                //Get the user input
                cmd = scan.nextLine();
               if(cmd.length() > 0){
                    //If exit option selecetd, break the loop and exit the system (no need to contact the server)
                    if(cmd.equals(String.valueOf(DEFAULT_EXIT_OPTION))){
                        System.out.println(GOODBYE_MSG);
                        break;
                    }
                    //Get account credentials from the user
                    System.out.println(USERNAME_REQ);
                    String username = scan.nextLine();
                    System.out.println(PASSWORD_REQ);
                    String password = scan.nextLine();
                    //Prepare the request string to send
                    String request = new String(username
                                        .concat(PACKET_DELIMITER)
                                        .concat(password)
                                        .concat(PACKET_DELIMITER)
                                        .concat(cmd));
                    System.out.println(request);
                    //Convert string request to bytes for sending
                    outBuffer = request.getBytes();
                    //Construct a new packet 
                    DatagramPacket requestPacket = new DatagramPacket(outBuffer, outBuffer.length, serverAddress, serverPort);
                    //Send the request packet to the server
                    udpSocket.send(requestPacket);
                    //Construct a response packet ready to receive server response
                    DatagramPacket responsePacket = new DatagramPacket(inBuffer, inBuffer.length);
                    //Receive the server response
                    udpSocket.receive(responsePacket);
                    //Extract packet data
                    String response = new String(responsePacket.getData()).trim();
                    //Display response message to the client user
                    System.out.println("Server Response: "+ response + "\n");
                    //Flush the buffers
                    Arrays.fill(outBuffer,(byte) 0);
                    Arrays.fill(inBuffer,(byte) 0);
               }
               else{
                   System.out.println(NULL_MENU_CMD);
               }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * printMenu simply prints the menu header and options to the console. The menu options
     * are printed central to the header width.
     * E.g.
     *          ~~~ COVID CONTACT TRACER ~~~
     *                  1. Sign In
     *                  2. Sign Out        
     *                   3. Exit
     *          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private static void printMenu(){
        System.out.println(menuHeader);
        for(String item: menuOptions){
            int padding = (menuHeader.length() /2) - (item.length() /2);
            for(int i = 0; i < padding; ++i)
                System.out.print(" ");
            System.out.println(item);
        }
        for(int i = 0; i < menuHeader.length(); ++i)
            System.out.print("~");
        System.out.println();
    }
}