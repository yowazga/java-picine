/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesServiceImpl.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 12:29:37 by Younes            #+#    #+#             */
/*   Updated: 2025/07/03 18:31:40 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.MessagesRepository;
import fr.school42.sockets.repositories.UsersRepository;

public class MessagesServiceImpl implements MessagesService{

    private UsersRepository usersRepository;

    private MessagesRepository messagesRepository;

    
    public MessagesServiceImpl(UsersRepository usersRepository, MessagesRepository messagesRepository) {
        this.usersRepository = usersRepository;
        this.messagesRepository = messagesRepository;
    }



    @Override
    public Message saveMessag(String login, String text) {
        
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login is empty");
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Password is empty");
        }

        User user = usersRepository.findByLogin(login).orElseThrow();

        Message message = new Message(user, text);
        messagesRepository.save(message);

        return message;
    }
}
