/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:03:02 by Younes            #+#    #+#             */
/*   Updated: 2025/12/22 10:11:57 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.UsersRepository;

@Component("usersServiceImpl")
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User signUp(String login, String rawPassword) {
        
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (usersRepository.findByLogin(login).isPresent()) {
            
            throw new IllegalArgumentException("User " + login + " already exists");
        }

        User user = new User(null, login, passwordEncoder.encode(rawPassword));

        usersRepository.save(user);

        return user;
    }

    @Override
    public User signIn(String login, String password) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        return usersRepository.findByLogin(login)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));
    }
}
