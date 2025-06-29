/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/26 14:44:19 by Younes            #+#    #+#             */
/*   Updated: 2025/06/26 14:46:18 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.models;

public class User {

    private Long id;
    private String email;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public User() {
    }
    
    public User(Long id, String email) {
        this.id = id;
        this.email = email;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + "]";
    }
    
}
