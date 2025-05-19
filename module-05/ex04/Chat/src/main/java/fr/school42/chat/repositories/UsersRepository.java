/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepository.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/16 20:13:31 by Younes            #+#    #+#             */
/*   Updated: 2025/05/16 20:14:24 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.repositories;

import java.util.List;

import fr.school42.chat.models.User;

public interface UsersRepository {

    List<User> findAll(int page, int size);
}
