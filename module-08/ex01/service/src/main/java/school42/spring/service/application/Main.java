/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Main.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/26 14:42:26 by Younes            #+#    #+#             */
/*   Updated: 2025/06/27 18:40:00 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.application;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import school42.spring.service.models.User;
import school42.spring.service.repositories.UsersRepository;
import school42.spring.service.repositories.UsersRepositoryJdbcImpl;

public class Main 
{
    public static void main( String[] args ) {
    
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        UsersRepository usersRepository = context.getBean("usersRepositoryJdbc", UsersRepositoryJdbcImpl.class);
        
        Optional<User> optional = usersRepository.findByEmail("zakdaria@42.fr");
        
        System.out.println(optional.get());
        
        
        
    }
}
