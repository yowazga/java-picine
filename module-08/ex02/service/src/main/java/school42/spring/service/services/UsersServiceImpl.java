/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/28 16:28:36 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 10:04:39 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import school42.spring.service.models.User;
import school42.spring.service.repositories.UsersRepository;

@Component("usersServiceImpl")
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplate") UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    
    @Override
    public String signUp(String email) {

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        UUID uuid = UUID.randomUUID();
        this.usersRepository.save(new User(email, uuid.toString()));
        
        Optional<User> userOpt = this.usersRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            return userOpt.get().getPassword();
        }

        return null;
    }

}
