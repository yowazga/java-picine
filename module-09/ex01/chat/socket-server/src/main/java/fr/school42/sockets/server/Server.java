/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Server.java                                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:01:45 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 17:34:21 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import fr.school42.sockets.services.MessagesService;
import fr.school42.sockets.services.UsersService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Server {

    @Autowired
    private UsersService usersService;

    @Autowired
    private MessagesService messagesService;
    public static List<ClientHandler> clientActive = Collections.synchronizedList(new ArrayList<>());

    public Server() {}
    
    public void start(int port) {
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            
            System.out.println("Starting server on port " + port + "...");
            while (true) {
                
                try {
                    
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);
                    
                    clientActive.add(new ClientHandler(clientSocket, usersService, messagesService));
                } catch (Throwable e) {
                    
                    System.err.println("Failed to create client listener: " + e.getMessage());
                }
                
            }
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
        System.out.println("Server shutting down.");
    }
}
