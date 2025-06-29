/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Main.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/26 14:42:26 by Younes            #+#    #+#             */
/*   Updated: 2025/06/28 14:41:24 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import school42.spring.service.repositories.UsersRepository;
import school42.spring.service.repositories.UsersRepositoryJdbcImpl;
import school42.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;

public class Main 
{
    public static void main( String[] args ) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml")) {
            
            UsersRepository repo1 = context.getBean("usersRepositoryJdbc", UsersRepositoryJdbcImpl.class);
            System.out.println("=== JDBC Impl ===");
            repo1.findAll().forEach(System.out::println);
    
            UsersRepository repo2 = context.getBean("UsersRepositoryJdbcTemplateImpl", UsersRepositoryJdbcTemplateImpl.class);
            System.out.println("=== JdbcTemplate Impl ===");
            repo2.findAll().forEach(System.out::println);
        }
        
    }
}
