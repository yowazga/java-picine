/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesService.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 12:29:56 by Younes            #+#    #+#             */
/*   Updated: 2025/07/03 14:33:23 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import fr.school42.sockets.models.Message;

public interface MessagesService {

    public Message saveMessag(String login, String text);
    
}
