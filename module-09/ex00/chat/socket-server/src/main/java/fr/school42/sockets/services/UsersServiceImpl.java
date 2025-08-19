/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:03:02 by Younes            #+#    #+#             */
/*   Updated: 2025/06/30 10:26:09 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import fr.school42.sockets.models.User;
import fr.school42.sockets.repositories.UsersRepository;

public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository) {
        
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Override
    public void signUp(String login, String rawPassword) {
        
        if (usersRepository.findByLogin(login).isPresent()) {
            
            throw new RuntimeException("User " + login + " already exists");
        }

        User user = new User(null, login, passwordEncoder.encode(rawPassword));

        usersRepository.save(user);
    }

}
