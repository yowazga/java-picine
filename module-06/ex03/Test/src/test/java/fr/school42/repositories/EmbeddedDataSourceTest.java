/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   EmbeddedDataSourceTest.java                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/29 13:15:20 by Younes            #+#    #+#             */
/*   Updated: 2025/06/05 10:33:48 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.repositories;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class EmbeddedDataSourceTest {

    private DataSource dataSource;

    @BeforeEach
    void init() {
        
        this.dataSource = new EmbeddedDatabaseBuilder()
                              .setType(EmbeddedDatabaseType.HSQL)
                              .addScripts("Schema.sql", "data.sql")
                              .build();
    }

    @Test
    void testConnection() throws  SQLException{
        
        Connection connection = dataSource.getConnection();
        
        assertNotNull(connection);
        connection.close();
    }
}
