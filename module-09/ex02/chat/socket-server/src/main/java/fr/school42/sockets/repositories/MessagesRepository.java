/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesRepository.java                            :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/02 19:45:46 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 11:34:34 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.util.List;

import fr.school42.sockets.models.Message;

public interface MessagesRepository extends CrudRepository<Message> {

    List<Message> findByRoomId(Long roomId, int limit, int offset);
}
