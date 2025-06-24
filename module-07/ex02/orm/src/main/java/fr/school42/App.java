/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   App.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/23 15:01:27 by Younes            #+#    #+#             */
/*   Updated: 2025/06/24 12:54:50 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42;

import org.sqlite.SQLiteDataSource;

import fr.school42.core.OrmManager;
import fr.school42.models.User;

public class App
{
    public static void main( String[] args ) {
    
        try {
            
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:orm.db");
            
            OrmManager orm = new OrmManager(dataSource);
            orm.init( User.class);

            User u = new User("Younes", "Wazga", 25);
            
            orm.save(u);
            
            System.out.println("get user: " + orm.findById(1L, User.class));

        } catch (Exception e) {
            System.err.println(e);
        }
      
    }
}


