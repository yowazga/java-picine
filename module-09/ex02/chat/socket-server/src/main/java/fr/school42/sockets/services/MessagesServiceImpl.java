/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesServiceImpl.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 12:29:37 by Younes            #+#    #+#             */
/*   Updated: 2025/12/22 10:13:43 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.Room;
import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.MessagesRepository;
import fr.school42.sockets.repositories.RoomsRepository;
import fr.school42.sockets.repositories.UsersRepository;

@Component
public class MessagesServiceImpl implements MessagesService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private RoomsRepository roomsRepository;

    @Override
    public Message saveMessag(String login, Long roomId, String text) {
        
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login is empty");
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text is empty");
        }
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID is empty");
        }

        User user = usersRepository.findByLogin(login).orElseThrow();

        Room room = roomsRepository.findById(roomId);
        
        System.out.println(user + " " + room);
        
        Message message = new Message(user, room, text);
        messagesRepository.save(message);

        return message;
    }

    @Override
    public List<Message> getLatestMessages(Long roomId, int limit, int offset) {
        
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID is empty");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("Limit is negative");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset is negative");
        }

        return messagesRepository.findByRoomId(roomId, limit, offset);
    }
}
