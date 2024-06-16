/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Berbari
 */


import java.util.*;
import java.net.*;
import java.io.*;

public class ChatClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
     try { 
            Socket socket = new Socket("localhost", 1234); 
            System.out.println("Connected to the chat server!"); 
  
            // Setting up input and output streams 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); 
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
  
            // Start a thread to handle incoming messages 
            new Thread(() -> { 
                try { 
                    String serverResponse; 
                    while ((serverResponse = in.readLine()) != null) { 
                        System.out.println(serverResponse); 
                    } 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            }).start(); 
  
            // Read messages from the console and send to the server 
            Scanner scanner = new Scanner(System.in); 
            String userInput; 
            while (true) { 
                userInput = scanner.nextLine(); 
                out.println(userInput); 
            } 
             
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }
    
}
