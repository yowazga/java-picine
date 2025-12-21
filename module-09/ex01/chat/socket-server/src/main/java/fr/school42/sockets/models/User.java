/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:02:02 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 16:54:45 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.models;

import java.util.Objects;

public class User {

    private Long id;
    private String login;
    private String password;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public User() {
    }

    public User(String login, String password) {
        this.login = Objects.requireNonNull(login , "login cannot be null") ;
        this.password = Objects.requireNonNull(password, "password cannot be null");
    }
    public User(Long id, String login, String password) {
        this.id = id;
        this.login = Objects.requireNonNull(login , "login cannot be null") ;
        this.password = Objects.requireNonNull(password, "password cannot be null");
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", login=" + login + ", password=" + password + "]";
    }

    
}
