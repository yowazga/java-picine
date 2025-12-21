/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Server.java                                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:01:45 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 14:30:43 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.school42.sockets.services.UsersService;

@Component
public class Server {

    private final UsersService usersService;
    
    @Autowired
    public Server(UsersService usersService) {
        
        this.usersService = usersService;
    }

    public void start(int port) {
        
        System.out.println("Starting server on port " + port + "...");
        try (ServerSocket serverSocket = new ServerSocket(port);
        Socket client = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            
                out.println("Hello from server!");
                String line = in.readLine();
                
                if (line != null && line.startsWith("signUp")) {
                    
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
            System.out.println("Server error: " + e.getMessage());
        }
        System.out.println("Server shutting down.");
    }

    
}
