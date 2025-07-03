/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ClientHandler.java                                 :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 10:32:08 by Younes            #+#    #+#             */
/*   Updated: 2025/07/03 17:41:20 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.User;
import fr.school42.sockets.services.MessagesService;
import fr.school42.sockets.services.UsersService;

public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private final UsersService usersService;
    private final MessagesService messagesService;
    private User signedInUser;

    PrintWriter out;
    
    public ClientHandler(Socket clientSocket, UsersService usersService, MessagesService messagesService) {
        this.clientSocket = clientSocket;
        this.usersService = usersService;
        this.messagesService = messagesService;
        start();
    }


    @Override
    public void run() {
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
              
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Hello fromo server!");
            try {
                
                String command = in.readLine();
                if ("signIn".equalsIgnoreCase(command)) {
                    
                    signedInUser = authenticateUser(in, out);
                    if (signedInUser != null) {
                        out.println("Startin messaging");
                        handleClientMessage(in, out);
                    }
                } else if ("signUp".equalsIgnoreCase(command)) {
                    
                    registerUser(in, out);
                } else {
                    out.println("Invalid command");
                }
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
            Server.clientActive.remove(this);

            try {
                clientSocket.close();
            } catch (IOException e) {
                
                System.err.println("Failed to close socket: " + e.getMessage());
            }
        }
        
    }

    private User authenticateUser(BufferedReader in, PrintWriter out) throws IOException {
        
        out.println("Inter Username");
        String login = in.readLine();

        out.println("Inter Password");
        String password = in.readLine();

        try {
            return usersService.signIn(login, password);
        } catch (Exception e) {
            out.println("Invalid credentials.");
            return null;
        }
    }

    private void handleClientMessage(BufferedReader in, PrintWriter out) throws IOException {
        
        String messageText;
        while ((messageText = in.readLine()) != null) {
            
            if (messageText.equalsIgnoreCase("Exit")) {
                out.println("You have left the chat.");
                return ;
            }

            System.out.println("Message received from " + signedInUser.getLogin() + ": " + messageText);

            Message message = messagesService.saveMessag(signedInUser.getLogin(), messageText);

            broadcastMessage(signedInUser.getLogin(), message.getMessage());
        }
    }

    private void broadcastMessage(String sender, String messageText) {

        String fullMessage = sender + ": " + messageText;
        synchronized (Server.clientActive) {
            for (ClientHandler client : Server.clientActive) {
                if (client != this && client.isSignedIn())
                    client.sendMessageToClient(fullMessage);
            }
        }
        
    }

    private boolean isSignedIn() {
        return signedInUser != null;
    }

    private void sendMessageToClient(String messageText) {
        
        if (out != null && signedInUser != null) {
            out.println(messageText);
        }
       
    }

    private void registerUser(BufferedReader in, PrintWriter out) throws IOException {
        
        out.println("Inter Username");
        String login = in.readLine();

        out.println("Inter Password");
        String password = in.readLine();

       try {
            usersService.signUp(login, password);
            out.println("Registration successful!");
       } catch (Exception e) {
            out.println("Registration failed: " + e.getMessage());
       }
    }
    
}
