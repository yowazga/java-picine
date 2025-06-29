/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   SocketsApplicationConfig.java                      :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/28 16:26:54 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 14:28:17 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

import school42.spring.service.repositories.UsersRepository;
import school42.spring.service.repositories.UsersRepositoryJdbcImpl;
import school42.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;

@Configuration
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
    public UsersRepository usersRepositoryJdbc() {
        
        return new UsersRepositoryJdbcImpl(dataSource());
    }

    @Bean
    public UsersRepository usersRepositoryJdbcTemplate() {
        
        return new UsersRepositoryJdbcTemplateImpl(dataSource());
    }
}
