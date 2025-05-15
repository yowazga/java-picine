/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesRepository.java                            :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/13 13:25:06 by Younes            #+#    #+#             */
/*   Updated: 2025/05/13 13:28:11 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.repositories;

import java.util.Optional;
import fr.school42.chat.models.Message;

public interface MessagesRepository {

    Optional<Message> findById(Long id);
    
}
