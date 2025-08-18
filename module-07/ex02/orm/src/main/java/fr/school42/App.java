/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   App.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/23 15:01:27 by Younes            #+#    #+#             */
/*   Updated: 2025/06/24 14:19:06 by Younes           ###   ########.fr       */
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
            
            User user1 = new User("Younes", "Wazga", 50);
            User user2 = new User(null, null, null);

            orm.save(user1);
            orm.save(user2);

            User getUser_db = orm.findById(2L, User.class);

            System.out.println(getUser_db);
            
            if (getUser_db != null) {
                getUser_db.setFirstName("newFirstname");
                getUser_db.setLastName("newLastname");
                getUser_db.setAge(20);
            }
            orm.update(getUser_db);
            System.out.println(getUser_db);

        } catch (Exception e) {
            System.err.println(e);
        }
      
    }
}


