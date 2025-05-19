/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Message.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/06 13:13:14 by Younes            #+#    #+#             */
/*   Updated: 2025/05/15 15:56:51 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message {
    
    private Long id;
    private User author;
    private Chatroom chatroom;
    private String text;
    private LocalDateTime createdAt;

    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}

    public User getAuthor() {return this.author;}
    public void setAuthor(User author) {this.author = author;}

    public Chatroom getChatroom() {return this.chatroom;}
    public void setChatroom(Chatroom chatroom) { this.chatroom = chatroom;}

    public String getText() {return this.text;}
    public void setText(String text) {this.text = text;}

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
    
        if (o == null || getClass() != o.getClass())
            return false;
        
        Message message = (Message) o;
        
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        String formattedDate = createdAt != null ? createdAt.format(formatter) : "N/A";
        return "Message : {\n" +
               "id=" + id + '\n' +
               ", author = " + (author != null ? author.getLogin() : "null") +
               ", chatroom = " + (chatroom != null ? chatroom.getName() : "null") +
               ", textPreview = '" + (text.substring(0, Math.min(20, text.length()))) + "'" +
               ", createdAt = " + formattedDate + '}';
    }
    
}
