/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Server.java                                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:01:45 by Younes            #+#    #+#             */
/*   Updated: 2025/07/04 16:48:16 by Younes           ###   ########.fr       */
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
import fr.school42.sockets.services.RoomsService;
import fr.school42.sockets.services.UsersService;

public class Server {

    private UsersService usersService;
    private MessagesService messagesService;
    private RoomsService roomsService;
    public static List<ClientHandler> clientActive = Collections.synchronizedList(new ArrayList<>());

    
    public Server(UsersService usersService, MessagesService messagesService, RoomsService roomsService) {
        this.usersService = usersService;
        this.messagesService = messagesService;
        this.roomsService = roomsService;
    }

    public Server() {}
    
    public void start(int port) {
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            
            System.out.println("Starting server on port " + port + "...");
            while (true) {
                
                try {
                    
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);
                    
                    clientActive.add(new ClientHandler(clientSocket, usersService, messagesService, roomsService));
                } catch (Throwable e) {
                    
                    System.err.println("Failed to create client listener: " + e.getMessage());
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        System.out.println("Server shutting down.");
    }
}
