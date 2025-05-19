/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Chatroom.java                                      :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/06 13:13:17 by Younes            #+#    #+#             */
/*   Updated: 2025/05/18 12:33:29 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chatroom {
    
    private Long id;
    private String name;
    private User owner;
    private List<Message> messages = new ArrayList<>();


    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public User getOwner() {return this.owner;}
    public void setOwner(User owner) {this.owner = owner;}

    public List<Message> getMessages() {return this.messages;}
    public void setMessages(List<Message> messages) {this.messages = messages;}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        
        if (o == null || getClass() != o.getClass())
            return false;
            
        Chatroom chatroom = (Chatroom) o;

        return Objects.equals(id, chatroom.id);
    }

    @Override
    public int hashCode() {
        
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        
        return "Chatroom{ " +
               "id = " + id + 
               ", name = " + name + 
               ", owner = " + (owner != null ? owner.getLogin() : "null") + 
               ", messagesCount = " + messages.size() + '}';
    }
    
}
