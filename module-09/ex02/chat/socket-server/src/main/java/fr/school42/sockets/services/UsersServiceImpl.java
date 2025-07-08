/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:03:02 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 14:46:14 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.UsersRepository;

public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;
    
    private BCryptPasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository) {
        
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
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
        
        return usersRepository.findByLogin(login)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));
    }
}
