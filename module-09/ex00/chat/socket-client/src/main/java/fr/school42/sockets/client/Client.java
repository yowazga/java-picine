/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Client.java                                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/30 11:39:44 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 11:22:10 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Client {

    public Client() {}
    
    public void start(String host, Integer port) {
        
        try (Socket socket = new Socket(host, port);
             BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter sockOut = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) {

                String message = sockIn.readLine();
                if (message == null)
                    throw new IOException("Server has close the connection.");
                
                System.out.println(message);
                while (message != null && !message.equals("Successful!")) {
                    String line = userIn.readLine();
                    sockOut.println(line);
                    
                    message = sockIn.readLine();
                    System.out.println(message);
                    if ("exit".equalsIgnoreCase(message) || message.startsWith("Error")) {
                        break;
                    }
                }
            
        } catch (IOException e) {
            System.err.println("The server is not available!");
        } catch (Throwable e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
