/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Main.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 17:54:11 by Younes            #+#    #+#             */
/*   Updated: 2025/07/02 16:04:30 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import fr.school42.sockets.config.SocketsApplicationConfig;
import fr.school42.sockets.server.Server;
import fr.school42.sockets.services.UsersService;

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
                    
            UsersService usersService = context.getBean(UsersService.class);

            Server server = new Server(usersService, port);

            server.start();
            // UsersRepository usersRepository = context.getBean(UsersRepository.class);

            // usersRepository.save(new User(null, "yowazga", "ab1234"));
        }
        
        
    }
}
