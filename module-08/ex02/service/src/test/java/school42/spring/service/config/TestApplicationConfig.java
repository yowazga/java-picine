/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   TestApplicationConfig.java                         :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/28 16:30:00 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 11:00:42 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import school42.spring.service.repositories.UsersRepository;
import school42.spring.service.services.UsersService;
import school42.spring.service.services.UsersServiceImpl;

@Configuration
@ComponentScan("school42.spring.service")
public class TestApplicationConfig {


    @Bean(name = {"hikariDataSource", "driverManagerDataSource"})
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL )
            .addScript("classpath:schema.sql")
            .addScript("classpath:data.sql")
            .build();
    }

    @Bean
    public UsersService usersService(@Qualifier("usersRepositoryJdbc") UsersRepository usersRepository) {
        return new UsersServiceImpl(usersRepository);
    }
}
