/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersService.java                                  :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:02:50 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 14:45:56 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import fr.school42.sockets.models.User;

public interface UsersService {

    User signUp(String login, String rawPassword);

    User signIn(String login, String password);
}
