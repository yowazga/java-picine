/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImpl.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/28 16:28:36 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 12:44:59 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import school42.spring.service.models.User;
import school42.spring.service.repositories.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    @Qualifier("usersRepositoryJdbc")
    private UsersRepository usersRepository;
    
    @Override
    public String signUp(String email) {
        
        User user = new User(null, email);

        usersRepository.save(user);

        return UUID.randomUUID().toString();
    }

}
