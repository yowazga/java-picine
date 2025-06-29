/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   TestApplicationConfig.java                         :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/28 16:30:00 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 12:20:42 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Statement;
import java.sql.Connection;

import org.h2.jdbcx.JdbcDataSource;

@Configuration
@ComponentScan("school42.spring.service")
public class TestApplicationConfig {


    @Bean
    public JdbcDataSource dataSource() {
        
        JdbcDataSource dataSource = new JdbcDataSource();

        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUser("test");
        dataSource.setPassword("");

        try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {

                stmt.execute("CREATE TABLE users (id BIGINT AUTO_INCREMENT PRIMARY KEY, email VARCHAR(255))");
            
        } catch (Exception e) {
            
            throw new RuntimeException("Failed to initialize test DB", e);
        }
        return dataSource;
    } 
}
