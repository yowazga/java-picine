/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   RoomsServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/04 16:26:30 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 12:36:39 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import java.util.List;
import fr.school42.sockets.models.Room;
import fr.school42.sockets.repositories.RoomsRepository;

public class RoomsServiceImpl implements RoomsService {

    private RoomsRepository roomsRepository;

    public RoomsServiceImpl(RoomsRepository roomsRepository) {
        
        this.roomsRepository = roomsRepository;
    }
    
    @Override
    public Room createRoom(String name) {
        
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (roomsRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Room already exists");
        }

        Room room = new Room(null, name);
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
