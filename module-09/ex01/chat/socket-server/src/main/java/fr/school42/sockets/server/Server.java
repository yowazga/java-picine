/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Server.java                                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:01:45 by Younes            #+#    #+#             */
/*   Updated: 2025/07/01 11:50:00 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import fr.school42.sockets.services.UsersService;

public class Server {

    private final UsersService usersService;
    private final int port; 
    
    public Server(UsersService usersService, int port) {
        
        this.usersService = usersService;
        this.port = port;
    }

    public void start() {
        
        System.out.println("Starting server on port " + port + "...");
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket client = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            
                out.println("Hello from server!");
                String line = in.readLine();
                
                if (line != null && line.startsWith("/signUp")) {
                    
                    out.println("Enter username:");
                    String login = in.readLine();
                    out.println("Enter password:");
                    String password = in.readLine();

                    try {
                        
                        usersService.signUp(login, password);
                        out.println("Successful!");
                    } catch (Exception e) {
                        out.println("Signup failed: " + e.getMessage());
                    }
                } else {
                    out.println("Unknown command.");
                }
            
        } catch (IOException e) {
            e.printStackTrace();;
        }
        System.out.println("Server shutting down.");
    }

    
}
