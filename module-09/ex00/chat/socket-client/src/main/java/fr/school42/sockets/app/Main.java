/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Main.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/01 13:26:40 by Younes            #+#    #+#             */
/*   Updated: 2025/07/02 18:20:14 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import fr.school42.sockets.client.Client;

@Parameters(separators = "=")
public class Main {

    @Parameter(names = "--server-port", description = "Server port", required = true)
    private int port;

    private static String host = "localhost";
    
    public static void main(String[] args) {
        
        Main main = new Main();

        try {
            JCommander.newBuilder().addObject(main).build().parse(args);;
        } catch (Exception e) {
            System.err.println("Wrong argument");
            System.exit(1);
        }
        
        Client client = new Client();

        client.start(host, main.port);
    }
}
