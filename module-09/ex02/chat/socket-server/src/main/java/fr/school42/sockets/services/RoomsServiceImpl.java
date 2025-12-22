/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   RoomsServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/04 16:26:30 by Younes            #+#    #+#             */
/*   Updated: 2025/12/22 10:23:00 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.school42.sockets.models.Room;
import fr.school42.sockets.repositories.RoomsRepository;

@Component
public class RoomsServiceImpl implements RoomsService {

    @Autowired
    private RoomsRepository roomsRepository;
    
    @Override
    public Room createRoom(String name) {
        
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (roomsRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Room already exists");
        }

        Room room = new Room(name);
        roomsRepository.save(room);
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        
        return roomsRepository.findAll();
    }

    @Override
    public Room findRoomById(Long id) {
        
        Room room = roomsRepository.findById(id);

        if (room == null) {
            throw new IllegalArgumentException("Room not found.");
        }
        return room;
    }

    @Override
    public Room findRoomByName(String name) {

        return roomsRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }
}
