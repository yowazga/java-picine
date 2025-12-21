/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Main.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 17:54:11 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 14:26:20 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import fr.school42.sockets.config.SocketsApplicationConfig;
import fr.school42.sockets.server.Server;

@Parameters(separators = "=")
public class Main 
{
    @Parameter(names = "--port", description = "Server port", required = true)
    private int port;

    public static void main(String[] args) {
        
        Main main = new Main();
        
        JCommander.newBuilder()
                  .addObject(main)
                  .build()
                  .parse(args);

        main.run();
    }
    
    public void run() {
    
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SocketsApplicationConfig.class)) {

            Server server = context.getBean(Server.class);

            server.start(port);
        }
        
        
    }
}
