/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/06 13:13:19 by Younes            #+#    #+#             */
/*   Updated: 2025/05/18 12:33:33 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private Long id;
    private String login;
    private String password;
    private List<Chatroom> createdChatrooms = new ArrayList<>();
    private List<Chatroom> participatingRooms = new ArrayList<>();
    
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}

    public String getLogin() {return this.login;}
    public void setLogin(String login) {this.login = login;}

    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}

    public List<Chatroom> getCreatedChatrooms() {return this.createdChatrooms;}
    public void setCreatedChatrooms(List<Chatroom> createdChatrooms) {this.createdChatrooms = createdChatrooms;}

    public List<Chatroom> getParticipatingRooms() {return this.participatingRooms;}
    public void setParticipatingRooms(List<Chatroom> participatingRooms) {this.participatingRooms = participatingRooms;}

    @Override
    public boolean equals(Object o) {
        
        if (this == o) {
            return true;
        } 

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        User user = (User) o;
        
        return Objects.equals(id, user.id) &&
               Objects.equals(login, user.login) &&
               Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        
        return Objects.hash(id, login, password);
    }

    @Override
    public String toString() {
        return "User{" +
           "id=" + id +
           ", login='" + login + '\'' +
           ", createdRooms=" + createdChatrooms.size() +
           ", chatRooms=" + participatingRooms.size() +
           '}';
    }
}