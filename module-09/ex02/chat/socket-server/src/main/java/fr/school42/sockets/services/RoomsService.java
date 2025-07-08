/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   RoomsService.java                                  :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/04 16:25:52 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 12:36:04 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import java.util.List;

import fr.school42.sockets.models.Room;

public interface RoomsService {

    Room createRoom(String name);
    
    List<Room> getAllRooms();
    
    Room findRoomById(Long id);
    
    Room findRoomByName(String name);
}
