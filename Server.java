/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author Daniel Hirt
 * @description Client/Server App
 * @file Server.java
 * @functionality This file implements the server side of the application.
 */
import java.io.*;
import java.net.*;
import java.text.*;

public class Server
{
    // Declare variables for socket/IO information
    static DataInputStream input; 
    static DataOutputStream output;
    static ServerSocket ss;
    static Socket s;
    
    public static void main(String args[]) throws IOException
    {   
        
      
        try{  // Try/Catch for exception handling of the client request
            System.out.println("Server is started.");
            ss = new ServerSocket(3166); // Create a new Socket for responses
            System.out.println("Waiting for the client to make a request...");
            s = ss.accept(); // Accept the client request once requested
            System.out.println("Client connected successfully!");
            // Define input and output objects
            input = new DataInputStream(s.getInputStream()); 
            output = new DataOutputStream(s.getOutputStream());
            
            // Start the server
            startServer(); 
        }
        catch(IOException e){
            System.out.println("Server Error."); 
        }
        finally{ 
            try{
                input.close();
                output.close();
                ss.close();
                s.close();
                System.out.println("All sockets and I/O have been closed.");
            }
            catch(IOException e){
                System.out.println("Error closing sockets and I/O.");
            }
        }
        System.out.println("Server stopped.");
    }

    // Performs all of the logic for the server
    public static void startServer(){
        int choice;
        double amount = 0;
        double newBalance = 0;
        
        // Create a new client object
        Client client = new Client();
        DecimalFormat df = new DecimalFormat("###.##"); 
        
        try{ // Exception handling 
            
            // Do-while implemented to handle cases other than exiting the program (4)
            do{
                choice = input.readInt();
                System.out.println("The client has selected choice: " + choice);
                if(choice == 1){ // Outputs current client balance
                    output.writeDouble(client.getBalance()); 
                }
                if(choice == 2){ // Asks user for amount and calls deposit() method of client class
                    amount = input.readDouble();
                    newBalance = client.deposit(amount);
                    newBalance = Double.parseDouble(df.format(newBalance));
                    System.out.println("The client deposited $" + df.format(amount));
                    System.out.println("");
                    output.writeUTF("You chose to deposit $" + df.format(amount) + ". Your new balance is: $" + df.format(newBalance)); 
                }
                if(choice == 3){ // Asks user for withdraw amount and calls withdraw() method of client class
                    amount = input.readDouble();
                    if(client.getBalance() - amount >= 0){ 
                        newBalance = client.withdraw(amount);
                        newBalance = Double.parseDouble(df.format(newBalance));
                        System.out.println("The client withdrew $" + df.format(amount));
                        output.writeUTF("You chose to withdraw $" + df.format(amount) + ". Your new balance is: $" + df.format(newBalance)); 
                    }
                    else{ // If the input from the client is more than current account balance
                        System.out.println("The client tried to withdraw more than their balance.");
                        output.writeUTF("Your account does have the requested amount of funds.");
                    }
                }
                if(choice == 4){ // Exit the program
                    System.out.println("The client has exited the program.");
                    output.writeUTF("Thank you for using the ITCS-3166 Student Bank!");
                }
            }
            while(choice != 4); // While the choice is not equal to 4, perform the do
        }
        catch(IOException e){
        }
    }
}


   
