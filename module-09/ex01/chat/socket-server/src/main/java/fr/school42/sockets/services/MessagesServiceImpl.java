/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesServiceImpl.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 12:29:37 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 17:33:52 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import org.springframework.stereotype.Component;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.MessagesRepository;
import fr.school42.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MessagesServiceImpl implements MessagesService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MessagesRepository messagesRepository;

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
