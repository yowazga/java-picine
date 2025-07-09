/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Client.java                                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/30 11:39:44 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 16:56:04 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import fr.school42.sockets.models.Command;

public class Client {

    public Client() {}
    
    public void start(String host, Integer port) {
        
        try (Socket socket = new Socket(host, port);
             BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter sockOut = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) {

                Thread reciveThread = new Thread(() -> {
                    
                    try {
                        while (true) {
                            String jsonString = sockIn.readLine();
                            if (jsonString == null) {
                                throw new IOException("Connection closed");
                            }
                            Command command = Command.fromJson(jsonString);

                            switch (command.getType()) {
                                case "message":
                                    System.out.println(command.getFrom() + ": " + command.getContent());
                                    break;
                                case "error":
                                    // System.out.println("ana hnaaaaa");
                                    System.out.println("Error: " + command.getContent());
                                default:
                                    if (!command.getContent().isEmpty()) {
                                        System.out.println(command.getContent());
                                    }
                            }
                            for (String option : command.getOptions()) {
                                System.out.println(option);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                });
                reciveThread.start();
                while(true) {
                    String input = userIn.readLine();
                    sockOut.println(new Command("command", "client", input).toJson());
                }
        } catch (IOException e) {
            System.err.println("Error connectiong to Server");
        }
    }
}

