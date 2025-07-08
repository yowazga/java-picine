/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesServiceImpl.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 12:29:37 by Younes            #+#    #+#             */
/*   Updated: 2025/07/07 10:42:32 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import java.util.List;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.Room;
import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.MessagesRepository;
import fr.school42.sockets.repositories.RoomsRepository;
import fr.school42.sockets.repositories.UsersRepository;

public class MessagesServiceImpl implements MessagesService{

    private UsersRepository usersRepository;

    private MessagesRepository messagesRepository;

    private RoomsRepository roomsRepository;

    
    public MessagesServiceImpl(UsersRepository usersRepository, MessagesRepository messagesRepository) {
        this.usersRepository = usersRepository;
        this.messagesRepository = messagesRepository;
    }



    @Override
    public Message saveMessag(String login, Long roomId, String text) {
        
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login is empty");
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Password is empty");
        }

        User user = usersRepository.findByLogin(login).orElseThrow();

        Room room = roomsRepository.findById(roomId);
        
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
