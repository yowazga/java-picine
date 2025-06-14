/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/12 18:04:36 by Younes            #+#    #+#             */
/*   Updated: 2025/06/13 16:37:02 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.services;

import fr.school42.exceptions.AlreadyAuthenticatedException;
import fr.school42.models.User;
import fr.school42.repositories.UsersRepository;

public class UsersServiceImpl {

    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    boolean authenticate(String login, String password) {

        User user = usersRepository.findByLogin(login);
        if (user.isAuthenticated()) {
            throw new AlreadyAuthenticatedException();
        }

        if (user.getPassword().equals(password)) {
            user.setAuthenticated(true);
            usersRepository.update(user);
            return true;
        }
        
        return false;
    }
}
