/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   RoomsRepository.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/04 16:00:33 by Younes            #+#    #+#             */
/*   Updated: 2025/07/04 16:01:32 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.util.Optional;

import fr.school42.sockets.models.Room;

public interface RoomsRepository extends CrudRepository<Room> {

    Optional<Room> findByName(String name);
}
