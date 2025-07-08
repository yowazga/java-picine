/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesService.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 12:29:56 by Younes            #+#    #+#             */
/*   Updated: 2025/07/07 10:40:43 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import java.util.List;

import fr.school42.sockets.models.Message;

public interface MessagesService {

    public Message saveMessag(String login, Long roomId, String text);

    public List<Message> getLatestMessages(Long roomId, int limit, int offset);
    
}
