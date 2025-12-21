/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Message.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/02 18:31:48 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 16:54:02 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.models;

import java.util.Date;

public class Message {

    private Long id;
    private User sender;
    private String message;
    private Date sentDate;
    
    public Message() {
    }

    public Message(User sender, String text) {
        this.sender = sender;
        this.message = text;
    }

    public Message(Long id, User sender, String message, Date timestamp) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.sentDate = timestamp;
    }
    
    public Message(User sender, String message, Date timestamp) {
        this.id = id;
        this.sender = sender;
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

    @Override
    public String toString() {
        return "Message [id=" + id + ", senderId=" + sender + ", message=" + message + ", timestamp=" + sentDate
                + "]";
    }
}
