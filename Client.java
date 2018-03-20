/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midterm;

/**
 *
 * @author Daniel Hirt
 * @description ITSC 3166 Midterm Project
 * @file Client.java
 * @functionality This file implements the client side of the application.
 */
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class Client
{
    // Declare client variables
    static Scanner keyboard = new Scanner(System.in);
    static Socket client;
    static DataInputStream input;
    static DataOutputStream output;
    
    public static void main(String args[])
    {   
      
        System.out.println("Connecting to server...");
        try{ // Exception handling in the event of unsuccessful connection.
            
            // Create a new socket for client requests
            client = new Socket("localhost", 3166); 
            System.out.println("Connection successful!");

            // Define input objects
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());

            int choice = 0;
            while(choice != 4){ // As long as the choice is not 4 (exit the program) prompt user for input
                choice = getInput();
                System.out.println("");
                sendInfo(choice);
            }
        }
        catch(IOException e){
            System.out.println("Error connecting to the server.");
        }
        finally{
            try{ 
                client.close();
                input.close();
                output.close();
                keyboard.close();
            }
            catch(IOException e){
                System.out.println("Error closing sockets and I/O.");
            }
        }
        System.exit(0);
    }

    public static int getInput(){ // Method to obtain input from the user
        
        int choice;
        
        System.out.println("Welcome to the ITSC 3166 Bank. Please select an option: " + "\n1. View Current Balance" +
            "\n2. Deposit Money" + "\n3. Withdraw Money" + "\n4. Exit");
       

        do{ // Do if the user does not input an option on the list
            System.out.print("What would you like to do?: ");
            while(!keyboard.hasNextInt()){ 
                System.out.println("Input invalid. Please input a number.");
                System.out.print("What would you like to do?: ");
                keyboard.next();
            }
            choice = keyboard.nextInt();
            if(choice < 1 || choice > 4){ // Only allow the user to select from 4 options
                System.out.println("Input invalid. Please input a number.");
            }
        }
        while(choice < 1 || choice > 4);

        return choice;
    }

    public static void sendInfo(int option){ // Method to obtain input from the user after they have selected an option
        double amount = 0;
        String response = "";
        DecimalFormat df = new DecimalFormat("###.##"); 
        if(option == 1){ // Display current balance
            try{ // Exception handling
                output.writeInt(1); 
                System.out.println("Current balance: $" + df.format(input.readDouble()));
            }
            catch(IOException e){
                System.out.println("Error communicating with the server.");
                System.exit(-1); 
            }
        }
        
        if(option == 2){ // Prompt the user to input an amount to deposit
            do{ 
                System.out.print("Enter the amount to deposit: ");
                while(!keyboard.hasNextDouble()){ 
                    System.out.println("Input invalid. Please input a number.");
                    System.out.print("Enter the amount to deposit: ");
                    keyboard.next();
                }
                amount = keyboard.nextDouble();
                if(amount < 0){ 
                    System.out.println("Input invalid. Please input a number.");
                }
            }
            while(amount < 0);
            amount = Double.parseDouble(df.format(amount));
            try{ // Exception handling
                
                output.writeInt(2); 
                output.writeDouble(amount);
                response = input.readUTF();
                System.out.println(response);
            }
            catch(IOException e){
                System.out.println("Error communicating with the server.");
                System.exit(-1);
            }
        }
        
        if(option == 3){ // Prompt the user to enter an amount to withdraw  
            do{ 
                System.out.print("Enter the amount to withdraw: ");
                while(!keyboard.hasNextDouble()){ 
                    System.out.println("Input invalid. Please input a number.");
                    System.out.print("Enter the amount to withdraw: ");
                    keyboard.next();
                }
                amount = keyboard.nextDouble();
                if(amount < 0){ 
                    System.out.println("Input invalid. Please input a number.");
                }
            }
            while(amount < 0);
            amount = Double.parseDouble(df.format(amount));
            try{
                output.writeInt(3); 
                output.writeDouble(amount);
                response = input.readUTF();
                System.out.println(response);
            }
            catch(IOException e){
                System.out.println("Error communicating with the server.");
                System.exit(-1);
            }
        }
        if(option == 4){ 
            try{
                output.writeInt(4);
                response = input.readUTF();
                System.out.println(response);
            }
            catch(IOException e){
                System.out.println("Error communicating with the server.");
                System.exit(-1);
            }
        }
        System.out.println("");
    }
    
    /*
    * The following methods perform deposit logic, withdraw logic;
    * as well as acts like a JavaBean with getters and setters for 
    * the users balance. 
    */
     private static double balance;
 
    
    public static double deposit(double amount){
        balance += amount;
        return balance;
    }
  
    public static double withdraw(double amount){
        balance -= amount;
        return balance;
    }
 
    public static double getBalance(){
        return balance;
    }
}