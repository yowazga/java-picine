/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepository.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:04:08 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 18:16:47 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.util.Optional;

import fr.school42.sockets.models.User;

public interface UsersRepository extends CrudRepository<User> {

    Optional<User> findByLogin(String login);
}
