/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   SocketsApplicationConfig.java                      :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:05:20 by Younes            #+#    #+#             */
/*   Updated: 2025/07/01 12:11:45 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

import fr.school42.sockets.repositories.UsersRepository;
import fr.school42.sockets.repositories.UsersRepositoryImpl;
import fr.school42.sockets.services.UsersServiceImpl;

@Configuration
@PropertySource("db.properties")
public class SocketsApplicationConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setDriverClassName(env.getProperty("db.driver.name"));

        return dataSource;
    }

    @Bean
    public UsersRepository usersRepository() {
        
        return new UsersRepositoryImpl(dataSource());
    }

    @Bean
    public UsersServiceImpl usersServiceImpl() {

        return new UsersServiceImpl(usersRepository());
    }
}
