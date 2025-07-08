/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Message.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/02 18:31:48 by Younes            #+#    #+#             */
/*   Updated: 2025/07/05 12:30:16 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.models;

import java.util.Date;

public class Message {

    private Long id;
    private User sender;
    private Room room;
    private String message;
    private Date sentDate;
    
    public Message() {
    }

    public Message(User sender, Room room, String text) {
        this.sender = sender;
        this.room = room;
        this.message = text;
        this.sentDate = new Date();
    }

    public Message(Long id, User sender, Room room, String message, Date timestamp) {
        this.id = id;
        this.sender = sender;
        this.room = room;
        this.message = message;
        this.sentDate = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return sentDate;
    }

    public void setTimestamp(Date timestamp) {
        this.sentDate = timestamp;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", sender=" + sender + ", room=" + room + ", message=" + message + ", sentDate="
                + sentDate + "]";
    }
}
